package com.deepcoder.hbalum.utils;

import com.deepcoder.hbalum.utils.logger.CustomLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    private static final String TAG = TimeUtils.class.getSimpleName();


    /**
     * Method to get time in timestamp
     *
     * @param updateDate date in string format
     * @return date format as a string
     */
    public static long getDateInLong(String updateDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(updateDate);
            return date.getTime();
        } catch (Exception ex) {
            CustomLog.errorException(TAG, ex);
            return 0;
        }
    }

    /**
     * Method to get date in formate dd/mm/yyyy
     *
     * @return date format as a string
     */
    public static Date getDateInFormatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        String dateTime = sdf.format(date);
        try {
            return sdf.parse(dateTime);
        } catch (Exception ex) {
            CustomLog.errorException(TAG, ex);
            return null;
        }
    }

    /**
     * Method to get date in formate dd/mm/yyyy
     *
     * @param time time in milliseconds
     * @return date format as a string
     */
    public static Date getDateFromMilliseconds(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date(time);
        String dateTime = sdf.format(date);
        try {
            return sdf.parse(dateTime);
        } catch (Exception ex) {
            CustomLog.errorException(TAG, ex);
            return null;
        }
    }

    /**
     * Method to get date in formate dd/mm/yyyy
     *
     * @param time time in milliseconds
     * @return date format as a string
     */
    public static String getTimeFromMilliseconds(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date(time);
        String dateTime = sdf.format(date);
        try {
            return dateTime;
        } catch (Exception ex) {
            CustomLog.errorException(TAG, ex);
            return null;
        }
    }

    /**
     * Method to get first day of week
     *
     * @return date format as a string
     */
    public static Date getFirstDayOfWeek() {
        Calendar c1 = Calendar.getInstance();
        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, 1);
        return getDateFromMilliseconds(c1.getTimeInMillis());
    }

    /**
     * Method to get last day of week
     *
     * @return date format as a string
     */
    public static Date getLastDayOfWeek() {
        Calendar c1 = Calendar.getInstance();
        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);
        return TimeUtils.getDateFromMilliseconds(c1.getTimeInMillis());
    }

    /**
     * Method to get first day of month
     *
     * @return date format as a string
     */
    public static Date getFirstDayOfMonth() {
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        //setTimeToBeginningOfDay(calendar);
        return TimeUtils.getDateFromMilliseconds(calendar.getTimeInMillis());
    }

    /**
     * Method to get last day of month
     *
     * @return date format as a string
     */
    public static Date getLastDayOfMonth() {
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        //setTimeToBeginningOfDay(calendar);
        return TimeUtils.getDateFromMilliseconds(calendar.getTimeInMillis());
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    /**
     * Method to get date from timestamp.
     *
     * @param time time in timestamp (long)
     * @return returns date in string in dd MMMM yyyy format
     */
    public static String getDateInDayFormat(long time) {
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            date = sdf.format(new Date(time));
            return date;
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
        return date;
    }

    /**
     * Method to get date from timestamp.
     *
     * @param time time in timestamp (long)
     * @return returns date in string in dd MMMM yyyy format
     */
    public static String getDayFromDate(long time) {
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
            date = sdf.format(new Date(time));
            return date;
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
        return date;
    }

    /**
     * Method to get date from timestamp.
     *
     * @param time time in timestamp (long)
     * @return returns date in string in dd MMMM yyyy format
     */
    public static String getDateInMonthFormat(long time) {
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.getDefault());
            date = sdf.format(new Date(time));
            return date;
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
        return date;
    }

    /**
     * Method to get date in dd/MM/yyyy HH:mm format
     *
     * @param time timestamp in long datatype
     * @return date format as a string
     */
    @SuppressWarnings("unused")
    public static String getDateIn24HrsFormatInUTC(long time) {
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = sdf.format(new Date(time));
            return date;
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
        return date;
    }

    /*public static String getDateInInUTC(long time) {
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
            date = sdf.format(new Date(time));
            return date;
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
        return date;
    }*/

    /**
     * Method to get time from current time.
     * This method will check Months, Weeks, days, Weeks, Days,Hours, Minutes, Sec different
     *
     * @param timeStamp end date
     * @return time different in string
     */
    @SuppressWarnings("unused")
    public static String getDateTimeDifference(long timeStamp) {


        Date currentDate = new Date();
        long different = currentDate.getTime() - timeStamp;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weeksInMilli = daysInMilli * 7;
        long monthsInMilli = weeksInMilli * 4;

        long elapsedMonths = different / monthsInMilli;
        different = different % monthsInMilli;

        long elapsedWeeks = different / weeksInMilli;
        different = different % weeksInMilli;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String time = null;

        if (elapsedMonths > 0) {
            time = formatTime(elapsedMonths, elapsedWeeks, " month", " week");
        } else {
            if (elapsedWeeks > 0) {
                time = formatTime(elapsedWeeks, elapsedDays, " week", " day");
            } else {
                if (elapsedDays > 0) {
                    time = formatTime(elapsedDays, elapsedHours, " day", " hour");
                } else {
                    if (elapsedHours > 0) {
                        time = formatTime(elapsedHours, elapsedMinutes, " hour", " min");
                    } else {
                        if (elapsedMinutes > 0) {
                            if (elapsedMinutes == 1) {
                                time = elapsedMinutes + " min ago";
                            } else {
                                time = elapsedMinutes + " mins ago";
                            }
                        } else {
                            if (elapsedSeconds > 0) {
                                time = elapsedSeconds + " secs ago";
                            }
                        }
                    }
                }
            }

        }
        if (time == null) {
            time = "0";
        }
        return time;
    }

    /**
     * Common method for checking single or multiple values in string and concat string
     *
     * @param checkVar   Main params
     * @param CompareVar sub params
     * @param mainString Main string
     * @param subString  Sub String
     * @return String with whole text which will be displayed.
     */

    private static String formatTime(long checkVar, long CompareVar, String mainString, String subString) {

        String time;

        if (checkVar == 1) {
            if (CompareVar > 0) {
                time = checkVar + mainString + CompareVar + subString + " ago";
            } else {
                time = checkVar + subString + "s " + " ago";
            }
        } else {
            if (CompareVar > 0) {
                time = checkVar + mainString + "s " + CompareVar + subString + "s " + " ago";
            } else {
                time = checkVar + subString + "s " + " ago";
            }
        }
        return time;
    }

    public static String getDateInddmmyyFormat(String updateDate) {
        String date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        date = sdf.format(new Date(updateDate));
        return date;
    }


}
