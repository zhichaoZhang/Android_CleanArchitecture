package com.joye.cleanarchitecture.app.core.mvp.view;

import android.content.Context;

import androidx.fragment.app.Fragment;
import dagger.android.support.AndroidSupportInjection;

/**
 * 实现自动依赖注入的基类Fragment
 *
 * Created by joye on 2018/8/21.
 */

public abstract class BaseInjectFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
}
