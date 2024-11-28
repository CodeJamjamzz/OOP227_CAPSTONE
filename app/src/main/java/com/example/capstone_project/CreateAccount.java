package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

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

        // TextWatcher to check if text is valid after user inputs each field
        InputedName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // way gamit
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // way gamit
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputName = s.toString().trim();

                // Validate name
                if (isValidName(inputName)) {
                    InputedName.setError(null);
                    isNameValid = true;
                } else {
                    InputedName.setError("Invalid name. Use 2-50 letters, spaces, hyphens, or apostrophes.");
                    isNameValid = false;
                }
            }
        });

// Check student number, must be  in the format of xx-xxxx-xx
        InputedStudentNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String studentNumber = s.toString().trim();

                // Validate student number format (xx-xxxx-xxx)
                if (isValidStudentNumber(studentNumber)) {
                    InputedStudentNumber.setError(null);
                    isStudentNumberValid = true;
                } else {
                    InputedStudentNumber.setError("Invalid student number. Use the format xx-xxxx-xxx.");
                    isStudentNumberValid = false;
                }
            }
        });

// Check if email kay valid format
        InputedEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();

                // Validate email format
                if (isValidEmail(email)) {
                    InputedEmail.setError(null);
                    isEmailValid = true;
                } else {
                    InputedEmail.setError("Invalid email format.");
                    isEmailValid = false;
                }
            }
        });

// Check if course is valid
        InputedCourseYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String course = s.toString().trim();

                // Validate course input (e.g., BSCS, BSN, BSMBA)
                if (isValidCourse(course)) {
                    InputedCourseYear.setError(null);
                    isCourseValid = true;
                } else {
                    InputedCourseYear.setError("Invalid course. Use uppercase letters like BSCS, BSN, BSMBA.");
                    isCourseValid = false;
                }
            }
        });
    }
    // valid name checker
    public boolean isValidName(String InputedName) {
        if (InputedName == null || InputedName.trim().isEmpty()) {
            return false; // empty or null
        }
        if (InputedName.length() < 2 || InputedName.length() > 50) {
            return false; // length out of bounds
        }
        // Regex for allowing letters, spaces, apostrophes, and hyphens
        return InputedName.matches("^[a-zA-Z\\s'-,.]+$"); // invalid characters
    }

    // valid student number checker
    public boolean isValidStudentNumber(String InputedStudentNumber) {
        if (InputedStudentNumber == null || InputedStudentNumber.trim().isEmpty()) {
            return false; // empty or null
        }
        // Regex for xx-xxxx-xxx format (numbers only)
        return InputedStudentNumber.matches("^\\d{2}-\\d{4}-\\d{3}$"); // invalid format
    }

    // valid email checker
    public boolean isValidEmail(String InputedEmail) {
        if (InputedEmail == null || InputedEmail.trim().isEmpty()) {
            return false; // empty or null
        }
        // Regex for a basic email format
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return InputedEmail.matches(emailPattern); // check if it matches the pattern
    }

    // valid course checker (BSCS, BSN, BSMBA, etc.)
    public boolean isValidCourse(String InputedCourseYear) {
        if (InputedCourseYear == null || InputedCourseYear.trim().isEmpty()) {
            return false; // empty or null
        }
        // Regex to allow uppercase letters (e.g., BSCS, BSN, BSMBA)
        return InputedCourseYear.matches("^[A-Z]{2,}-[1-4]$"); // invalid course format
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

    // create account button; need to add qr generation and input verification
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