package com.joye.cleanarchitecture.domain.utils;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MyLoggerHandler extends Handler {

    MyLoggerHandler() {
        super();
    }

    @Override
    public void publish(LogRecord record) {
        if (!super.isLoggable(record))
            return;

        String name = record.getLoggerName();
        int maxLength = 30;
        String tag = name.length() > maxLength ? name.substring(name.length() - maxLength) : name;

        try {
            Level level = record.getLevel();
            System.out.println(level.getName() + "/[" + tag + "]" + record.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
