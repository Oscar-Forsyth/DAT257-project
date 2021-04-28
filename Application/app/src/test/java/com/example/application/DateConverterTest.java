package com.example.application;

import com.example.application.calendar.DateConverter;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateConverterTest {

    @Test
    public void convertDateFromCalendarWithTime() {
        assertEquals("Actual output: " + DateConverter.convertDateFromCalendar("2019-11-20T17:00:00+01:00"), "20 November 2019 17:00", DateConverter.convertDateFromCalendar("2019-11-20T17:00:00+01:00"));
    }

    @Test
    public void convertDateFromCalendarWithTime2() {
        assertEquals("Actual output: " + DateConverter.convertDateFromCalendar("2019-01-20T17:00:00+01:00"), "20 January 2019 17:00", DateConverter.convertDateFromCalendar("2019-01-20T17:00:00+01:00"));
    }

    @Test
    public void convertDateFromCalendarWithoutTime() {
        assertEquals("Actual output: " + DateConverter.convertDateFromCalendar("2019-11-20"), "5 November 2019", DateConverter.convertDateFromCalendar("2019-11-05"));
    }
}