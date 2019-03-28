package com.joye.cleanarchitecture.app.core.mvp.view;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.dialog.BaseDialog;
import com.joye.cleanarchitecture.app.core.dialog.LoadingDialog;
import com.joye.cleanarchitecture.app.core.mvp.presenter.BasePresenter;
import com.joye.cleanarchitecture.domain.utils.MyLog;
import com.joye.cleanarchitecture.utils.InputTypeUtil;
import com.joye.cleanarchitecture.utils.ToastUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类
 * <p>
 * 使用ToolBar作为应用标题栏
 * 提供空白页、错误页、Loading对话框的操作
 * <p>
 * <p>
 * Created by joye on 2017/12/13.
 */

public abstract class BaseActivity<B extends BasePresenter<?>> extends AppCompatActivity implements BaseView {
    private final String CLASS_NAME = getClass().getSimpleName();

    /**
     * 标题栏布局
     */
    protected Toolbar toolbar;
    /**
     * 内容布局容器
     */
    private FrameLayout flContent;
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

    /**
     * ButterKnife空间引用解绑器
     */
    private Unbinder unbinder;

    /**
     * 等待对话框
     */
    private LoadingDialog loadingDialog;

    @Inject
    protected B mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.d("---onCreate()---(%s)", CLASS_NAME);
        super.setContentView(R.layout.activity_base);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.app_bar);
        flContent = findViewById(R.id.fl_content);
        clEmpty = findViewById(R.id.cl_empty);
        clError = findViewById(R.id.cl_error);
        tvEmpty = findViewById(R.id.tv_empty);
        ivEmpty = findViewById(R.id.iv_empty);
        tvError = findViewById(R.id.tv_error);
        ivError = findViewById(R.id.iv_error);

        setSupportActionBar(toolbar);
    }

    /*代理setContentView方法，试子类布局添加指定容器中*/
    @Override
    public void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view == null) {
            MyLog.e("setContentView(): the view is null.");
            return;
        }
        flContent.removeAllViews();
        if (params == null) {
            flContent.addView(view);
        } else {
            flContent.addView(view, params);
        }
        unbinder = ButterKnife.bind(this, view);
        if (mPresenter != null) {
            mPresenter.onCreate(getIntent().getExtras());
        }
    }
    /*代理setContentView方法，使子类布局添加指定容器中*/

    @Override
    protected void onStart() {
        super.onStart();
        MyLog.d("---onStart()---(%s)", CLASS_NAME);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MyLog.d("---onRestart()---(%s)", CLASS_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.d("---onResume()---(%s)", CLASS_NAME);
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyLog.d("---onStop()---(%s)", CLASS_NAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyLog.d("---onPause()---(%s)", CLASS_NAME);
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.d("---onDestroy()---(%s)", CLASS_NAME);
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MyLog.d("---onSaveInstanceState()---(%s)", CLASS_NAME);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        MyLog.d("---onRestoreInstanceState()---(%s)", CLASS_NAME);
    }

    @Override
    public void onBackPressed() {
        BaseFragment childActiveFragment = getActiveFragment(getSupportFragmentManager());
        if (childActiveFragment == null || !childActiveFragment.onFragmentBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (intent == null) {
            MyLog.e("startActivity(): intent is null.");
            return;
        }
        try {
            intent.setPackage(getPackageName());
            super.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            MyLog.e(e, "open intent(%s) fail.", intent.toString());
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null) {
            MyLog.e("startActivityForResult(): intent is null.");
            return;
        }
        intent.setPackage(getPackageName());
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startExternalActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startExternalActivity(Intent intent) {
        if (intent == null) {
            MyLog.e("startActivity(): intent is null.");
            return;
        }
        try {
            super.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            MyLog.e(e, "open intent(%s) fail.", intent.toString());
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void setEmptyViewVisible(boolean emptyViewVisible) {
        setEmptyViewVisible(emptyViewVisible, getString(R.string.empty_tip_placeholder));
    }

    @Override
    public void setEmptyViewVisible(boolean emptyViewVisible, String emptyTip) {
        setEmptyViewVisible(emptyViewVisible, emptyTip, R.drawable.img_empty);
    }

    @Override
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

    @Override
    public void setErrorViewVisible(boolean errorViewVisible) {
        setErrorViewVisible(errorViewVisible, getString(R.string.error_tip_placeholder));
    }

    @Override
    public void setErrorViewVisible(boolean errorViewVisible, String errorTip) {
        setEmptyViewVisible(errorViewVisible, errorTip, R.drawable.img_empty);
    }

    @Override
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

    @Override
    public void showToast(String toast) {
        showToast(toast, true);
    }

    @Override
    public void showToast(String toast, boolean isLongTime) {
        if (isLongTime) {
            ToastUtil.showLong(getApplicationContext(), toast);
        } else {
            ToastUtil.showShort(getApplicationContext(), toast);
        }
    }

    @Override
    public void showLoading() {
        showLoading(getString(R.string.loading));
    }

    @Override
    public void showLoading(String loadingTip) {
        showLoading(loadingTip, null);
    }

    @Override
    public void showLoading(String loadingTip, DialogInterface.OnCancelListener onCancelListener) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        loadingDialog = new LoadingDialog.Builder()
                .setContent(loadingTip)
                .setOnCancelListener(onCancelListener)
                .build();
        loadingDialog.show(getSupportFragmentManager(), "loading");
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismissAllowingStateLoss();
        }
    }

    @Override
    public void showDialog(BaseDialog baseDialog) {
        if (baseDialog == null) {
            MyLog.e("showDialog(): baseDialog is null.");
            return;
        }
        baseDialog.show(getSupportFragmentManager(), baseDialog.getTag());
    }

    @Override
    public void showCommonErrorTip(String error) {
        if (error == null) {
            MyLog.e("showCommonErrorTip(): error is null.");
            return;
        }
        if (toolbar == null) {
            MyLog.e("showCommonErrorTip(): toolbar is null.");
            return;
        }
        Snackbar.make(toolbar, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void openSoftKeyboard(View focusView) {
        InputTypeUtil.openSoftKeyBoard(getApplicationContext(), focusView);
    }

    @Override
    public void hideSoftKeyboard(View anyView) {
        InputTypeUtil.closeSoftKeyBoard(getApplicationContext(), anyView);
    }

    private BaseFragment getActiveFragment(FragmentManager fragmentManager) {
        return getActiveFragment(fragmentManager, null);
    }

    private BaseFragment getActiveFragment(FragmentManager fragmentManager, BaseFragment parentFragment) {
        if (fragmentManager == null) {
            MyLog.e("getActiveFragment(): fragment manager is null.");
            return null;
        }
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null) {
            return parentFragment;
        }

        int activeFSize = fragments.size();
        for (int i = activeFSize - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment instanceof BaseFragment) {
                if (fragment.isResumed() && !fragment.isHidden() && fragment.getUserVisibleHint()) {
                    return getActiveFragment(fragment.getChildFragmentManager(), (BaseFragment) fragment);
                }
            }
        }
        return parentFragment;
    }
}
