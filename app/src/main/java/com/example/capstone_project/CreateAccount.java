package com.example.capstone_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstone_project.FirebaseController.RegItFirebaseController;
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

    @SuppressLint("SetTextI18n")
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

        Intent intent = getIntent();
        String sName = intent.getStringExtra("sName");
        String sEmail = intent.getStringExtra("sEmail");
        String sCourse = intent.getStringExtra("sCourse");
        String studentNumber = intent.getStringExtra("studentNumber");

        boolean EditingInfo = getIntent().getBooleanExtra("EditingInfo", false);
        String password = intent.getStringExtra("Pass");
        if (studentNumber != null) {
            InputedStudentNumber.setText(studentNumber);
            InputedStudentNumber.setEnabled(false);
        }

        // if editing, change create account a little bit
        if(EditingInfo){
            QRCodecreate.setText("Edit Information");
            isCourseValid = true;
            isNameValid = true;
            isEmailValid = true;
            InputedStudentNumber.setEnabled(false);
            InputedStudentNumber.setAlpha(0.5f);
            Password.setEnabled(false);
            ConfirmPassword.setEnabled(false);
            InputedName.setText(sName);
            InputedStudentNumber.setText(studentNumber);
            InputedEmail.setText(sEmail);
            InputedCourseYear.setText(sCourse);
            Password.setText(password);
            Password.setAlpha(0.5f);
            ConfirmPassword.setText(password);
            ConfirmPassword.setAlpha(0.5f);
        }

        View rootView = findViewById(android.R.id.content);
        final boolean[] isKeyboardOpen = {false};
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect areawindow = new Rect();
            rootView.getWindowVisibleDisplayFrame(areawindow);
            int screenHeight = rootView.getRootView().getHeight();
            int mykeypadHeight = screenHeight - areawindow.bottom;

            // Check if the keyboard is visible
            if (mykeypadHeight > screenHeight * 0.05) {
                if (!isKeyboardOpen[0]) {
                    // Keyboard has just opened
                    isKeyboardOpen[0] = true;
                    scrollToFocusedView(rootView, areawindow);
                }
            } else {
                if (isKeyboardOpen[0]) {
                    isKeyboardOpen[0] = false;
                    rootView.scrollTo(0, 0);  // Reset scroll position when keyboard is hidden
                }
            }
        });
        addFocusChangeListener(Password, rootView);
        addFocusChangeListener(ConfirmPassword, rootView);

        showPrivacyNotice();
        RealTimeValidate(InputedName, "name");
        RealTimeValidate(InputedEmail, "email");
        RealTimeValidate(InputedCourseYear, "course");
    }
    //helper for keyboard scrolling
    private void scrollToFocusedView(View rootView, Rect areawindow) {
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            int scrollAmount = focusedView.getBottom() - areawindow.bottom + 70;
            if (scrollAmount > 0) {
                rootView.scrollBy(0, scrollAmount);
            }
        }
    }
    //another helper
    private void addFocusChangeListener(EditText editText, View rootView) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rootView.post(() -> {
                    //  reset to 0 0
                    rootView.scrollTo(0, 0);
                    //  scrolling to view
                    Rect areawindow = new Rect();
                    rootView.getWindowVisibleDisplayFrame(areawindow);
                    scrollToFocusedView(rootView, areawindow);
                });
            }
        });
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
    // helper
    private void resetFlag(String type){
        switch (type) {
            case "name":
                isNameValid = false;
            case "studentNumber":
                isStudentNumberValid = false;
                break;
            case "email":
                isEmailValid = false;
                break;
            case "course":
                isCourseValid = false;
                break;
        }
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
                if (input.isEmpty()) {
                    text.setError(null);
                    resetFlag(type);
                    return;
                }
                boolean isValid = false;
                String errorMessage = "";
                // validate input
                switch (type) {
                    case "name":
                        isValid = InputValidator.isValidName(input);
                        errorMessage = "Invalid name. Use 2-50 letters, spaces, hyphens, or apostrophes.";
                        isNameValid = isValid;
                        break;
                    case "email":
                        isValid = InputValidator.isValidEmail(input);
                        errorMessage = "Invalid email format.";
                        isEmailValid = isValid;
                        break;
                    case "course":
                        isValid = InputValidator.isValidCourse(input);
                        errorMessage = "Invalid course. Follow the format like BSCS - 2, BSN - 1, BSMBA - 3.";
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
    String Name, StudentNumber, Email, CourseYear;
    public void getInformationCreateAccount(Intent nextActivity){

        Name = InputedName.getText().toString();
        StudentNumber = InputedStudentNumber.getText().toString();
        Email = InputedEmail.getText().toString();
        CourseYear = InputedCourseYear.getText().toString();

        // creating a new Account in firebase
        RegItFirebaseController.getInstance().createNewUser(StudentNumber, Name, Email, CourseYear, Password.getText().toString());

        // Display Information in Next Activity
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