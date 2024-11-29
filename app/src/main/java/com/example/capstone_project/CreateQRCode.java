package com.example.capstone_project;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.WriterException;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.OutputStream;

// java code for activity_create_qrcode.xml screen
public class CreateQRCode extends AppCompatActivity {
    // open activity_create_qrcode.xml

    //different textView for the display
    TextView DisplayName, DisplayStudentNumber, DisplayEmail, DisplayCourseYear;
    ImageView qrCodeImageView;
    Button saveToGalleryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_qrcode);

        DisplayName = findViewById(R.id.outputNameDisplay);
        DisplayStudentNumber = findViewById(R.id.outputStudentNumberDisplay);
        DisplayEmail = findViewById(R.id.outputEmailDisplay);
        DisplayCourseYear = findViewById(R.id.outputCourseYearDisplay);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        saveToGalleryButton = findViewById(R.id.savetoGallery);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DisplayInfo();
        generateAndDisplayQRCode();
        saveToGalleryButton.setOnClickListener(v -> saveQRCodeToGallery());
    }

    public void DisplayInfo(){
        Intent previousActivity = getIntent();
        DisplayName.setText(previousActivity.getStringExtra("InputedName"));
        DisplayStudentNumber.setText(previousActivity.getStringExtra("InputedStudentNumber"));
        DisplayEmail.setText(previousActivity.getStringExtra("InputedEmail"));
        DisplayCourseYear.setText(previousActivity.getStringExtra("InputedCourseYear"));
    }
    // function that generates QR
    private void generateAndDisplayQRCode() {
        try {
            String data = "{"
                    + "\"name\":\"" + DisplayName.getText().toString() + "\","
                    + "\"studentNumber\":\"" + DisplayStudentNumber.getText().toString() + "\","
                    + "\"email\":\"" + DisplayEmail.getText().toString() + "\","
                    + "\"course\":\"" + DisplayCourseYear.getText().toString() + "\""
                    + "}";

            QRCodeGenerator qrCodeGenerator = QRCodeGenerator.getInstance();
            Bitmap qrCodeBitmap = qrCodeGenerator.generateQRCode(data);

            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    // function that saves qr code to gallery w/ da use of mediastore
    private void saveQRCodeToGallery() {
        BitmapDrawable drawable = (BitmapDrawable) qrCodeImageView.getDrawable();
        Bitmap bitmap = drawable != null ? drawable.getBitmap() : null;

        if (bitmap != null) {
            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            // data chu2
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "QRCode_" + System.currentTimeMillis() + ".png");
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QR Codes");

            Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            try (OutputStream outputStream = contentResolver.openOutputStream(imageUri)) {
                if (outputStream != null) {
                    // connect da bitmap to the output stream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    Toast.makeText(this, "QR Code saved to gallery", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save QR Code", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No QR Code to save", Toast.LENGTH_SHORT).show();
        }
    }

    // function to return to main page
    public void returnMainActivity(View view){
        startActivity(new Intent(CreateQRCode.this , MainActivity.class));
    }

}