package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstone_project.utils.InputValidator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// java code for activity_create_account.xml screen
public class CreateAccount extends AppCompatActivity {
    // open activity_create_account.xml
    TextView QRCodecreate;
    EditText InputedName, InputedStudentNumber,InputedEmail, InputedCourseYear;
    boolean isNameValid = false, isStudentNumberValid = false, isEmailValid = false, isCourseValid = false;

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
        InputedName = findViewById(R.id.inputName);
        InputedStudentNumber = findViewById(R.id.inputStudentNumber);
        InputedEmail = findViewById(R.id.inputEmail);
        InputedCourseYear = findViewById(R.id.inputCourseYear);

        RealTimeValidate(InputedName, "name");
        RealTimeValidate(InputedStudentNumber, "studentNumber");
        RealTimeValidate(InputedEmail, "email");
        RealTimeValidate(InputedCourseYear, "course");
    }

    // function to validate each input in real time
    public void RealTimeValidate(EditText text, String type){
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                boolean isValid = false;
                String errorMessage = "";
                // validate input
                switch (type) {
                    case "name":
                        isValid = InputValidator.isValidName(input);
                        errorMessage = "Invalid name. Use 2-50 letters, spaces, hyphens, or apostrophes.";
                        isNameValid = isValid;
                        break;
                    case "studentNumber":
                        isValid = InputValidator.isValidStudentNumber(input);
                        errorMessage = "Invalid student number. Use the format xx-xxxx-xxx.";
                        isStudentNumberValid = isValid;
                        break;
                    case "email":
                        isValid = InputValidator.isValidEmail(input);
                        errorMessage = "Invalid email format.";
                        isEmailValid = isValid;
                        break;
                    case "course":
                        isValid = InputValidator.isValidCourse(input);
                        errorMessage = "Invalid course. Follow the format like BSCS, BSN, BSMBA.";
                        isCourseValid = isValid;
                        break;
                }
                // error message
                if (isValid) {
                    text.setError(null);
                } else {
                    text.setError(errorMessage);
                }
            }
        });
    }
    public void getInformationCreateAccount(Intent nextActivity){

        String Name = InputedName.getText().toString();
        String StudentNumber = InputedStudentNumber.getText().toString();
        String Email = InputedEmail.getText().toString();
        String CourseYear = InputedCourseYear.getText().toString();

        nextActivity.putExtra("InputedName", Name);
        nextActivity.putExtra("InputedStudentNumber", StudentNumber);
        nextActivity.putExtra("InputedEmail", Email);
        nextActivity.putExtra("InputedCourseYear", CourseYear);
    }

    // create account button; final checkpoint if it has any invalid inputs.
    public void createQRCodeActivity(View view){
        if (!isNameValid) {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isStudentNumberValid) {
            Toast.makeText(this, "Please enter a valid student number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isEmailValid) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isCourseValid) {
            Toast.makeText(this, "Please enter a valid course", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent CreateQrCode = new Intent(CreateAccount.this, CreateQRCode.class);
        getInformationCreateAccount(CreateQrCode);
        startActivity(CreateQrCode);
    }
}