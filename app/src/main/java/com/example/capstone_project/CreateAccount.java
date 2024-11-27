package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// java code for activity_create_account.xml screen
public class CreateAccount extends AppCompatActivity {
    // open activity_create_account.xml
    TextView QRCodecreate;
    EditText InputedName;
    EditText InputedStudentNumber;
    EditText InputedEmail;
    EditText InputedCourseYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.studentnumber), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        QRCodecreate = findViewById(R.id.QRCodeCreateActivity);
    }

    public void getInformationCreateAccount(Intent nextActivity){
        InputedName = findViewById(R.id.inputName);
        InputedStudentNumber = findViewById(R.id.inputStudentNumber);
        InputedEmail = findViewById(R.id.inputEmail);
        InputedCourseYear = findViewById(R.id.inputCourseYear);

        String Name = InputedName.getText().toString();
        String StudentNumber = InputedStudentNumber.getText().toString();
        String Email = InputedEmail.getText().toString();
        String CourseYear = InputedCourseYear.getText().toString();

        nextActivity.putExtra("InputedName", Name);
        nextActivity.putExtra("InputedStudentNumber", StudentNumber);
        nextActivity.putExtra("InputedEmail", Email);
        nextActivity.putExtra("InputedCourseYear", CourseYear);
        return;
    }

    // create account button; need to add qr generation and input verification
    public void createQRCodeActivity(View view){
        Intent CreateQrCode = new Intent(CreateAccount.this, CreateQRCode.class);
        getInformationCreateAccount(CreateQrCode);
        startActivity(CreateQrCode);
    }
}