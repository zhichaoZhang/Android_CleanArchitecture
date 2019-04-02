package com.joye.cleanarchitecture.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.joye.cleanarchitecture.domain.utils.MyLog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * toast显示工具类
 * 连续显示多个Toast时，前面的Toast会被立即覆盖
 */
public class ToastUtil {
    @SuppressLint("StaticFieldLeak")
    private static TextView tvCustom;
    private static volatile Toast mToast;
    private static volatile boolean isReflectedHandler = false;

    /**
     * 显示toast,时间为LENGTH_LONG
     *
     * @param context 上下文
     * @param string  显示内容
     */
    @SuppressLint("ShowToast")
    public static void showLong(Context context, String string) {
        if (context == null) {
            return;
        }
        if (string == null) {
            return;
        }

        ensureToast(context);
        if (mToast == null) {
            MyLog.e("create Toast error: the context is %s.", context);
            return;
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setText(string);
        mToast.show();
    }

    /**
     * 显示toast,时间为LENGTH_SHORT
     *
     * @param context 上下文
     * @param string  显示内容
     */
    @SuppressLint("ShowToast")
    public static void showShort(Context context, String string) {
        if (context == null) {
            return;
        }
        if (string == null) {
            return;
        }
        ensureToast(context);
        if (mToast == null) {
            MyLog.e("create Toast error: the context is %s.", context);
            return;
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setText(string);
        mToast.show();
    }

    private static void setText(String msg) {
        if (msg == null) {
            return;
        }
        if (tvCustom != null) {
            tvCustom.setText(msg);
        } else {
            mToast.setText(msg);
        }
    }

    @SuppressLint("ShowToast")
    private static void ensureToast(Context context) {
        if (mToast != null) {
            return;
        }
        mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_LONG);
        //在7.x系统上，反射代理Toast中TN变量的Handler，捕获BadTokenException异常，异常出现原因：主线程阻塞，Toast显示时Token已过期
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.N && sdkInt < Build.VERSION_CODES.O && !isReflectedHandler) {
            reflectTNHandler(mToast);
            isReflectedHandler = true;
        }
    }

    private static void reflectTNHandler(Toast toast) {
        try {
            Field tNField = toast.getClass().getDeclaredField("mTN");
            if (tNField == null) {
                tNField = toast.getClass().getDeclaredField("TN");
            }
            if (tNField == null) {
                return;
            }
            tNField.setAccessible(true);
            Object TN = tNField.get(toast);
            if (TN == null) {
                return;
            }
            Field handlerField = TN.getClass().getDeclaredField("mHandler");
            if (handlerField == null) {
                return;
            }
            MyLog.d("TN class is %s.", TN.getClass());
            handlerField.setAccessible(true);
            handlerField.set(TN, new ProxyTNHandler(TN));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //Toast$TN持有的Handler变量
    private static class ProxyTNHandler extends Handler {
        private Object tnObject;
        private Method handleShowMethod;
        private Method handleHideMethod;

        ProxyTNHandler(Object tnObject) {
            this.tnObject = tnObject;
            try {
                this.handleShowMethod = tnObject.getClass().getDeclaredMethod("handleShow", IBinder.class);
                this.handleShowMethod.setAccessible(true);
                MyLog.d("handleShow method is %s", handleShowMethod);
                this.handleHideMethod = tnObject.getClass().getDeclaredMethod("handleHide");
                this.handleHideMethod.setAccessible(true);
                MyLog.d("handleHide method is %s", handleHideMethod);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    //SHOW
                    IBinder token = (IBinder) msg.obj;
                    MyLog.d("handleMessage(): token is %s", token);
                    if (handleShowMethod != null) {
                        try {
                            handleShowMethod.invoke(tnObject, token);
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (WindowManager.BadTokenException e) {
                            //显示Toast时添加BadTokenException异常捕获
                            e.printStackTrace();
                            MyLog.e(e, "show toast error.");
                        }
                    }
                    break;
                }

                case 1: {
                    //HIDE
                    MyLog.d("handleMessage(): hide");
                    if (handleHideMethod != null) {
                        try {
                            handleHideMethod.invoke(tnObject);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case 2: {
                    //CANCEL
                    MyLog.d("handleMessage(): cancel");
                    if (handleHideMethod != null) {
                        try {
                            handleHideMethod.invoke(tnObject);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }

            }
            super.handleMessage(msg);
        }
    }

    /**
     * 在UI线程运行弹出toast
     *
     * @param ctx  上下文
     * @param text 显示内容
     */
    public static void showToastOnUiThread(final Activity ctx, final String text) {
        if (ctx != null) {
            ctx.runOnUiThread(() -> showLong(ctx, text));
        }
    }

    /**
     * 自定义Toast样式
     *
     * @param context 上下文
     * @param view    Toast布局
     * @param tvId    显示内容的文件框空间id
     * @param gravity 对齐方式
     * @param xOffset x方向偏移量
     * @param yOffset y方向偏移量
     */
    public static void customStyle(Context context, View view, int tvId, int gravity, int xOffset, int yOffset) {
        ensureToast(context);
        if (view != null) {
            tvCustom = view.findViewById(tvId);
            mToast.setView(view);
        }
        mToast.setGravity(gravity, xOffset, yOffset);
    }
}