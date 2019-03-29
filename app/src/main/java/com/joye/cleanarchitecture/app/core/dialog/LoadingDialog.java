package com.joye.cleanarchitecture.app.core.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.widget.BaseTextView;

/**
 * Loading等待框
 * <p>
 * Created by joye on 2018/8/3.
 */

public class LoadingDialog extends BaseDialog<LoadingDialog.Builder> {
    private Builder dialogBuilder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogBuilder = getBuilder();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (dialogBuilder != null) {
            String loadingTip = dialogBuilder.getContent();
            if (loadingTip != null) {
                View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);
                BaseTextView tvTip = loadingView.findViewById(R.id.tv_loading_tip);
                tvTip.setText(loadingTip);
                Dialog dialog = new Dialog(getContext(), R.style.Base_Dialog_Loading);
                dialog.setContentView(loadingView);
                dialog.setOnCancelListener(dialogBuilder.getOnCancelListener());
                dialog.setOnDismissListener(dialogBuilder.getOnDismissListener());
                dialog.setCanceledOnTouchOutside(false);
                return dialog;
            }
        }
        return super.onCreateDialog(savedInstanceState);
    }

    public static class Builder extends BaseDialog.Builder<LoadingDialog.Builder, LoadingDialog> {

        @Override
        public LoadingDialog build() {
            LoadingDialog loadingDialog = new LoadingDialog();
            loadingDialog.setBuilder(this);
            return loadingDialog;
        }
    }
}
