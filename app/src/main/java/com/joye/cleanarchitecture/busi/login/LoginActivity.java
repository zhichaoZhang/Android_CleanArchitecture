package com.joye.cleanarchitecture.busi.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseInjectActivity;
import com.joye.cleanarchitecture.widget.BaseEditText;

import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * 登录页面
 */
public class LoginActivity extends BaseInjectActivity<LoginPresenter> implements LoginView {

    @BindView(R.id.account)
    AutoCompleteTextView etAccount;
    @BindView(R.id.password)
    BaseEditText etPassword;
    @BindView(R.id.btn_sign_in)
    Button btnLogin;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            Set<String> params = uri.getQueryParameterNames();
            if (params != null) {
                for (String paramName : params) {
                    String paramValue = uri.getQueryParameter(paramName);
                    System.out.println("参数名: " + paramName + ", 参数值: " + paramValue);
                }
            }
        }
    }

    @Override
    public void showAccountError(String string) {
        etAccount.setError(string);
    }

    @Override
    public void showPassWdError(String string) {
        etPassword.setError(string);
    }

    @Override
    public void setLoginBtnEnable(boolean loginBtnEnable) {
        btnLogin.setEnabled(loginBtnEnable);
    }

    @OnClick(R.id.btn_sign_in)
    public void onClickLoginBtn() {
        confirmLogin();
    }

    @OnEditorAction(R.id.password)
    public boolean onClickConfirmEditor(int imeId) {
        if (imeId == 6) {
            confirmLogin();
            return true;
        }
        return false;
    }

    private void confirmLogin() {
        String inputAccount = etAccount.getText().toString().trim();
        String inputPasswd = etPassword.getText().toString().trim();
        mPresenter.login(inputAccount, inputPasswd);
    }
}

