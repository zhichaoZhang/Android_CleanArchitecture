package com.joye.cleanarchitecture.busi.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.R2;
import com.joye.cleanarchitecture.adapter.FragmentAdapter;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseActivity;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R2.id.vp_main)
    ViewPager vpMain;
    @BindView(R2.id.navigation)
    BottomNavigationView navigationView;

    private FragmentAdapter vpAdapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    /**
     * 底部导航栏点击监听
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                if (vpMain.getCurrentItem() != 0) {
                    vpMain.setCurrentItem(0, false);
                }
                return true;
            case R.id.navigation_dashboard:
                if (vpMain.getCurrentItem() != 1) {
                    vpMain.setCurrentItem(1, false);
                }
                return true;
            case R.id.navigation_notifications:
                if (vpMain.getCurrentItem() != 2) {
                    vpMain.setCurrentItem(2, false);
                }
                return true;
        }
        return false;
    };

    /**
     * ViewPage页面切换监听
     */
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    navigationView.setSelectedItemId(R.id.navigation_home);
                    break;
                case 1:
                    navigationView.setSelectedItemId(R.id.navigation_dashboard);
                    break;
                case 2:
                    navigationView.setSelectedItemId(R.id.navigation_notifications);
                    break;
            }
            setPageTitle(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected boolean isInjectActivity() {
        return false;
    }

    @Override
    protected void initView(Toolbar toolbar) {
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        List<BaseFragment> fragments = new ArrayList<>(3);
        fragments.add(HomeFragment.newInstance());
        fragments.add(DashboardFragment.newInstance());
        fragments.add(NotificationFragment.newInstance());
        vpAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vpMain.setAdapter(vpAdapter);
        vpMain.addOnPageChangeListener(mOnPageChangeListener);
        vpMain.setOffscreenPageLimit(2);
        vpMain.setCurrentItem(0);
        setPageTitle(0);
    }

    // 设置标题栏标题
    private void setPageTitle(int currentItem) {
        String pageTitle = null;
        switch (currentItem) {
            case 0:
                pageTitle = getString(R.string.title_home);
                break;
            case 1:
                pageTitle = getString(R.string.title_dashboard);
                break;
            case 2:
                pageTitle = getString(R.string.title_notifications);
                break;
        }
        getToolBar().setTitle(pageTitle);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true);
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }
}
