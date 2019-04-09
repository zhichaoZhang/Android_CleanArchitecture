package com.joye.cleanarchitecture.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * 官方下拉刷新组件基类
 */
public class BaseSwipeRefreshLayout extends SwipeRefreshLayout {

    public BaseSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public BaseSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
