package com.joye.cleanarchitecture.busi.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseActivity;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseInjectActivity;
import com.joye.cleanarchitecture.data.utils.MyLog;

import butterknife.BindView;

/**
 * 欢迎页
 * <p>
 * Created by joye on 2018/8/22.
 */

public class WelActivity extends BaseInjectActivity<WelPresenter> implements WelView {

    @BindView(R.id.tv_countdown)
    TextView tvCountdown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);
    }

    @Override
    public void setCountDownTime(int timeRemain) {
        tvCountdown.setText(String.valueOf(timeRemain));
    }

    @Override
    public void setAdImage(String adImageUrl) {
        MyLog.d("setAdImage(): %s", adImageUrl);
    }
}
