package com.joye.cleanarchitecture.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

/**
 * 列表控件基类
 */
public class BaseRecyclerView extends RecyclerView {

    public BaseRecyclerView(@NonNull Context context) {
        super(context);
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * ViewHolder基类
     */
    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * RecycleView 适配器基类
     *
     * @param <VH> ViewHolder类型
     */
    public static abstract class BaseAdapter<VH extends BaseViewHolder, M> extends RecyclerView.Adapter<VH> {
        private LayoutInflater mLayoutInflater;
        protected Context mCxt;

        public BaseAdapter(Context context) {
            this.mCxt = context;
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return onCreateViewHolder(mLayoutInflater, parent, viewType);
        }

        @NonNull
        public abstract VH onCreateViewHolder(LayoutInflater layoutInflater, @NonNull ViewGroup parent, int viewType);

        /**
         * 更新数据
         *
         * @param m 数据模型
         */
        public abstract void updateData(M m);
    }
}
