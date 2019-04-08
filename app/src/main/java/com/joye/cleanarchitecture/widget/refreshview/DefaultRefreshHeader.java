package com.joye.cleanarchitecture.widget.refreshview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.domain.utils.MyLog;
import com.joye.cleanarchitecture.widget.BaseTextView;

/**
 * 默认的下拉刷新视图
 */
public class DefaultRefreshHeader implements RefreshHeader {
    private ProgressBar mProgressBar;
    private BaseTextView mTextView;
    private Context mCxt;

    public DefaultRefreshHeader(Context context) {
        this.mCxt = context;
    }

    @Override
    public View getRefreshView() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mCxt).inflate(R.layout.view_default_refresh, null, false);
        mProgressBar = view.findViewById(R.id.pb_refreshing);
        mTextView = view.findViewById(R.id.btv_refresh);
        return view;
    }

    @Override
    public void onPullDown(float deltaY) {
        MyLog.d("onPullDown distance is %s", deltaY);
        mProgressBar.setVisibility(View.GONE);
        mTextView.setText(mCxt.getString(R.string.pull_down_to_refresh));
    }

    @Override
    public void onReadyToRefresh() {
        mProgressBar.setVisibility(View.GONE);
        mTextView.setText(mCxt.getString(R.string.release_to_refresh));
    }

    @Override
    public void onRefreshing() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setText(mCxt.getString(R.string.refreshing));
    }

    @Override
    public void onRefreshCompleted() {
        mProgressBar.setVisibility(View.GONE);
        mTextView.setText(mCxt.getString(R.string.refresh_completed));
    }
}
