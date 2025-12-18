package com.example.iq5.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Custom deserializer để xử lý các định dạng ngày tháng từ server
 * Hỗ trợ định dạng ISO 8601 với microsecond
 */
public class DateDeserializer implements JsonDeserializer<Date> {
    
    // Các định dạng ngày tháng có thể từ server
    private static final String[] DATE_FORMATS = {
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS",  // Với microsecond (7 chữ số)
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",   // Với microsecond (6 chữ số)
        "yyyy-MM-dd'T'HH:mm:ss.SSSSS",    // Với microsecond (5 chữ số)
        "yyyy-MM-dd'T'HH:mm:ss.SSSS",     // Với millisecond (4 chữ số)
        "yyyy-MM-dd'T'HH:mm:ss.SSS",      // Với millisecond (3 chữ số)
        "yyyy-MM-dd'T'HH:mm:ss",          // Không có phần giây lẻ
        "yyyy-MM-dd HH:mm:ss",            // Định dạng thông thường
        "yyyy-MM-dd"                      // Chỉ có ngày
    };

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        
        if (json == null || json.isJsonNull()) {
            return null;
        }
        
        String dateString = json.getAsString();
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        
        // Thử parse với từng định dạng
        for (String format : DATE_FORMATS) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                return sdf.parse(dateString);
            } catch (ParseException e) {
                // Tiếp tục thử định dạng tiếp theo
            }
        }
        
        // Nếu không parse được với bất kỳ định dạng nào
        throw new JsonParseException("Unable to parse date: " + dateString);
    }
}