package com.joye.cleanarchitecture.app.core.mvp.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import dagger.android.AndroidInjection;

/**
 * 实现自动依赖注入的Activity基类
 * <p>
 * Created by joye on 2018/8/21.
 */

public abstract class BaseInjectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isInjectActivity()) {
            //Dagger2依赖注入
            AndroidInjection.inject(this);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * 是否针对Activity做依赖注入，默认为true。
     * 返回false则不会调用AndroidInjection.inject(this);
     * <p>
     * 如果使用Activity+多Fragment的方式组织页面，则可以不为宿主Activity添加依赖注入
     *
     * @return 是否针对Activity做依赖注入，默认为true
     */
    protected boolean isInjectActivity() {
        return true;
    }
}
