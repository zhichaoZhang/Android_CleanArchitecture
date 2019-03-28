package com.joye.cleanarchitecture.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * TextView基类
 *
 * Created by joye on 2017/12/30.
 */

public class BaseTextView extends AppCompatTextView {
    public BaseTextView(Context context) {
        super(context);
    }

    public BaseTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
