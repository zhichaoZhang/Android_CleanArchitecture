package com.joye.cleanarchitecture.widget.refreshview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joye.cleanarchitecture.R;

public class DefaultLoadMoreFooter implements LoadMoreFooter {
    private Context mCxt;
    private ProgressBar pbLoadingMore;
    private TextView tvLoadingMore;

    public DefaultLoadMoreFooter(Context context) {
        this.mCxt = context;
    }

    @Override
    public View getLoadMoreView() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mCxt).inflate(R.layout.view_default_load_more, null, false);
        pbLoadingMore = view.findViewById(R.id.pb_loading_more);
        tvLoadingMore = view.findViewById(R.id.btv_loading_more);
        return view;
    }

    @Override
    public void onPullUp(float deltaY) {
        pbLoadingMore.setVisibility(View.GONE);
        tvLoadingMore.setText(mCxt.getString(R.string.pull_up_to_load_more));
    }

    @Override
    public void onReadyToLoadMore() {
        pbLoadingMore.setVisibility(View.GONE);
        tvLoadingMore.setText(mCxt.getString(R.string.release_to_loadmore));
    }

    @Override
    public void onLoadingMore() {
        pbLoadingMore.setVisibility(View.VISIBLE);
        tvLoadingMore.setText(mCxt.getString(R.string.loading));
    }

    @Override
    public void onLoadMoreCompleted() {
        pbLoadingMore.setVisibility(View.GONE);
        tvLoadingMore.setText(mCxt.getString(R.string.refresh_completed));
    }

    @Override
    public void noMore() {
        pbLoadingMore.setVisibility(View.GONE);
        tvLoadingMore.setText(mCxt.getString(R.string.no_more_data));
    }
}
