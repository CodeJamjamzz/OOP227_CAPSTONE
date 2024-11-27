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
    EditText inputNameEditText, inputStudentNumberEditText, inputEmailEditText, inputCourseEditText;
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

        // connect sa xml
        QRCodecreate = (TextView) findViewById(R.id.QRCodeCreateActivity);
        inputNameEditText = findViewById(R.id.inputName);
        inputStudentNumberEditText = findViewById(R.id.inputStudentNumber);
        inputEmailEditText = findViewById(R.id.inputEmail);
        inputCourseEditText = findViewById(R.id.inputCourse);

        // TextWatcher to check if text is valid after user inputs each field
        inputNameEditText.addTextChangedListener(new TextWatcher() {
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
                    inputNameEditText.setError(null);
                    isNameValid = true;
                } else {
                    inputNameEditText.setError("Invalid name. Use 2-50 letters, spaces, hyphens, or apostrophes.");
                    isNameValid = false;
                }
            }
        });

        // Check student number, must be in the format of xx-xxxx-xx
        inputStudentNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String studentNumber = s.toString().trim();

                // Validate student number format (xx-xxxx-xxx)
                if (isValidStudentNumber(studentNumber)) {
                    inputStudentNumberEditText.setError(null);
                    isStudentNumberValid = true;
                } else {
                    inputStudentNumberEditText.setError("Invalid student number. Use the format xx-xxxx-xxx.");
                    isStudentNumberValid = false;
                }
            }
        });

        // Check if email kay valid format
        inputEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();

                // Validate email format
                if (isValidEmail(email)) {
                    inputEmailEditText.setError(null);
                    isEmailValid = true;
                } else {
                    inputEmailEditText.setError("Invalid email format.");
                    isEmailValid = false;
                }
            }
        });

        // Check if course is valid
        inputCourseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String course = s.toString().trim();

                // Validate course input (e.g., BSCS, BSN, BSMBA)
                if (isValidCourse(course)) {
                    inputCourseEditText.setError(null);
                    isCourseValid = true;
                } else {
                    inputCourseEditText.setError("Invalid course. Use uppercase letters like BSCS, BSN, BSMBA.");
                    isCourseValid = false;
                }
            }
        });
    }

    // valid name checker
    public boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false; // empty or null
        }
        if (name.length() < 2 || name.length() > 50) {
            return false; // length out of bounds
        }
        // Regex for allowing letters, spaces, apostrophes, and hyphens
        if (!name.matches("^[a-zA-Z\\s'-,.]+$")) {
            return false; // invalid characters
        }
        return true;
    }

    // valid student number checker
    public boolean isValidStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            return false; // empty or null
        }
        // Regex for xx-xxxx-xxx format (numbers only)
        if (!studentNumber.matches("^\\d{2}-\\d{4}-\\d{3}$")) {
            return false; // invalid format
        }
        return true;
    }

    // valid email checker
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false; // empty or null
        }
        // Regex for a basic email format
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern); // check if it matches the pattern
    }

    // valid course checker (BSCS, BSN, BSMBA, etc.)
    public boolean isValidCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            return false; // empty or null
        }
        // Regex to allow uppercase letters (e.g., BSCS, BSN, BSMBA)
        if (!course.matches("^[A-Z]{1,4}[A-Z]{1,4}$")) {
            return false; // invalid course format
        }
        return true;
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

        startActivity(new Intent(CreateAccount.this, CreateQRCode.class));
    }
}