package com.joye.cleanarchitecture.app.core.dialog;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * 对话框基类
 * <p>
 * 使用DialogFragment作为Dialog的容器，更容易控制返回键和屏幕旋转时Dialog的生命周期
 * <p>
 * Created by joye on 2018/8/3.
 */

public abstract class BaseDialog<B extends BaseDialog.Builder> extends AppCompatDialogFragment {
    private B builder;

    public void setBuilder(B builder) {
        this.builder = builder;
    }

    protected B getBuilder() {
        return builder;
    }

    /**
     * 构建DialogFragment时传入的构建器参数
     */
    public static final String ARG_DIALOG_BUILDER = "dialog_builder";

    /**
     * 对话框中操作按钮点击事件回调
     */
    public interface OnDialogButtonClickListener {
        /**
         * 点击肯定按钮
         *
         * @param dialogFragment 对话框操作接口
         */
        void onPositive(BaseDialog dialogFragment);

        /**
         * 点击否定按钮
         *
         * @param dialogFragment 对话框操作接口
         */
        void onNegative(BaseDialog dialogFragment);
    }

    /**
     * 对话框构建器
     */
    public abstract static class Builder<B extends Builder<?, ?>, D extends BaseDialog> {
        /**
         * 标题
         */
        private String title;
        /**
         * 内容
         */
        private String content;
        /**
         * 肯定按钮文本
         */
        private String positiveBtnText;
        /**
         * 否定按钮文本
         */
        private String negativeBtnText;
        /**
         * 对话框按钮点击监听
         */
        private OnDialogButtonClickListener onDialogButtonClickListener;
        /**
         * 对话框被取消回调
         * 点击返回键和触摸对话框以外区域
         */
        private DialogInterface.OnCancelListener onCancelListener;
        /**
         * 对话框消失回调
         * 用户主动操作引起的对话框关闭
         */
        private DialogInterface.OnDismissListener onDismissListener;

        public Builder() {
        }

        public B setTitle(String title) {
            this.title = title;
            return getThis();
        }

        public B setContent(String content) {
            this.content = content;
            return getThis();
        }

        public B setPositiveBtnText(String positiveBtnText) {
            this.positiveBtnText = positiveBtnText;
            return getThis();
        }

        public B setNegativeBtnText(String negativeBtnText) {
            this.negativeBtnText = negativeBtnText;
            return getThis();
        }

        public B setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return getThis();
        }

        public B setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return getThis();
        }

        public B setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
            this.onDialogButtonClickListener = onDialogButtonClickListener;
            return getThis();
        }

        private B getThis() {
            return (B) this;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getPositiveBtnText() {
            return positiveBtnText;
        }

        public String getNegativeBtnText() {
            return negativeBtnText;
        }

        public OnDialogButtonClickListener getOnDialogButtonClickListener() {
            return onDialogButtonClickListener;
        }

        public DialogInterface.OnCancelListener getOnCancelListener() {
            return onCancelListener;
        }

        public DialogInterface.OnDismissListener getOnDismissListener() {
            return onDismissListener;
        }

        public abstract D build();
    }

}
