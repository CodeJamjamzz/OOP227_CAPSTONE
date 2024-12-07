package com.example.capstone_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.capstone_project.models.Event;
import com.example.capstone_project.utils.InputValidator;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstone_project.utils.EventServiceManager;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class EventForms extends AppCompatActivity {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/d/yyyy hh:mm a");
    EditText EventName, EventDescription, EventVenue, EventAudienceLimit, EventDate, EventStart,
             EventEnd, EventDateEnd, EventTicketPrice;
    int inputtedHourStart, inputtedMinuteStart;
    int inputtedHourEnd, inputtedMinuteEnd;
    boolean InputEventNameValidator = false , InputEventTicketPriceValidator = false, InputEventAudienceLimitValidator = false;
    Button createEventButton;

    private Event event;
    boolean editMode = false;

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

        EventName = findViewById(R.id.inputEventName);
        EventDescription = findViewById(R.id.inputEventDescription);
        EventVenue = findViewById(R.id.inputEventVenue);
        EventTicketPrice = findViewById(R.id.inputEventTicket);
        EventAudienceLimit = findViewById(R.id.inputEventLimit);
        EventDate = findViewById(R.id.inputEventStartDate);
        EventDateEnd = findViewById(R.id.inputEventEndDate);
        EventStart = findViewById(R.id.inputEventStart);
        EventEnd = findViewById(R.id.inputEventEnd);
        createEventButton = findViewById(R.id.CreateEvent);

        inputValidation(EventName, "EventName");
        inputValidation(EventAudienceLimit, "EventLimit");
        inputValidation(EventTicketPrice, "EventPrice");

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

        String eventId = getIntent().getStringExtra("SELECTED_EVENT_ID");
        if (eventId != null) {
            if (!eventId.isEmpty()) {
                event = EventServiceManager.getInstance().getEventFromId(eventId);
                EventName.setText(event.getName());
                EventDescription.setText(event.getDescription());
                EventVenue.setText(event.getVenue());
                EventTicketPrice.setText(String.format("%.2f", event.getTicketPrice()));
                EventAudienceLimit.setText(String.format("%d", event.getAudienceLimit()));
                createEventButton.setText(R.string.confirmEdit);
                if (event.getStartDate() != null) {
                    EventDate.setText(event.getStartDate());
//                    EventDate.setText(event.getStartDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
//                    EventStart.setText(event.getStartDate().format(DateTimeFormatter.ofPattern("hh:mm a")));
                }
                if (event.getEndDate() != null) {
                    EventDate.setText(event.getEndDate());
//                    EventDateEnd.setText(event.getEndDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
//                    EventEnd.setText(event.getEndDate().format(DateTimeFormatter.ofPattern("hh:mm a")));
                }
                editMode = true;
            }
        }

        // DONE: input validation to prevent crashing
        createEventButton.setOnClickListener(v -> {
            if(!InputEventNameValidator){
                Toast.makeText(this, "Please input a valid event name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!InputEventTicketPriceValidator){EventTicketPrice.setText("0");}
            if(!InputEventAudienceLimitValidator){EventAudienceLimit.setText("0");}

            String eventName = EventName.getText().toString();

            String eventDescription = EventDescription.getText().toString();
            if (eventDescription.isEmpty()) {
                eventDescription = "No description";
            }

            String eventVenue = EventVenue.getText().toString();

            double eventPrice;
            try {
                eventPrice = Double.parseDouble(EventTicketPrice.getText().toString());
            } catch (NumberFormatException e) {
                eventPrice = Integer.parseInt(EventTicketPrice.getText().toString());
            }

            LocalDateTime eventStart;
            String eventStartString = null;
            try {
                eventStart = LocalDateTime.parse(String.format("%s %s", EventDate.getText().toString(),
                                                                        EventStart.getText().toString()), dateTimeFormatter);
                eventStartString = eventStart.format(DateTimeFormatter.ISO_DATE_TIME);
            } catch (DateTimeException e) {
                eventStart = null;
            }

            LocalDateTime eventEnd;
            String eventEndString = null;
            try {
                eventEnd = LocalDateTime.parse(String.format("%s %s", EventDateEnd.getText().toString(),
                                                                      EventEnd.getText().toString()), dateTimeFormatter);
                eventEndString = eventEnd.format(DateTimeFormatter.ISO_DATE_TIME);
            } catch (DateTimeException e) {
                eventEnd = null;
            }

            int eventLimit = Integer.parseInt(EventAudienceLimit.getText().toString());
            if (!editMode) {
                EventServiceManager.getInstance().createEvent( eventName,
                                                               eventDescription,
                                                               eventVenue,
                                                               eventStartString,
                                                               eventEndString,
                                                               eventPrice,
                                                               eventLimit);
            } else {
                event.setName(eventName);
                event.setDescription(eventDescription);
                event.setVenue(eventVenue);
                event.setStartDate(eventStartString);
                event.setEndDate(eventEndString);
                event.setTicketPrice(eventPrice);
                event.setAudienceLimit(eventLimit);
            }

            finish();
        });
    }

    public void inputValidation(EditText text, String category){
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                return;
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                String errorMessage = "";
                boolean isValid = false;

                switch (category){
                    case "EventName":
                        isValid = InputValidator.isValidEventName(input);
                        errorMessage = "Invalid event input. Event name cannot be empty.";
                        InputEventNameValidator = isValid;
                        break;
                    case "EventPrice":
                        isValid = InputValidator.isValidEventTicketPrice(input);
                        errorMessage = "Invalid event price. Please input numbers only.";
                        InputEventTicketPriceValidator = isValid;
                        if(s.toString().trim().isEmpty()) InputEventTicketPriceValidator = false;
                        break;
                    case "EventLimit":
                        isValid = InputValidator.isValidEventAudienceLimit(input);
                        errorMessage = "Invalid event limit. Please input numbers only.";
                        InputEventAudienceLimitValidator = isValid;
                        if(s.toString().trim().isEmpty()) InputEventAudienceLimitValidator = false;
                        break;
                }

                if(!isValid){
                    text.setError(errorMessage);
                    return;
                } else {
                    text.setError(null);
                    return;
                }
            }
        });

    }

}