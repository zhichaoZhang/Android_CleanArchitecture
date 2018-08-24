package com.joye.cleanarchitecture.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.joye.cleanarchitecture.app.core.mvp.view.BaseFragment;

import java.util.List;

/**
 * Fragment列表适配器
 *
 * Created by joye on 2018/8/21.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private final List<BaseFragment> fragments;

    public FragmentAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }
}
