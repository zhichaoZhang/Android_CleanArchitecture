package com.joye.cleanarchitecture.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
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
