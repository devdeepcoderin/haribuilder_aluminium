package com.deepcoder.hbalum.utils.logger;

import android.util.Log;

import com.deepcoder.hbalum.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CustomLog {


    /**
     * Method to print verbose information
     * @param tag key
     * @param message value to be printed
     */
    @SuppressWarnings("unused")
    public static void verbose(final String tag, final Object message) {
        if (BuildConfig.DEBUG)
            Log.v(tag, message.toString());
    }

    /**
     * Method to print warning information
     * @param tag key
     * @param message value to be printed
     */@SuppressWarnings("unused")

    public static void warning(final String tag, final String message) {
        if (BuildConfig.DEBUG)
            Log.w(tag, message);
    }

    /**
     * Method to print debug information
     * @param tag key
     * @param message value to be printed
     */
    @SuppressWarnings("unused")
    public static void debug(final String tag, final Object message) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message.toString());
    }

    /**
     * Method to print info information
     * @param tag key
     * @param message value to be printed
     */
    @SuppressWarnings("unused")
    public static void info(final String tag, final String message) {
        if (BuildConfig.DEBUG)
            Log.i(tag, message);
    }

    /**
     * Method to print error information
     * @param tag key
     * @param message value to be printed
     */
    @SuppressWarnings("unused")
    public static void error(final String tag, final String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);

        }
    }


    /**
     * Method to print error  information with throwable
     * @param tag key
     * @param message value to be printed
     * @param throwable exception throwable
     */
    @SuppressWarnings("unused")
    public static void error(final String tag, final String message, final Throwable throwable) {
        if (BuildConfig.DEBUG)
            Log.e(tag, message, throwable);
    }
    /**
     * Method to print error information
     * @param tag key
     * @param exception value to be printed
     */
    @SuppressWarnings("unused")
    public static void errorException(final String tag, final Exception exception) {
        if (BuildConfig.DEBUG) {
            StringWriter sw = new StringWriter();
            exception.printStackTrace(new PrintWriter(sw));
            Log.e(tag, sw.toString());
        }
    }

    /**
     * Method to print error information
     * @param tag key
     * @param throwable value to be printed
     */
    @SuppressWarnings("unused")
    public static void errorThrowable(final String tag, final Throwable throwable) {
        if (BuildConfig.DEBUG) {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            Log.e(tag, sw.toString());
        }
    }
}
