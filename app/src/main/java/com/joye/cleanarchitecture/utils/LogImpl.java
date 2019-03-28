package com.joye.cleanarchitecture.utils;

import android.util.Log;

import com.joye.cleanarchitecture.domain.utils.MyLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志实现
 */
public class LogImpl {

    public static MyLog.ILog getInstance(boolean debug) {
        return debug ? debugLogImpl : releaseLogImpl;
    }

    /**
     * 调试日志输出
     */
    private static MyLog.ILog debugLogImpl = new MyLog.ILog() {
        static final int MAX_LOG_LENGTH = 4000;
        private static final int CALL_STACK_INDEX = 4;
        private final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

        /** Return whether a message at {@code priority} should be logged. */
        boolean isLoggable(int priority) {
            return true;
        }

        private void prepareLog(int priority, Throwable t, String message, Object... args) {
            if (!isLoggable(priority)) {
                return;
            }
            if (message != null && message.length() == 0) {
                message = null;
            }
            if (message == null) {
                if (t == null) {
                    return; // Swallow message if it's null and there's no throwable.
                }
                message = Log.getStackTraceString(t);
            } else {
                if (args.length > 0) {
                    message = String.format(message, args);
                }
                if (t != null) {
                    message += "\n" + Log.getStackTraceString(t);
                }
            }

            log(priority, getTag(), message, t);
        }

        String createStackElementTag(StackTraceElement element) {
            String tag = element.getClassName();
            Matcher m = ANONYMOUS_CLASS.matcher(tag);
            if (m.find()) {
                tag = m.replaceAll("");
            }
            return tag.substring(tag.lastIndexOf('.') + 1);
        }

        String getTag() {

            // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
            // because Robolectric runs them on the JVM but on Android the elements are different.
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            if (stackTrace.length <= CALL_STACK_INDEX) {
                throw new IllegalStateException(
                        "Synthetic stacktrace didn't have enough elements: are you using proguard?");
            }
            return createStackElementTag(stackTrace[CALL_STACK_INDEX]);
        }


        void log(int priority, String tag, String message, Throwable t) {
            if (tag == null || "".equals(tag)) {
                tag = getTag();
            }
            realLog(priority, tag, message, t);
        }


        void realLog(int priority, String tag, String message, Throwable t) {
            if (message.length() < MAX_LOG_LENGTH) {
                if (priority == android.util.Log.ASSERT) {
                    android.util.Log.wtf(tag, message);
                } else {
                    android.util.Log.println(priority, tag, message);
                }
                return;
            }

            // Split by line, then ensure each line can fit into Log's maximum length.
            for (int i = 0, length = message.length(); i < length; i++) {
                int newline = message.indexOf('\n', i);
                newline = newline != -1 ? newline : length;
                do {
                    int end = Math.min(newline, i + MAX_LOG_LENGTH);
                    String part = message.substring(i, end);
                    if (priority == android.util.Log.ASSERT) {
                        android.util.Log.wtf(tag, part);
                    } else {
                        android.util.Log.println(priority, tag, part);
                    }
                    i = end;
                } while (i < newline);
            }
        }

        @Override
        public void v(String msg, Object... params) {
            prepareLog(Log.VERBOSE, null, msg, params);
        }

        @Override
        public void i(String msg, Object... params) {
            prepareLog(Log.INFO, null, msg, params);
        }

        @Override
        public void d(String msg, Object... params) {
            prepareLog(Log.DEBUG, null, msg, params);
        }

        @Override
        public void w(String msg, Object... params) {
            prepareLog(Log.WARN, null, msg, params);
        }

        @Override
        public void e(Throwable throwable, String msg, Object... params) {
            prepareLog(Log.ERROR, throwable, msg, params);
        }

        @Override
        public void e(String msg, Object... params) {
            prepareLog(Log.ERROR, null, msg, params);
        }
    };

    /**
     * 正式日志输出
     */
    // TODO: 2019/3/21 正式日志输出实现
    private static MyLog.ILog releaseLogImpl = new MyLog.ILog() {
        @Override
        public void v(String msg, Object... params) {

        }

        @Override
        public void i(String msg, Object... params) {

        }

        @Override
        public void d(String msg, Object... params) {

        }

        @Override
        public void w(String msg, Object... params) {

        }

        @Override
        public void e(Throwable throwable, String msg, Object... params) {

        }

        @Override
        public void e(String msg, Object... params) {

        }
    };

}
