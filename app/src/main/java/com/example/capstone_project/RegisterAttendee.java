package com.example.capstone_project;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.example.capstone_project.models.Attendee;
import com.example.capstone_project.utils.EventServiceManager;
import com.example.capstone_project.utils.QRCodeScanner;

public class RegisterAttendee extends AppCompatActivity {
    private String eventId;
    private PreviewView previewView;
    private QRCodeScanner qrCodeScanner;
    private TextView header;
    private TextView attendeeStatus;
    private TextView attendeeName;

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

        startQRScanner();
    }

    private void startQRScanner() {
        qrCodeScanner.startCamera(this, this, new QRCodeScanner.QRScanCallback() {
            @Override
            public void onQRCodeScanned(String attendeeId) {
                try {
                    // TODO: get account details here & add email maybe
                    Attendee a = new Attendee("John Smith", attendeeId, "BSCS", 2);

                    previewView.setBackground(ContextCompat.getDrawable(RegisterAttendee.this, R.drawable.scan_success));
                    if (EventServiceManager.getInstance().registerAttendee(eventId, a)) {
                        attendeeStatus.setBackground(ContextCompat.getDrawable(RegisterAttendee.this, R.drawable.white_button));
                        attendeeStatus.setText(R.string.attendee_registered);
                        attendeeName.setText(EventServiceManager.getInstance().getAttendeeFromId(eventId, attendeeId).getAttendeeName());
                    } else {
                        attendeeStatus.setBackground(ContextCompat.getDrawable(RegisterAttendee.this, R.drawable.red_button));
                        attendeeStatus.setText(R.string.attendee_register_error);
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
}
