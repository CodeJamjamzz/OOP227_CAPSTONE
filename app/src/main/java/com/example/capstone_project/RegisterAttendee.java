package com.example.capstone_project;

import static com.example.capstone_project.utils.PasswordEncryptor.checkPassword;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.capstone_project.FirebaseController.RegItFirebaseController;
import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.utils.EventServiceManager;
import com.example.capstone_project.utils.QRCodeScanner;

import java.util.concurrent.CompletableFuture;

public class RegisterAttendee extends AppCompatActivity {
    private String eventId;
    private PreviewView previewView;
    private QRCodeScanner qrCodeScanner;
    private TextView header;
    private TextView attendeeStatus;
    private TextView attendeeName;

    private static final String TAG = "RegisterAttendee";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{android.Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_attendee);

        eventId = getIntent().getStringExtra("SELECTED_EVENT_ID");
        header = findViewById(R.id.textView);
        previewView = findViewById(R.id.viewFinder);
        attendeeStatus = findViewById(R.id.verifyAttendantStatus);
        attendeeName = findViewById(R.id.attendeeName);

        qrCodeScanner = new QRCodeScanner(previewView);

        header.setText("Register Attendee");

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
                    // Split the string using the ' - ' as the delimiter
                    String[] parts = attendeeId.split(" - ");
                    if(parts.length != 3) throw new RuntimeException();
                    // Access the two parts
                    String p1 = parts[0];
                    String p2 = parts[1];
                    String p3 = parts[2];
                    System.out.println("p1 and p2 is: "+ p1 +" dawawd " + p2);
                    System.out.println(p3);
                    if(!checkPassword(p1 + " - " + p2, p3)) {
                        System.out.println("Wrong password");
                        throw new RuntimeException();
                    }

                    previewView.setBackground(ContextCompat.getDrawable(RegisterAttendee.this, R.drawable.scan_success));
                    Attendee a = new Attendee(p1, p2);

                    CompletableFuture<Boolean> future = EventServiceManager.getInstance().registerAttendee(eventId, a);
                    future.thenAccept(result -> {
                        if (result) {
                            // Attendee added successfully
                            RegItFirebaseController.getInstance().addEventToUserAccount(eventId, p1);
                            EventServiceManager.updateEvents();
                            System.out.println("Attendee added successfully");
                            attendeeStatus.setBackground(ContextCompat.getDrawable(RegisterAttendee.this, R.drawable.white_button));
                            attendeeStatus.setTextColor(ContextCompat.getColor(RegisterAttendee.this, R.color.green));
                            attendeeStatus.setText(R.string.attendee_registered);
                            attendeeName.setText(p2);
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                finish();
                            }, 1500); // 2000 milliseconds = 2 seconds
                        } else {
                            // Attendee already exists or an error occurred
                            attendeeStatus.setBackground(ContextCompat.getDrawable(RegisterAttendee.this, R.drawable.red_button));
                            attendeeStatus.setTextColor(ContextCompat.getColor(RegisterAttendee.this, R.color.white));
                            attendeeStatus.setText(R.string.attendee_register_error);
                            System.out.println("Attendee already exists or failed to add");
                        }
                    }).exceptionally(e -> {
                        // Handle exceptions
                        System.out.println("Error adding attendee: " + e.getMessage());
                        return null;
                    });
                } catch (IllegalStateException e) {
                    Log.e("QRScanner", "Invalid event state", e);
                } catch (RuntimeException e) {
                    Log.e("QRScanner", "Invalid QR", e);
                    attendeeStatus.setBackground(ContextCompat.getDrawable(RegisterAttendee.this, R.drawable.red_button));
                    attendeeStatus.setTextColor(ContextCompat.getColor(RegisterAttendee.this, R.color.white));
                    attendeeStatus.setText("INVALID QR");
                }
            }

            @Override
            public void onScanningPaused() {
                // Optional: Add any UI updates when scanning is paused
            }

            @Override
            public void onScanningResumed() {
                attendeeStatus.setText(R.string.waiting_for_scan);
                attendeeStatus.setTextColor(ContextCompat.getColor(RegisterAttendee.this, R.color.green));
                attendeeStatus.setBackground(ContextCompat.getDrawable(RegisterAttendee.this, R.drawable.yellow_button));
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
