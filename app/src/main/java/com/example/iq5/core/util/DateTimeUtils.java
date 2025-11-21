package com.example.iq5.core.util;

import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";

    public static String formatDate(@Nullable Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(date);
    }

    public static String formatDateTimeForDisplay(@Nullable Date dateTime) {
        if (dateTime == null) return "";
        return new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault()).format(dateTime);
    }
}