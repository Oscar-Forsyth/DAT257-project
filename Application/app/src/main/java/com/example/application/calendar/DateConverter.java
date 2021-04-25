package com.example.application.calendar;

/**
 * Class that converts the date from CIS Google Calendars JSON-file to a more readable format.
 */
public class DateConverter {

    /**
     * Method that converts the original string to a more readable format.
     * @param date A date as a string formatted like "2019-11-20T17:00:00+01:00" or
     *             "2020-02-22"
     * @return A string in the format "20 November 2019 17:00" or "22 February 2020"
     */
    public static String convertDateFromCalendar(String date) {
        StringBuilder sb = new StringBuilder();
        sb.append(getDay(date));
        sb.append(" ");
        sb.append(getMonth(date));
        sb.append(" ");
        sb.append(getYear(date));
        // Checks if date string has information about time
        if (date.contains("T")) {
            sb.append(" ");
            sb.append(getTime(date));
        }
        return sb.toString();
    }

    /**
     * Method retrieves time from original datetime string.
     * @param date A date as a string formatted like "2019-11-20T17:00:00+01:00"
     * @return A string, which represents time, in the format "17:00"
     */
    private static String getTime(String date) {
        return date.substring(11,16);
    }

    /**
     * Method retrieves year from original datetime string.
     * @param date A date as a string formatted like "2019-11-20T17:00:00+01:00"
     * @return A string, which represents year, in the format "2019"
     */
    private static String getYear(String date) {
        return date.substring(0, 4);
    }

    /**
     * Method retrieves day from original datetime string.
     * @param date A date as a string formatted like "2019-11-20T17:00:00+01:00"
     * @return A string, which represents day in month, in the format "20"
     */
    private static String getDay(String date) {
        // Make sure the 0 is not displayed if it's one of the first nine days in a month.
        if (date.charAt(8) == '0') {
            return date.substring(9, 10);
        }
        return date.substring(8, 10);
    }

    /**
     * Method retrieves month from original datetime string.
     * @param date A date as a string formatted like "2019-11-20T17:00:00+01:00"
     * @return A string, which represents month in text, in the format "November"
     */
    private static String getMonth(String date) {
        // The month's number is converted to the name of month
        switch (date.substring(5, 7)) {
            case "01":
                return "January";
            case "02":
                return "February";
            case "03":
                return "Mars";
            case "04":
                return "April";
            case "05":
                return "May";
            case "06":
                return "June";
            case "07":
                return "July";
            case "08":
                return "August";
            case "09":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            default:
                return "Month";
        }
    }
}
