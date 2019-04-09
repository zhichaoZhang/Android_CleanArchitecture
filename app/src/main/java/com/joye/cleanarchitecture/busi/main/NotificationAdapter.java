package com.joye.cleanarchitecture.busi.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.domain.model.message.Notification;
import com.joye.cleanarchitecture.widget.BaseRecyclerView;
import com.joye.cleanarchitecture.widget.refreshview.RecyclerViewWithRefresh.LoadMoreAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;

public class NotificationAdapter extends LoadMoreAdapter<NotificationAdapter.NotificationViewHolder, List<Notification>> {
    private List<Notification> mNotificationList;

    public NotificationAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindChildViewHolder(@NonNull NotificationViewHolder holder, int position) {
        if (position > getChildItemCount()) {
            return;
        }
        Notification notification = mNotificationList.get(position);
        holder.tvId.setText(notification.getId());
        holder.tvTime.setText(notification.getDate().toString());
    }

    @Override
    protected int getChildItemViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    protected NotificationViewHolder onCreateChildViewHolder(LayoutInflater layoutInflater, @NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(layoutInflater.inflate(R.layout.viewholder_notification, parent, false));
    }

    @Override
    protected int getChildItemCount() {
        return mNotificationList == null ? 0 : mNotificationList.size();
    }

    @Override
    public void updateData(List<Notification> notifications) {
        mNotificationList = notifications;
        notifyDataSetChanged();
    }

    static class NotificationViewHolder extends BaseRecyclerView.BaseViewHolder {
        @BindView(R.id.tv_id)
        TextView tvId;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
