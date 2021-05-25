package com.example.application;

import com.example.application.calendar.EventsJSONConverter;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JSONTest {
    public static EventsJSONConverter eventsJSONConverter;
    private final DateFormat JSONFormat = new SimpleDateFormat("yyyy-MM-dd");


    @BeforeClass
    public static void beforeClass(){
        eventsJSONConverter = new EventsJSONConverter();
    }
    @Test
    public void AddDaysToDateTest() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, ParseException {

        Date date = JSONFormat.parse("1999-08-20");

        Method method = EventsJSONConverter.class.getDeclaredMethod("addDaysToDate",Date.class,Integer.class);
        method.setAccessible(true);
        Date output = (Date) method.invoke(eventsJSONConverter,date,1);

        assertEquals(output.toString(),"Sat Aug 21 00:00:00 CEST 1999");
    }
}