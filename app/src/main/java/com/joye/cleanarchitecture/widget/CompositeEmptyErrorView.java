package com.joye.cleanarchitecture.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.joye.cleanarchitecture.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 空页面和错误页面的组合页面
 */
public class CompositeEmptyErrorView extends FrameLayout {
    private Context mCxt;

    /**
     * 错误页面
     */
    private ConstraintLayout clError;
    /**
     * 错误提示文本框
     */
    private TextView tvError;
    /**
     * 错误提示图片
     */
    private ImageView ivError;
    /**
     * 空页面
     */
    private ConstraintLayout clEmpty;
    /**
     * 空白提示文本框
     */
    private TextView tvEmpty;
    /**
     * 空白提示图片
     */
    private ImageView ivEmpty;

    public CompositeEmptyErrorView(@NonNull Context context) {
        this(context, null);
    }

    public CompositeEmptyErrorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CompositeEmptyErrorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CompositeEmptyErrorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.mCxt = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_empty_error, this, true);
        clEmpty = view.findViewById(R.id.cl_empty);
        clError = view.findViewById(R.id.cl_error);
        tvEmpty = view.findViewById(R.id.tv_empty);
        ivEmpty = view.findViewById(R.id.iv_empty);
        tvError = view.findViewById(R.id.tv_error);
        ivError = view.findViewById(R.id.iv_error);
    }


    public void setEmptyViewVisible(boolean emptyViewVisible) {
        setEmptyViewVisible(emptyViewVisible, mCxt.getString(R.string.empty_tip_placeholder));
    }

    public void setEmptyViewVisible(boolean emptyViewVisible, String emptyTip) {
        setEmptyViewVisible(emptyViewVisible, emptyTip, R.drawable.ic_empty);
    }

    public void setEmptyViewVisible(boolean emptyViewVisible, String emptyTip, @DrawableRes int emptyImageRes) {
        if (clEmpty != null) {
            clEmpty.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
        }
        if (tvEmpty != null && emptyTip != null) {
            tvEmpty.setText(emptyTip);
        }
        if (ivEmpty != null) {
            ivEmpty.setImageResource(emptyImageRes);
        }
    }

    public void setErrorViewVisible(boolean errorViewVisible) {
        setErrorViewVisible(errorViewVisible, mCxt.getString(R.string.error_tip_placeholder));
    }

    public void setErrorViewVisible(boolean errorViewVisible, String errorTip) {
        setErrorViewVisible(errorViewVisible, errorTip, R.drawable.ic_error);
    }

    public void setErrorViewVisible(boolean errorViewVisible, String errorTip, @DrawableRes int errorImageRes) {
        if (clError != null) {
            clError.setVisibility(errorViewVisible ? View.VISIBLE : View.GONE);
        }
        if (tvError != null && errorTip != null) {
            tvError.setText(errorTip);
        }
        if (ivError != null) {
            ivError.setImageResource(errorImageRes);
        }
    }
}
