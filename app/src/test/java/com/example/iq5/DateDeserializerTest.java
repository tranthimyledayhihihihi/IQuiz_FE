package com.example.iq5;

import com.example.iq5.utils.DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Test cho DateDeserializer
 */
public class DateDeserializerTest {

    @Test
    public void testDateDeserializerWithMicroseconds() {
        // Tạo Gson với custom deserializer
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        // Test với định dạng có microsecond như server trả về
        String jsonWithMicroseconds = "\"2025-12-17T19:29:31.7089785\"";
        
        try {
            Date result = gson.fromJson(jsonWithMicroseconds, Date.class);
            assertNotNull("Date should not be null", result);
            System.out.println("✅ Successfully parsed date with microseconds: " + result);
        } catch (Exception e) {
            fail("Should be able to parse date with microseconds: " + e.getMessage());
        }
    }

    @Test
    public void testDateDeserializerWithMilliseconds() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        // Test với định dạng millisecond thông thường
        String jsonWithMilliseconds = "\"2025-12-17T19:29:31.708\"";
        
        try {
            Date result = gson.fromJson(jsonWithMilliseconds, Date.class);
            assertNotNull("Date should not be null", result);
            System.out.println("✅ Successfully parsed date with milliseconds: " + result);
        } catch (Exception e) {
            fail("Should be able to parse date with milliseconds: " + e.getMessage());
        }
    }

    @Test
    public void testDateDeserializerWithoutFraction() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        // Test với định dạng không có phần giây lẻ
        String jsonWithoutFraction = "\"2025-12-17T19:29:31\"";
        
        try {
            Date result = gson.fromJson(jsonWithoutFraction, Date.class);
            assertNotNull("Date should not be null", result);
            System.out.println("✅ Successfully parsed date without fraction: " + result);
        } catch (Exception e) {
            fail("Should be able to parse date without fraction: " + e.getMessage());
        }
    }
}