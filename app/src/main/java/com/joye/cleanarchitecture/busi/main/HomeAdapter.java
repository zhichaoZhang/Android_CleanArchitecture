package com.joye.cleanarchitecture.busi.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.domain.model.transaction.Transaction;
import com.joye.cleanarchitecture.widget.BaseRecyclerView;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends BaseRecyclerView.BaseAdapter<HomeAdapter.HomeViewHolder, List<Transaction>> {
    private List<Transaction> mTransactions;

    public HomeAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        if (mTransactions.size() < position) {
            return;
        }

        Transaction transaction = mTransactions.get(position);
        holder.tvId.setText(transaction.getId());
        holder.tvTime.setText(transaction.getTransactionDate().toString());
    }

    @Override
    public int getItemCount() {
        return mTransactions == null ? 0 : mTransactions.size();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(LayoutInflater layoutInflater, @NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.viewholder_home, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void updateData(List<Transaction> transactions) {
        mTransactions = transactions;
        notifyDataSetChanged();
    }

    static class HomeViewHolder extends BaseRecyclerView.BaseViewHolder {
        @BindView(R.id.tv_id)
        TextView tvId;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
