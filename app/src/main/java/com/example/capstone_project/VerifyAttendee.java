package com.example.capstone_project;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.capstone_project.models.EventServiceManager;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class VerifyAttendee extends AppCompatActivity {
    private String eventId;
    private PreviewView previewView;
    private BarcodeScanner scanner;
    private ProcessCameraProvider cameraProvider;
    private ImageAnalysis imageAnalysis;

    private static final String TAG = "QRScanner";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{android.Manifest.permission.CAMERA};

    private boolean isScanning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_attendant);
        eventId = getIntent().getStringExtra("EVENT_ID");

        previewView = findViewById(R.id.viewFinder);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                // Build Preview use case
                Preview preview = new Preview.Builder()
                        .build();

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Build ImageAnalysis use case
                imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                scanner = BarcodeScanning.getClient();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
                    @OptIn(markerClass = ExperimentalGetImage.class)
                    @Override
                    public void analyze(@NonNull ImageProxy image) {
                        if (!isScanning) {
                            image.close();
                            return;
                        }

                        InputImage inputImage = InputImage.fromMediaImage(
                                Objects.requireNonNull(image.getImage()),
                                image.getImageInfo().getRotationDegrees()
                        );

                        Task<List<Barcode>> result = scanner.process(inputImage)
                                .addOnSuccessListener(barcodes -> {
                                    for (Barcode barcode : barcodes) {
                                        // Verification of attendee
                                        String scanResult = barcode.getRawValue();
                                        if (scanResult != null) {
                                            Log.d(TAG, "Scanned: " + scanResult);
                                            pauseScanning();
                                            processScanResults(scanResult);
                                        }
                                    }
                                })
                                .addOnCompleteListener(task -> image.close());
                    }
                });

                // Select back camera
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                // Must unbind use cases before rebinding
                cameraProvider.unbindAll();

                // Bind use cases to camera
                Camera camera = cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageAnalysis
                );

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Use case binding failed", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void pauseScanning() {
        isScanning = false;
    }

    private void resumeScanning() {
        isScanning = true;
    }

    private void processScanResults(String attendeeId) {
        // Verify attendee logic
        EventServiceManager em = EventServiceManager.getInstance();
        try {
            if (em.verifyAttendee(eventId, attendeeId)) {
                // Change status state
            } else {
                // Also change status state (attendee not in event)
            }
        } catch (IllegalStateException e) {
            // Event somehow invalid idk why, do something
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this,
                                "Permissions not granted by the user.",
                                Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseScanning();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeScanning();
    }
}