package com.example.application;

public class DateConverter {

    /**
     *
     * @param date A date as a string formatted like "2019-11-20T17:00:00+01:00" or
     *             "2020-02-22"
     */
    public static String convertDateFromCalendar(String date) {
        StringBuilder sb = new StringBuilder();
        sb.append(getDay(date));
        sb.append(" ");
        sb.append(getMonth(date));
        sb.append(" ");
        sb.append(getYear(date));
        if (date.contains("T")) {
            sb.append(" ");
            sb.append(getTime(date));
        }
        return sb.toString();
    }

    private static String getTime(String date) {
        return date.substring(11,16);
    }

    private static String getYear(String date) {
        return date.substring(0, 4);
    }

    private static String getDay(String date) {
        if (date.charAt(8) == '0') {
            return date.substring(9, 10);
        }
        return date.substring(8, 10);
    }

    private static String getMonth(String date) {
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
