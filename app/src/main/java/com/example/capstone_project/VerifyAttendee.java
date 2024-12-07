package com.example.capstone_project;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.capstone_project.utils.EventServiceManager;
import com.example.capstone_project.utils.QRCodeScanner;

public class VerifyAttendee extends AppCompatActivity {
    private String eventId;
    private PreviewView previewView;
    private QRCodeScanner qrCodeScanner;
    private TextView attendeeStatus;
    private TextView attendeeName;

    private static final String TAG = "QRScanner";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{android.Manifest.permission.CAMERA};

    private boolean isScanning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_attendee);
        eventId = getIntent().getStringExtra("SELECTED_EVENT_ID");

        previewView = findViewById(R.id.viewFinder);
        attendeeStatus = findViewById(R.id.verifyAttendantStatus);
        attendeeName = findViewById(R.id.attendeeName);

        qrCodeScanner = new QRCodeScanner(previewView);

        if (allPermissionsGranted()) {
            startQRScanner();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startQRScanner() {
        qrCodeScanner.startCamera(this, this, new QRCodeScanner.QRScanCallback() {
            @Override
            public void onQRCodeScanned(String attendeeId) {
                try {
                    previewView.setBackground(ContextCompat.getDrawable(VerifyAttendee.this, R.drawable.scan_success));
                    if (EventServiceManager.getInstance().verifyAttendee(eventId, attendeeId)) {
                        attendeeStatus.setBackground(ContextCompat.getDrawable(VerifyAttendee.this, R.drawable.white_button));
                        attendeeStatus.setText(R.string.attendee_verified);
                        attendeeName.setText(EventServiceManager.getInstance().getAttendeeFromId(eventId, attendeeId).getName());
                    } else {
                        attendeeStatus.setBackground(ContextCompat.getDrawable(VerifyAttendee.this, R.drawable.red_button));
                        attendeeStatus.setText(R.string.attendee_verifail);
                    }
                } catch (IllegalStateException e) {
                    Log.e("QRScanner", "Invalid event state", e);
                }
                qrCodeScanner.resumeScanning(this);
            }

            @Override
            public void onScanningPaused() {
                // Optional: Add any UI updates when scanning is paused
            }

            @Override
            public void onScanningResumed() {
                // Optional: Add any UI updates when scanning is resumed
            }
        });
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
//        attendeeStatus.setBackgroundResource(R.drawable.yellow_button);
//        attendeeStatus.setText(R.string.waiting_for_scan);
//        previewView.setBackground(null);
    }

    private void processScanResults(String attendeeId) {
        // TODO: fix crashes (usually if attendant not in event)
        // Verify attendee logic
        runOnUiThread(()-> {
            try {
                previewView.setBackground(ContextCompat.getDrawable(this, R.drawable.scan_success));
                if (EventServiceManager.getInstance().verifyAttendee(eventId, attendeeId)) {
                    Log.d("QRScanner", "Attendee verified");
                    attendeeStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.white_button));
                    attendeeStatus.setTextColor(ContextCompat.getColor(this,R.color.white));
                    attendeeStatus.setText(R.string.attendee_verified);
                    attendeeName.setText(EventServiceManager.getInstance().getAttendeeFromId(eventId, attendeeId).getName());
                } else {
                    Log.d("QRScanner", "Attendee verification failed");
                    attendeeStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.red_button));
                    attendeeStatus.setTextColor(ContextCompat.getColor(this,R.color.white));
                    attendeeStatus.setText(R.string.attendee_verifail);
                }
            } catch (IllegalStateException e) {
                Log.e("QRScanner", "Invalid event state", e);
            }
        });
        resumeScanning();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startQRScanner();
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
        qrCodeScanner.pauseScanning(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeScanner.resumeScanning(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        qrCodeScanner.stopCamera();
    }
}