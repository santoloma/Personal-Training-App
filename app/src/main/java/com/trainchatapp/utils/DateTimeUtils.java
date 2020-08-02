package com.trainchatapp.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy/MM/dd-HH:mm");
    public static final SimpleDateFormat SDF_BASIC = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    public static final SimpleDateFormat SDF_TODAY = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat SDF_YESTERDAY = new SimpleDateFormat("'Yesterday', HH:mm");
    public static final SimpleDateFormat SDF_THIS_WEEK = new SimpleDateFormat("EEE, HH:mm");
    public static final SimpleDateFormat SDF_THIS_YEAR = new SimpleDateFormat("MMM/d, HH:mm");
    public static final SimpleDateFormat SDF_DIFFERENT_YEAR = new SimpleDateFormat("MMM/d/yyyy, HH:mm");

    public static String formatDate(long timestampLong) {
        SimpleDateFormat sdf = null;
        long timeNow = new Date().getTime();
        if (timestampLong > timeNow
        ) {
            return "";
        }

        String[] then = (SDF_BASIC.format(new Timestamp(timestampLong))).split("-");
        String[] now = (SDF_BASIC.format(new Timestamp(timeNow))).split("-");

        String year_then = then[0];
        String year_now = now[0];

        String month_then = then[1];
        String month_now = now[1];

        String day_then = then[2];
        String day_now = now[2];

        //String hour_then = then[3];
        //String hour_now = now[3];

        //String minute_then = then[4];
        //String minute_now = now[4];

        if (year_then.equals(year_now)) {
            if (month_then.equals(month_now)) {
                if (day_then.equals(day_now)) {
                    sdf = SDF_TODAY;
                } else {
                    int days_passed = Integer.parseInt(day_now) - Integer.parseInt(day_then);
                    if (days_passed == 1) {
                        sdf = SDF_YESTERDAY;
                    } else if (days_passed > 1 && days_passed < 8) {
                        sdf = SDF_THIS_WEEK;

                    } else {
                        sdf = SDF_THIS_YEAR;
                    }
                }
            } else {
                sdf = SDF_THIS_YEAR;
            }
        } else {
            sdf = SDF_DIFFERENT_YEAR;
        }
        return sdf.format(new Timestamp(timestampLong));
    }

    public static int[] timeRangeSplitter(String timeRange) {
        if (timeRange.matches("[0-9]{1,2}:\\[0-9]{1,2}-\\[0-9]{1,2}:[0-9]{1,2}")) {
            return new int[]{0, 0, 0, 0};
        } else {
            String[] fromTo = timeRange.split("-");
            String[] from = fromTo[0].split(":");
            String[] to = fromTo[1].split(":");
            return new int[]{Integer.parseInt(from[0]),
                    Integer.parseInt(to[0]),
                    Integer.parseInt(from[1]),
                    Integer.parseInt(to[1])};
        }
    }


    public static boolean isInThePast(String dateThen, String time) throws RuntimeException {
        String timeNowFormatted = SDF_DATE.format(new Date().getTime());  //

        String[] dateTimeNow = timeNowFormatted.split("-");
        String[] dateSplit = dateThen.split("/");
        if(dateSplit[1].length() == 1){
            dateSplit[1] = "0" + dateSplit[1];
        }
        if(dateSplit[0].length() == 1){
            dateSplit[0] = "0" + dateSplit[0];
        }
        dateThen = dateSplit[2] + "/" + dateSplit[1] + "/" + dateSplit[0];
        System.out.println(dateTimeNow[0] + " vs " + dateThen);
        System.out.println(dateTimeNow[1] + " vs " + time);

        boolean res = true;
        if (dateTimeNow[0].equals(dateThen)) {
            res = dateTimeNow[1].compareTo(time) > 0;
        } else {
            res = dateTimeNow[0].compareTo(dateThen) > 0;
        }
        System.out.println("In the past: " + res);
        return res;
    }
}
