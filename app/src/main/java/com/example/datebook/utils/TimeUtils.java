package com.example.datebook.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public final static long hourInMill = 1000 * 60 * 60;

    public static String getHoursString(long millis) {
        return String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }
}
