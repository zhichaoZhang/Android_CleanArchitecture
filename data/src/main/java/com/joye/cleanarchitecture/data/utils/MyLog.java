package com.joye.cleanarchitecture.data.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * 日志打印工具类
 *
 * Created by joye on 2018/7/26.
 */

public class MyLog {
    private static ILog mLogImpl;

    static {
        mLogImpl = new DefaultLogImpl();
        if (!isAndroidEmv()) {
            //非Android环境使用System.out输出
            Timber.plant(new BaseTimberTree() {
                @Override
                protected void realLog(int priority, String tag, String message, Throwable t) {
                    System.out.println(tag + ":" + message);
                }
            });
        } else {
            //默认使用的日志树
            Timber.plant(new BaseTimberTree() {
                @Override
                protected void realLog(int priority, String tag, String message, Throwable t) {
                    if (message.length() < MAX_LOG_LENGTH) {
                        if (priority == Log.ASSERT) {
                            Log.wtf(tag, message);
                        } else {
                            Log.println(priority, tag, message);
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
                            if (priority == Log.ASSERT) {
                                Log.wtf(tag, part);
                            } else {
                                Log.println(priority, tag, part);
                            }
                            i = end;
                        } while (i < newline);
                    }
                }
            });
        }
    }

    /**
     * 设置是否处于Debug模式
     * 在非Debug模式下，会记录日志输出并上传到服务端
     *
     * @param isDebug 是否Debug
     * @param context 上下文
     */
    public static void setDebug(boolean isDebug, Context context) {
        if (!isDebug) {
            //非调试模式下，移除所有日志树，并添加线上日志树
            Timber.uprootAll();
            Timber.plant(new BaseTimberTree() {
                @Override
                protected void realLog(int priority, String tag, String message, Throwable t) {
                    Log.wtf(tag, message, t);
                }
            });
        }
    }

    /**
     * 设置Log输出实现
     *
     * @param log 自定义Log输入，实现ILog接口
     */
    public static void setLogImpl(ILog log) {
        if (log != null) {
            mLogImpl = log;
        }
    }

    //是否是Android环境
    private static boolean isAndroidEmv() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
        return false;
    }

    private abstract static class BaseTimberTree extends Timber.Tree {
        static final int MAX_LOG_LENGTH = 4000;
        private static final int CALL_STACK_INDEX = 8;
        private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

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


        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (tag == null || "".equals(tag)) {
                tag = getTag();
            }
            realLog(priority, tag, message, t);
        }

        protected abstract void realLog(int priority, String tag, String message, Throwable t);
    }

    /**
     * Log接口定义
     */
    public interface ILog {
        void v(String msg, Object... params);

        void i(String msg, Object... params);

        void d(String msg, Object... params);

        void w(String msg, Object... params);

        void e(Throwable throwable, String msg, Object... params);

        void e(String msg, Object... params);
    }

    /**
     * 默认Log实现
     */
    static class DefaultLogImpl implements ILog {

        @Override
        public void v(String msg, Object... params) {
            Timber.v(msg, params);
        }

        @Override
        public void i(String msg, Object... params) {
            Timber.i(msg, params);
        }

        @Override
        public void d(String msg, Object... params) {
            Timber.i(msg, params);
        }

        @Override
        public void w(String msg, Object... params) {
            Timber.w(msg, params);
        }

        @Override
        public void e(Throwable throwable, String msg, Object... params) {
            Timber.e(throwable, msg, params);
        }

        @Override
        public void e(String msg, Object... params) {
            Timber.e(msg, params);
        }
    }

    public static void v(String msg, Object... objects) {
        mLogImpl.v(msg, objects);
    }

    public static void i(String msg, Object... objects) {
        mLogImpl.i(msg, objects);
    }

    public static void d(String msg, Object... objects) {
        mLogImpl.d(msg, objects);
    }

    public static void w(String msg, Object... objects) {
        mLogImpl.w(msg, objects);
    }

    public static void e(String msg, Object... objects) {
        mLogImpl.e(msg, objects);
    }

    public static void e(Throwable throwable, String msg, Object... objects) {
        mLogImpl.e(throwable, msg, objects);
    }
}
