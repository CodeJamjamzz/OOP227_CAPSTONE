package com.example.capstone_project.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class InputValidator {

    // valid name checker
    public static boolean isValidName(String InputedName) {
        if (InputedName == null || InputedName.trim().isEmpty()) {
            return false;
        }
        if (InputedName.length() < 2 || InputedName.length() > 50) {
            return false;
        }
        // allows letters, spaces, apostrophes, and hyphens
        return InputedName.matches("^[a-zA-Z\\s'-,.]+$");
    }

    // valid student number checker
    public static boolean isValidStudentNumber(String InputedStudentNumber) {
        if (InputedStudentNumber == null || InputedStudentNumber.trim().isEmpty()) {
            return false;
        }
        // allows xx-xxxx-xxx format (numbers)
        return InputedStudentNumber.matches("^\\d{2}-\\d{4}-\\d{3}$");
    }

    // valid email checker
    public static boolean isValidEmail(String InputedEmail) {
        if (InputedEmail == null || InputedEmail.trim().isEmpty()) {
            return false; // empty or null
        }
        // allows email format (letters + @ + .)
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return InputedEmail.matches(emailPattern);
    }

    // valid course checker (BSCS, BSN, BSMBA, etc.)
    public static boolean isValidCourse(String InputedCourseYear) {
        if (InputedCourseYear == null || InputedCourseYear.trim().isEmpty()) {
            return false; // empty or null
        }
        // allows minimum of 2 letters + 1 number format
        return InputedCourseYear.matches("^[A-Z]{2,} - [1-4]$");
    }

    // valid Ticket Price checker
    public static boolean isValidEventTicketPrice(String InputedTicketPrice){
        // Already initalized to 0 even the input tag is empty as this is not required.
        return InputedTicketPrice.matches("^$|^\\d+$");
    }

    // valid Audience Limit checker
    public static boolean isValidEventAudienceLimit(String InputedAudienceLimit){
        return InputedAudienceLimit.matches("^$|^\\d+$");
    }

    public static boolean isValidEventName(String InputedEventName){
        return InputedEventName != null && !InputedEventName.trim().isEmpty();
    }

    public static boolean isValidDate(String input, String end){
        if(input.trim().isEmpty()){
            return true;
        }

        String res = DateValidFormat(input);

        boolean result = true;
        LocalDate current = LocalDate.now(ZoneId.of("Asia/Manila"));
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate inputtedDate = LocalDate.parse(res, format);

        if(!end.trim().isEmpty()){
            end = DateValidFormat(end);
            LocalDate inputtedendDate = LocalDate.parse(end, format);
            result = !inputtedDate.isBefore(current) && inputtedDate.isBefore(inputtedendDate);
            return result;
        }

        result = !inputtedDate.isBefore(current);
        return result;
    }

    public static boolean isValidDateEnd(String input, String start){
        if(input.trim().isEmpty()){
            return true;
        }

        String res = DateValidFormat(input);

        boolean result = true;
        LocalDate current = LocalDate.now(ZoneId.of("Asia/Manila"));
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate inputtedDate = LocalDate.parse(res, format);

        if(!start.trim().isEmpty()){
            start = DateValidFormat(start);
            LocalDate inputtedstartDate = LocalDate.parse(start, format);
            result = !inputtedDate.isBefore(current) && !inputtedDate.isBefore(inputtedstartDate);
            return result;
        }

        result = !inputtedDate.isBefore(current);
        return result;
    }

    // Helper function since LocalDate.parse cannot parse a singe digit (e.g. 1,2,3) it must be paired with 0 at first
    public static String DateValidFormat(String str){
        String[] parts = str.split("/");
        String res = str;
        if(Integer.parseInt(parts[1]) < 10){res = parts[0] + "/" + '0' + parts[1] + "/" + parts[2];}
        if(Integer.parseInt(parts[0]) < 10){res = "0" + res;}
        return res;
    }
}
