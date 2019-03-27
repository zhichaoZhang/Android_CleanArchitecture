package com.joye.cleanarchitecture.domain.utils;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 日志打印工具类
 * <p>
 * Created by joye on 2018/7/26.
 */

public class MyLog {
    private static ILog mLogImpl;

    static {
        mLogImpl = new DefaultLogImpl();
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
        private static final Logger logger = Logger.getLogger("DefaultLogger");

        static {
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            if (handlers != null) {
                for (Handler handler : handlers) {
                    System.out.println("remove log handler: " + handler.getClass().getName());
                    rootLogger.removeHandler(handler);
                }
            }
            rootLogger.addHandler(new MyLoggerHandler());
            logger.setLevel(Level.ALL);
        }

        String format(String msg, Object... params) {
            return format(null, msg, params);
        }

        String format(Throwable throwable, String msg, Object... params) {
            String result = "";
            if (msg != null) {
                result = msg;
            }
            if (msg != null && params != null && params.length > 0) {
                result = String.format(msg, params);
            }
            if (throwable != null) {
                result = result + throwable.getMessage();
            }
            return result;
        }

        @Override
        public void v(String msg, Object... params) {
            logger.finest(format(msg, params));
        }

        @Override
        public void i(String msg, Object... params) {
            logger.info(format(msg, params));
        }

        @Override
        public void d(String msg, Object... params) {
            logger.fine(format(msg, params));
        }

        @Override
        public void w(String msg, Object... params) {
            logger.warning(format(msg, params));
        }

        @Override
        public void e(Throwable throwable, String msg, Object... params) {
            logger.severe(format(throwable, msg, params));
        }

        @Override
        public void e(String msg, Object... params) {
            logger.severe(format(msg, params));
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
