package com.joye.cleanarchitecture.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 按钮基类
 */
public class BaseButton extends Button {
    public BaseButton(Context context) {
        super(context);
    }

    public BaseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
