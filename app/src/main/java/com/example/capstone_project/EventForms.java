package com.example.capstone_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.time.LocalTime;
import java.time.Year;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstone_project.models.Event;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class EventForms extends AppCompatActivity {

    EditText EventName, EventDesription, EventVenue, EventAudienceLimit, EventDate, EventStart, EventEnd, EventDateEnd;
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

        EventDate = findViewById(R.id.inputEventStartDate);
        EventDateEnd = findViewById(R.id.inputEventEndDate);
        EventStart = findViewById(R.id.inputEventStart);
        EventEnd = findViewById(R.id.inputEventEnd);

        EventDate.setFocusable(false);
        EventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EventForms.this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String formattedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                                EventDate.setText(formattedDate);
                            }
                        },
                        currentYear, currentMonth, currentDay
                );
                dialog.show();
            }
        });

        EventDateEnd.setFocusable(false);
        EventDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EventForms.this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String formattedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                        EventDateEnd.setText(formattedDate);
                    }
                },
                        currentYear, currentMonth, currentDay
                );
                dialog.show();
            }
        });

        EventStart.setFocusable(false);
        EventStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                inputtedHourStart = calendar.get(Calendar.HOUR_OF_DAY);
                inputtedMinuteStart = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(EventForms.this, R.style.CustomTimePickerDialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                inputtedHourStart = hourOfDay;
                                inputtedMinuteStart = minute;

                                LocalTime start = LocalTime.of(hourOfDay, minute);
                                DateTimeFormatter startFormat = DateTimeFormatter.ofPattern("hh:mm a");
                                String startTime = start.format(startFormat);
                                EventStart.setText(startTime);
                            }
                        }, inputtedHourStart, inputtedMinuteStart, false);

                dialog.show();
            }
        });

        EventEnd.setFocusable(false);
        EventEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                inputtedHourEnd = calendar.get(Calendar.HOUR_OF_DAY);
                inputtedMinuteEnd = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(EventForms.this, R.style.CustomTimePickerDialog, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            inputtedHourEnd = hourOfDay;
                            inputtedMinuteEnd = minute;

                            LocalTime start = LocalTime.of(hourOfDay, minute);
                            DateTimeFormatter startFormat = DateTimeFormatter.ofPattern("hh:mm a");
                            String startTime = start.format(startFormat);
                            EventEnd.setText(startTime);
                        }
                    }, inputtedHourStart, inputtedMinuteStart, false);
                dialog.show();
            }
        });
    }


}