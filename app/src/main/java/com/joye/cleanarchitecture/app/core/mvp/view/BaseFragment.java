package com.joye.cleanarchitecture.app.core.mvp.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.joye.cleanarchitecture.app.core.dialog.BaseDialog;
import com.joye.cleanarchitecture.app.core.mvp.presenter.BasePresenter;
import com.joye.cleanarchitecture.domain.utils.MyLog;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类
 * <p>
 * Created by joye on 2017/12/13.
 */

public abstract class BaseFragment<P extends BasePresenter<?>> extends BaseInjectFragment implements BaseView {
    private final String CLASS_NAME = getClass().getSimpleName();

    /**
     * 宿主Activity
     */
    private BaseActivity hostActivity;
    /**
     * ButterKnife控件引用解绑器
     */
    private Unbinder unbinder;

    @Inject
    protected P mPresenter;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("---setUserVisibleHint:%b---(%s)", isVisibleToUser, CLASS_NAME);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MyLog.d("---onAttach---(%s)", CLASS_NAME);
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            hostActivity = (BaseActivity) activity;
        } else {
            throw new IllegalStateException("the host activity for current fragment must be extends 'BaseActivity'");
        }
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        MyLog.d("---onActivityCreated---(%s)", CLASS_NAME);
        super.onActivityCreated(savedInstanceState);
        initView(hostActivity.getToolBar());
        if (mPresenter != null) {
            mPresenter.onCreate(getArguments());
        }
    }

    protected Toolbar getToolbar() {
        return hostActivity.getToolBar();
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MyLog.d("---onCreateView---(%s)", CLASS_NAME);
        return onChildCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MyLog.d("---onViewCreated---(%s)", CLASS_NAME);
        unbinder = ButterKnife.bind(this, view);
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        MyLog.d("---onCreateAnimation---(%s)", CLASS_NAME);

        if (nextAnim == 0) {
            onAnimEnd(enter, null);
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
        Animation animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                MyLog.d("---onAnimationStart---(%s)", CLASS_NAME);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MyLog.d("---onAnimationEnd---(%s)", CLASS_NAME);
                onAnimEnd(enter, animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return animation;
    }

    /**
     * Fragment转场动画结束回调
     * 如果Fragment没有设置转场动画，则直接回调此方法
     *
     * @param enter     是否是入场动画
     * @param animation 动画实例
     */
    protected void onAnimEnd(boolean enter, @Nullable Animation animation) {
        if (enter) {
            if (mPresenter != null) {
                mPresenter.onEnterAnimEnd();
            }
        }
    }

    @Override
    public void onResume() {
        MyLog.d("---onResume---(%s)", CLASS_NAME);
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        MyLog.d("---onPause---(%s)", CLASS_NAME);
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    /**
     * 供子类创建布局
     *
     * @param inflater           布局加载器
     * @param container          父视图
     * @param savedInstanceState 恢复参数
     * @return 视图
     */
    protected abstract View onChildCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 初始化视图
     */
    protected abstract void initView(Toolbar toolbar);

    @Override
    public void onDestroyView() {
        MyLog.d("---onDestroyView---(%s)", CLASS_NAME);
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.d("---onDestroy---(%s)", CLASS_NAME);
    }

    /**
     * 返回键事件处理
     *
     * @return true由当前Fragment处理返回事件，false则由宿主Activity处理
     */
    protected boolean onFragmentBackPress() {
        return false;
    }

    @Override
    public void startExternalActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void finishActivity() {
        if (hostActivity != null) {
            hostActivity.finishActivity();
        }
    }

    @Override
    public void showDialog(BaseDialog baseDialog) {
        if (hostActivity != null) {
            hostActivity.showDialog(baseDialog);
        }
    }

    @Override
    public void startExternalActivity(Intent intent) {
        if (hostActivity != null) {
            hostActivity.startExternalActivity(intent);
        }
    }

    @Override
    public void setEmptyViewVisible(boolean emptyViewVisible) {
        if (hostActivity != null) {
            hostActivity.setEmptyViewVisible(emptyViewVisible);
        }
    }

    @Override
    public void setEmptyViewVisible(boolean emptyViewVisible, String emptyTip) {
        if (hostActivity != null) {
            hostActivity.setEmptyViewVisible(emptyViewVisible, emptyTip);
        }
    }

    @Override
    public void setEmptyViewVisible(boolean emptyViewVisible, String emptyTip, int emptyImageRes) {
        if (hostActivity != null) {
            hostActivity.setEmptyViewVisible(emptyViewVisible, emptyTip, emptyImageRes);
        }
    }

    @Override
    public void setErrorViewVisible(boolean errorViewVisible) {
        if (hostActivity != null) {
            hostActivity.setErrorViewVisible(errorViewVisible);
        }
    }

    @Override
    public void setErrorViewVisible(boolean errorViewVisible, String errorTip) {
        if (hostActivity != null) {
            hostActivity.setErrorViewVisible(errorViewVisible, errorTip);
        }
    }

    @Override
    public void setErrorViewVisible(boolean errorViewVisible, String errorTip, int errorImageRes) {
        if (hostActivity != null) {
            hostActivity.setErrorViewVisible(errorViewVisible, errorTip, errorImageRes);
        }
    }

    @Override
    public void showToast(String toast) {
        if (hostActivity != null) {
            hostActivity.showToast(toast);
        }
    }

    @Override
    public void showToast(String toast, boolean isLongTime) {
        if (hostActivity != null) {
            hostActivity.showToast(toast, isLongTime);
        }
    }

    @Override
    public void showLoading() {
        if (hostActivity != null) {
            hostActivity.showLoading();
        }
    }

    @Override
    public void showLoading(String loadingTip) {
        if (hostActivity != null) {
            hostActivity.showLoading(loadingTip);
        }
    }

    @Override
    public void showLoading(String loadingTip, DialogInterface.OnCancelListener onCancelListener) {
        if (hostActivity != null) {
            hostActivity.showLoading(loadingTip, onCancelListener);
        }
    }

    @Override
    public void hideLoading() {
        if (hostActivity != null) {
            hostActivity.hideLoading();
        }
    }

    @Override
    public void showCommonErrorTip(String error) {
        if (hostActivity != null) {
            hostActivity.showCommonErrorTip(error);
        }
    }

    @Override
    public void openSoftKeyboard(View focusView) {
        if (hostActivity != null) {
            hostActivity.openSoftKeyboard(focusView);
        }
    }

    @Override
    public void hideSoftKeyboard(View anyView) {
        if (hostActivity != null) {
            hostActivity.hideSoftKeyboard(anyView);
        }
    }
}
