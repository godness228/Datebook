package com.example.datebook;

import java.util.concurrent.TimeUnit;

public class Convert {
    public final static long hourInMill = 1000 * 60 * 60;

    public static String convertMilToString(long millis) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }
}
