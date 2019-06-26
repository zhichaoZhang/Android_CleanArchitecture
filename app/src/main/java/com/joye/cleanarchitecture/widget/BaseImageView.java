package com.joye.cleanarchitecture.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @Description: ImageView基类
 * @Author: joye
 * @CreateDate: 2019-06-13 23:10
 * @ProjectName: Android_CleanArchitecture
 * @Package: com.joye.cleanarchitecture.widget
 * @ClassName: BaseImageView
 */
public class BaseImageView extends AppCompatImageView {

    public BaseImageView(Context context) {
        super(context);
    }

    public BaseImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
