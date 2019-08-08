package com.example.application1.Class;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CancellationException;

public class DateUtils {
    public long getCurrentTimestamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong;
    }

    public String getCurrentDateTimestamp() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd");
        String formatted = df.format(date);
        return formatted;
    }

    public String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(date);
        return formattedDate;
    }

    public String getCurrentTime() {
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss aa");
        String formattedDate = df.format(date);

        return formattedDate;
    }
}
