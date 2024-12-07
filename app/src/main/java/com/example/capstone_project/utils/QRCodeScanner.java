package com.example.capstone_project.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class QRCodeScanner {
    private static final String TAG = "QRCodeScanner";
    private static final long SCAN_DELAY_MILLIS = 1000;

    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private BarcodeScanner scanner;
    private ImageAnalysis imageAnalysis;
    private Camera camera;
    private boolean isScanning = true;
    private Handler delayHandler;

    public interface QRScanCallback {
        void onQRCodeScanned(String scanResult);
        void onScanningPaused();
        void onScanningResumed();
    }

    public QRCodeScanner(PreviewView previewView) {
        this.previewView = previewView;
        this.scanner = BarcodeScanning.getClient();
        this.delayHandler = new Handler(Looper.getMainLooper());
    }

    public void startCamera(Context context, LifecycleOwner lifecycleOwner, QRScanCallback callback) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                setupBarcodeAnalyzer(context, callback);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();

                camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                );

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Use case binding failed", e);
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private void setupBarcodeAnalyzer(Context context, QRScanCallback callback) {
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), new ImageAnalysis.Analyzer() {
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
                                String scanResult = barcode.getRawValue();
                                if (scanResult != null) {
                                    Log.d(TAG, "Scanned: " + scanResult);
                                    pauseScanning(callback);

                                    callback.onQRCodeScanned(scanResult);

                                    delayHandler.postDelayed(() -> {
                                        resumeScanning(callback);
                                    }, SCAN_DELAY_MILLIS);
                                }
                            }
                        })
                        .addOnCompleteListener(task -> {
                            image.close();
                        });
            }
        });
    }

    public void pauseScanning(QRScanCallback callback) {
        isScanning = false;
        if (callback != null) {
            callback.onScanningPaused();
        }
    }

    public void resumeScanning(QRScanCallback callback) {
        isScanning = true;
        if (callback != null) {
            callback.onScanningResumed();
        }
    }

    public void stopCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        if (delayHandler != null) {
            delayHandler.removeCallbacksAndMessages(null);
        }
    }
}