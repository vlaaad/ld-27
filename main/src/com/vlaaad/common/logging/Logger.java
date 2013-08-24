package com.vlaaad.common.logging;

import com.badlogic.gdx.Gdx;

/**
 * Created 14.06.13 by vlaaad
 */
public class Logger {

    private static final String TAG = "com.vlaaad.tenseconds";

    /**
     * Logs a message to the console or logcat
     */
    public static void log(Object message) {
        Gdx.app.log(TAG, String.valueOf(message));
    }

    /**
     * Logs a message to the console or logcat
     */
    public static void log(Object message, Exception exception) {
        Gdx.app.log(TAG, String.valueOf(message), exception);
    }

    /**
     * Logs an error message to the console or logcat
     */
    public static void error(Object message) {
        Gdx.app.error(TAG, String.valueOf(message));
    }

    /**
     * Logs an error message to the console or logcat
     */
    public static void error(Object message, Throwable exception) {
        Gdx.app.error(TAG, String.valueOf(message), exception);
    }

    /**
     * Logs a debug message to the console or logcat
     */
    public static void debug(Object message) {
        Gdx.app.debug(TAG, String.valueOf(message));
    }

    /**
     * Logs a debug message to the console or logcat
     */
    public static void debug(Object message, Throwable exception) {
        Gdx.app.debug(TAG, String.valueOf(message), exception);
    }
}
