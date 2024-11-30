package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.graphics.Bitmap;
import android.widget.TextView;
import android.widget.ImageView;
import com.google.zxing.WriterException;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// java code for activity_create_qrcode.xml screen
public class CreateQRCode extends AppCompatActivity {
    // open activity_create_qrcode.xml

    //different textView for the display
    TextView DisplayName;
    TextView DisplayStudentNumber;
    TextView DisplayEmail;
    TextView DisplayCourseYear;
    ImageView qrCodeImageView;

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DisplayInfo();
        generateAndDisplayQRCode();
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

    // function to return to main page
    public void returnMainActivity(View view){
        startActivity(new Intent(CreateQRCode.this , MainActivity.class));
    }

}