package com.joye.cleanarchitecture.busi.login;

import android.content.Intent;

import com.joye.cleanarchitecture.R;
import com.joye.cleanarchitecture.busi.main.MainActivity;
import com.joye.cleanarchitecture.domain.interactor.UserInteractor;
import com.joye.cleanarchitecture.domain.model.User;
import com.joye.cleanarchitecture.domain.model.UserConfig;
import com.joye.cleanarchitecture.domain.model.common.Contact;
import com.joye.cleanarchitecture.domain.repository.Cache;
import com.joye.cleanarchitecture.domain.repository.IdentityAuth;
import com.joye.cleanarchitecture.domain.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import io.reactivex.Observable;
import com.joye.cleanarchitecture.RobolectricTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 登录页视图逻辑测试
 * <p>
 * Created by joye on 2018/8/5.
 */
public class LoginPresenterTest extends RobolectricTest {
    private final String MOCK_ACCOUNT = "15330059740";
    private final String MOCK_PASSWD = "059740";

    @Mock
    LoginView loginView;

    @Mock
    UserRepository userRepository;

    @Mock
    Cache<User> userCache;

    @Mock
    Cache<UserConfig> userConfigCache;

    @Mock
    IdentityAuth identityAuth;

    UserInteractor userInteractor;
    LoginPresenter loginPresenter;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        //这里做了一个妥协，正确方式应该是mock UserInteractor类，但需要依赖BaseInteractor的execute方法，所以mock了UserRepository类
        userInteractor = new UserInteractor(threadExecutor, postExecutionThread, userRepository, userCache, userConfigCache, identityAuth);
        loginPresenter = new LoginPresenter(mContext, loginView, userInteractor);
    }

    @Test
    public void testLoginSuc() throws Exception {
        Observable<User> result = Observable.just(createNormalUser(1));
        when(userCache.update(any(User.class))).thenReturn(result);
        //假设登录成功，返回正常
        when(userRepository.loginForUser(MOCK_ACCOUNT, MOCK_PASSWD)).thenReturn(result);
        //调用Presenter的登录操作，实际操作中会取出账号和密码编辑框中的输入，这里输入假数据
        loginPresenter.login(MOCK_ACCOUNT, MOCK_PASSWD);
        //第一步：显示Loading等待框
        verify(loginView).showLoading(mContext.getString(R.string.logging_in));
        //第二步：显示"登录成功"Toast，并打开主页
        verify(loginView).showToast(mContext.getString(R.string.login_success));
        ArgumentCaptor<Intent> intentArgumentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(loginView).startActivity(intentArgumentCaptor.capture());
        //验证是否是打开的主页面
        assertEquals(intentArgumentCaptor.getValue().getComponent().getClassName(), MainActivity.class.getName());
        //第三步：关闭登录页
        verify(loginView).finishActivity();
        //第四步：隐藏Loading等待框
        verify(loginView).hideLoading();
    }

    @Test
    public void testAccountEmpty() throws Exception {
        loginPresenter.login("", "123");
        verify(loginView).showAccountError(mContext.getString(R.string.account_must_not_be_empty));
    }

    @Test
    public void testPasswdEmpty() throws Exception {
        loginPresenter.login("123456789", "");
        verify(loginView).showPassWdError(mContext.getString(R.string.password_must_not_be_empty));
    }

    private User createNormalUser(int userId) {
        User user = new User(userId);
        Contact contact = new Contact();
        contact.setMobilePhone("1323123123");
        user.setContact(contact);
        user.setSessionId("83cca947-8184-43d6-a6cf-23ab7484449d");
        return user;
    }
}