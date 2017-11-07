package ru.mail.park.tpschedule.utils;

import java.util.Locale;

/**
 * Created by ed on 07.11.17.
 */

public class ErrorMessage {
    private static final String messageFormat = "error: %s, line %d";

    private String message;
    private int lineNumber;

    public ErrorMessage(Throwable error) {
        this.message = error.getMessage();
        this.lineNumber = error.getStackTrace()[2].getLineNumber();
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, messageFormat, message, lineNumber);
    }
}
