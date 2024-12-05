package com.example.capstone_project;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstone_project.utils.InputValidator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// java code for activity_create_account.xml screen
public class CreateAccount extends AppCompatActivity {
    // open activity_create_account.xml
    TextView QRCodecreate;
    EditText InputedName, InputedStudentNumber,InputedEmail, InputedCourseYear, Password, ConfirmPassword;
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
        Password = findViewById(R.id.inputPassword);
        ConfirmPassword = findViewById(R.id.inputConfirmPassword);


        View rootView = findViewById(android.R.id.content);
        final boolean[] isKeyboardOpen = {false};
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect areawindow = new Rect();
                rootView.getWindowVisibleDisplayFrame(areawindow);
                int screenHeight = rootView.getRootView().getHeight();
                int mykeypadHeight = screenHeight - areawindow.bottom;

                // Check if the keyboard is visible
                if (mykeypadHeight > screenHeight * 0.05) {
                    if (!isKeyboardOpen[0]) {
                        // Keyboard has just opened
                        isKeyboardOpen[0] = true;
                        int scrollAmount = InputedCourseYear.getBottom() - areawindow.bottom + 70;
                        if (scrollAmount > 0) {
                            rootView.scrollBy(0, scrollAmount);  // Scroll only when keyboard appears
                        }
                    }
                } else {
                    if (isKeyboardOpen[0]) {
                        isKeyboardOpen[0] = false;
                        rootView.scrollTo(0, 0);  // Reset scroll position when keyboard is hidden
                    }
                }
            }
        });
        showPrivacyNotice();
        RealTimeValidate(InputedName, "name");
        RealTimeValidate(InputedStudentNumber, "studentNumber");
        RealTimeValidate(InputedEmail, "email");
        RealTimeValidate(InputedCourseYear, "course");
    }
    // function to show privacy notice in a popup
    private void showPrivacyNotice() {
        AlertDialog.Builder popup = new AlertDialog.Builder(this);
        popup.setTitle("Privacy Notice");
        popup.setMessage("The information you provide will be used exclusively to enhance the " +
                        "success of the organization's future events. " +
                        "This data will help us improve planning, " +
                        "communication, and event experiences. " +
                        "Your information will not be shared with third parties " +
                        "or used for any unrelated purposes. " +
                        "We are committed to protecting your privacy and ensuring your data is handled responsibly");
        popup.setPositiveButton("Agree", (dialog, which) -> {
            Toast.makeText(this, "You agreed to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        popup.setCancelable(false);
        AlertDialog dialog = popup.create();
        dialog.show();
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
        String pass1 = Password.getText().toString();
        String pass2 = ConfirmPassword.getText().toString();
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
        if (!(pass1.equals(pass2))) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent CreateQrCode = new Intent(CreateAccount.this, CreateQRCode.class);
        getInformationCreateAccount(CreateQrCode);
        startActivity(CreateQrCode);
    }
}