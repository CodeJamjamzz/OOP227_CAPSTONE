package com.example.capstone_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstone_project.models.Event;

import java.util.Calendar;
import java.util.Locale;

public class EventForms extends AppCompatActivity {

    EditText EventName, EventDesription, EventVenue, EventAudienceLimit, EventDate, EventStart, EventEnd;
    int inputtedYear, inputtedMonth, inputtedDay;
    int inputtedHourStart, inputtedMinuteStart;
    int inputtedHourEnd, inputtedMinuteEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_forms);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EventDate = findViewById(R.id.inputEventDate);
        EventStart = findViewById(R.id.inputEventStart);
        EventEnd = findViewById(R.id.inputEventEnd);

        EventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                inputtedYear = calendar.get(Calendar.YEAR);
                inputtedMonth = calendar.get(Calendar.MONTH);
                inputtedDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(EventForms.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        inputtedYear = year;
                        inputtedMonth = month;
                        inputtedDay = dayOfMonth;

                        String date = inputtedMonth + "/" + inputtedDay + "/" + inputtedYear;
                        EventDate.setText(date);
                    }
                }, inputtedMonth,inputtedDay,inputtedYear);
                dialog.show();
            }
        });

        EventStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                inputtedHourStart = calendar.get(Calendar.HOUR_OF_DAY);
                inputtedMinuteStart = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog;
                dialog = new TimePickerDialog(EventForms.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        inputtedHourStart = hourOfDay;
                        inputtedMinuteStart = minute;

                        if (inputtedHourStart <= 12){
                            EventStart.setText(String.format(Locale.getDefault(), "%d:%d am", inputtedHourStart, inputtedMinuteStart));
                        }else{
                            EventStart.setText(String.format(Locale.getDefault(), "%d:%d pm", inputtedHourStart, inputtedMinuteStart));
                        }
                    }
                }, inputtedHourStart, inputtedMinuteStart, true);
                dialog.show();
            }
        });

        EventEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                inputtedHourEnd = calendar.get(Calendar.HOUR_OF_DAY);
                inputtedMinuteEnd = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog;
                dialog = new TimePickerDialog(EventForms.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        inputtedHourEnd = hourOfDay;
                        inputtedMinuteEnd = minute;

                        if (inputtedHourEnd <= 12){
                            EventEnd.setText(String.format(Locale.getDefault(), "%d:%d am", inputtedHourEnd, inputtedMinuteEnd));
                        }else{
                            EventEnd.setText(String.format(Locale.getDefault(), "%d:%d pm", inputtedHourEnd, inputtedMinuteEnd));
                        }
                    }
                }, inputtedHourEnd, inputtedMinuteEnd, true);
                dialog.show();
            }
        });
    }

//    public void addEvent(){
//
//    }
}