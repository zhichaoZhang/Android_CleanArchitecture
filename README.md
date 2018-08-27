# Android CleanArchitecture
基于Clean Architecture和MVP思想的项目结构示例。
基本实现了欢迎页 -> 登录页 -> 主页的一般逻辑。可基于此项目开发直接添加不同的业务模块。

## 项目结构图
<img src="https://near.qfpay.com.cn/op_upload/483425/1535360655744.png" width=656 height=426 />

## 主要第三方库依赖

* Dagger2               依赖注入库
* Rxjava                链式异步调用库
* ButterKnife           UI控件注入库
* OkHttp                网络库
* Retrofit              网络请求封装库
* Room                  数据库

* Junit                 单元测试库
* Robolectric           虚拟Android单元测试环境库
* AndroidJUnitRunner    集成测试库
* Mockito               Mock和断言库

* LeakCanary            内存泄漏检测
* Stetho                网络、数据库监控

## data层设计
根据Clean Architecture的思想，data层负责业务逻辑中数据访问，包含网络数据、数据库数据、缓存数据等。

```
public class UserDataRepoImpl implements UserRepository {
    private final UserService userService;
    private UserEntityMapper userEntityMapper;
    private UserDao userDao;

    @Override
    public Observable<User> loginForUser(String account, String passwd) {
        return userService.login(account, passwd)
                .map(new ServerResultFunc<>())
                .map(userEntity -> userEntityMapper.transform(userEntity));
    }
}
```
Repository数据库仓库除了提供数据访问外，还封装了一般网络错误(RxErrorHandlingCallAdapterFactory)和一般业务代码错误(ServerResultFunc)，直接向上层抛出异常。
另外实现了基于Cookie-Set响应头的Cookie管理，调用者也可以手动添加Cookie和User-Agent。

## domain层设计
domain层实现了和UI无关的业务逻辑，并通过Repository和Cache接口实现数据访问。
对于异常的业务逻辑分支，通过抛出自定义异常的方式抛出，展示层可根据不同的异常控制UI显示。

```
public class UserInteractor extends BaseInteractor {
    /**
         * 用户登录
         *
         * @param userName 用户名
         * @param password 密码
         * @return 登录操作实例
         */
        public Observable<User> login(String userName, String password) {
            return Observable.create((ObservableOnSubscribe<String>) emitter -> {
                // 验证账户、密码格式
                // 密码加密
                emitter.onNext(password);
                emitter.onComplete();
            }).flatMap(entryPasswd -> userRepository.loginForUser(userName, entryPasswd))
                    .onErrorResumeNext(throwable -> {
                        if (throwable instanceof CommonDomainException) {
                            int respCode = ((CommonDomainException) throwable).getExcCode();
                            if (respCode == EXC_DOMAIN_USER_NOT_REGISTER) {
                                throwable = new UnregisterException(throwable, respCode, throwable.getMessage());
                            }
                        }
                        return Observable.error(throwable);
                    })
                    //持久化
                    .flatMap(u -> {
                        u.setLogged(true);
                        return userCache.update(u);
                    })
                    .doOnNext(user -> {
                        //用户信息完整性检查
                        if (user.getContact() == null) {
                            throw new UserInfoIncompleteException(EXC_DOMAIN_USER_INFO_INCOMPLETE, "the user's contact does not exist.");
                        }
                    });
        }
}
```

## presentation层设计

Presentation层要根据不同的业务处理结果，展示不同的UI和页面跳转。

```
public class LoginPresenter extends BasePresenter<LoginView> {
    if (TextUtils.isEmpty(account)) {
        mView.showAccountError(mCtx.getString(R.string.account_must_not_be_empty));
        return;
    }
    if (TextUtils.isEmpty(passwd)) {
        mView.showPassWdError(mCtx.getString(R.string.password_must_not_be_empty));
        return;
    }
    mView.setLoginBtnEnable(false);
    mView.showLoading(mCtx.getString(R.string.logging_in));
    Observable<User> userObservable = mUserInteractor.login(account, passwd);
    mUserInteractor.execute(userObservable, new UIObserver<User>(mView) {
        @Override
        public void onNext(User value) {
            super.onNext(value);
            mView.showToast(mCtx.getString(R.string.login_success));
            mView.startActivity(MainActivity.getCallingIntent(mCtx));
            mView.finishActivity();
        }

        @Override
        public void onFinally() {
            super.onFinally();
            mView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            //登录失败
            mView.setLoginBtnEnable(true);

            if (e instanceof UnregisterException) {
                mView.startActivity(RegisterActivity.getCallingIntent(mCtx));
            }

            if (e instanceof UserInfoIncompleteException) {
                mView.showCommonErrorTip(((UserInfoIncompleteException) e).getExcMsg());
            }
        }
    });
}
```

### 一般异常统一处理

```
public class UIObserver<T> extends DefaultObserver<T> {
    private BaseView baseView;

    public UIObserver(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        handleCommonError(e);
    }

    private void handleCommonError(Throwable e) {
        if (e instanceof NetErrorException) {
            if (e instanceof HttpException) {
                showErrorMsg("服务器异常，请稍后重试");
            } else if (e instanceof NetWorkException) {
                showErrorMsg("网络连接异常，请检查网络后重试");
            } else if (e instanceof UnknownException) {
                showErrorMsg("未知网络错误");
            }
        }

        if (e instanceof CommonDomainException) {
            showErrorMsg(e.getMessage());
        }
    }

    //显示错误信息，可根据产品要求使用不同的提示形式
    private void showErrorMsg(String errorMsg) {
        if (baseView != null) {
            baseView.showCommonErrorTip(errorMsg);
        }
    }
}
```


