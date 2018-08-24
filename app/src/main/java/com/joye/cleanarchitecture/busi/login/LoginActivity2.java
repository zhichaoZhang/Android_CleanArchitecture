package com.joye.cleanarchitecture.busi.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.app.core.mvp.view.BaseActivity;
import com.joye.cleanarchitecture.busi.main.MainActivity;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.utils.TextUtils;
import com.joye.cleanarchitecture.utils.ToastUtil;
import com.joye.cleanarchitecture.widget.BaseEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 登录页面
 */
public class LoginActivity2 extends BaseActivity {

    AutoCompleteTextView etAccount;
    BaseEditText etPassword;
    Button btnLogin;
    LoginHandler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etAccount = findViewById(R.id.account);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_sign_in);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });
        loginHandler = new LoginHandler(getApplicationContext());
    }

    private void onClickLogin() {
        String account = etAccount.getText().toString().trim();
        String passwd = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            etAccount.setError("please input account");
            return;
        }
        if (TextUtils.isEmpty(passwd)) {
            etPassword.setError("please input password");
            return;
        }
        if(!isAccountValidate(account)) {
            etPassword.setError("please input validate account");
            return;
        }
        btnLogin.setEnabled(false);
        showLoading("正在登录");
        login(account, passwd);
    }

    //验证账号格式
    private boolean isAccountValidate(String account) {
        return false;
    }

    private void login(String account, String passwd) {
        String loginUrl = "https://www.o.qfpay.com/login";
        new Thread() {
            @Override
            public void run() {
                super.run();
                URL url = null;
                try {
                    url = new URL(loginUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(10 * 1000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.addRequestProperty("User-Agent", "android");
                    OutputStream outputStream = urlConnection.getOutputStream();
                    String stringBuilder = "" + "username" + "=" + account + "&" +
                            "password" + "=" + passwd;
                    outputStream.write(stringBuilder.getBytes());

                    int respCode = urlConnection.getResponseCode();
                    if (respCode != 200) {
                        Message errorMsg = loginHandler.obtainMessage();
                        errorMsg.what = MSG_LOGIN_ERROR;
                        loginHandler.sendMessage(errorMsg);
                    } else {
                        InputStream inputStream = urlConnection.getInputStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int readCount;
                        while ((readCount = bufferedInputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, readCount);
                        }
                        String result = new String(byteArrayOutputStream.toByteArray());
                        Message sucMsg = loginHandler.obtainMessage();
                        Bundle param = new Bundle();
                        param.putString("result", result);
                        sucMsg.what = MSG_LOGIN_SUCCESS;
                        sucMsg.setData(param);
                        loginHandler.sendMessage(sucMsg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Message errorMsg = loginHandler.obtainMessage();
                    errorMsg.what = MSG_LOGIN_ERROR;
                    loginHandler.sendMessage(errorMsg);
                }
            }
        }.start();
    }


    private static final int MSG_LOGIN_ERROR = 1;
    private static final int MSG_LOGIN_SUCCESS = MSG_LOGIN_ERROR + 1;

    private class LoginHandler extends Handler {
        private Context context;

        public LoginHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_LOGIN_ERROR:
                    ToastUtil.showLong(context, "网络请求失败");
                    break;
                case MSG_LOGIN_SUCCESS:
                    String result = msg.getData().getString("result");
                    parseLoginResult(result);
                    break;
            }
        }
    }

    private void parseLoginResult(String result) {
        ResponseWrapper responseWrapper = new ResponseWrapper<User>();
        try {
            JSONObject jsonResult = new JSONObject(result);
            responseWrapper.respcd = jsonResult.getInt("respcd");
            responseWrapper.resperr = jsonResult.getString("resperr");
            responseWrapper.data = jsonResult.getJSONObject("data");
            if (responseWrapper.respcd != 0000) {
                ToastUtil.showLong(getApplicationContext(), "登录失败：" + responseWrapper.resperr);
            } else {
                loginSuccess((User) responseWrapper.data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.showLong(getApplicationContext(), "数据解析异常");
        }
    }

    private void loginSuccess(User user) {
        //保存用户信息到磁盘
        saveUser2Disk(user);
        //验证用户信息是否完整
        checkUser(user);
        //其他组件初始化
        //...


        //提示用户登录成功并跳转到主页面
        ToastUtil.showLong(getApplicationContext(), "登录成功");
        startActivity(MainActivity.getCallingIntent(getApplicationContext()));
    }

    private void checkUser(User user) {

    }

    private void saveUser2Disk(User user) {

    }

    public class ResponseWrapper<D> {
        private int respcd;
        private String resperr;
        private D data;

        public int getRespcd() {
            return respcd;
        }

        public String getResperr() {
            return resperr;
        }

        public D getData() {
            return data;
        }

        public void setRespcd(int respcd) {
            this.respcd = respcd;
        }

        public void setResperr(String resperr) {
            this.resperr = resperr;
        }

        public void setData(D data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "ResponseWrapper{" +
                    "respcd=" + respcd +
                    ", resperr='" + resperr + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
}

