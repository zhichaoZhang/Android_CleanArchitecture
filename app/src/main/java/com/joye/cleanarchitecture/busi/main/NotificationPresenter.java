package com.joye.cleanarchitecture.busi.main;

import android.content.Context;
import android.os.Bundle;

import com.joye.cleanarchitecture.app.core.mvp.presenter.BaseListPresenter;
import com.joye.cleanarchitecture.app.core.mvp.presenter.BasePresenter;
import com.joye.cleanarchitecture.app.core.mvp.presenter.PagingByPageNumListPresenter;
import com.joye.cleanarchitecture.domain.interactor.NotifyInteractor;
import com.joye.cleanarchitecture.domain.interactor.UserInteractor;
import com.joye.cleanarchitecture.domain.model.message.Notification;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * 首页视图逻辑
 * <p>
 * Created by joye on 2018/8/5.
 */

public class NotificationPresenter extends PagingByPageNumListPresenter<NotificationView, List<Notification>> {
    private UserInteractor userInteractor;
    private NotifyInteractor mNotifyInteractor;
    private List<Notification> mNotificationList = new ArrayList<>();

    @Inject
    public NotificationPresenter(Context context, NotificationView notificationView, UserInteractor userInteractor, NotifyInteractor notifyInteractor) {
        super(context);
        this.userInteractor = userInteractor;
        this.mNotifyInteractor = notifyInteractor;
        this.mView = notificationView;
    }

    @Override
    public void onCreate(Bundle params) {

    }

    @Override
    public void onEnterAnimEnd() {
        mView.showRefresh();
        refresh();
    }

    @Override
    protected void loadFinish(List<Notification> value, LoadType loadType) {
        if (loadType == LoadType.LOAD_TYPE_REFRESH) {
            mNotificationList.clear();
        }
        mNotificationList.addAll(value);
        mView.renderList(mNotificationList);
    }

    @Override
    protected int getDataListSize() {
        return mNotificationList.size();
    }

    @Override
    protected Observable<List<Notification>> createRequest(int pageNum, int pageSize) {
        return mNotifyInteractor.getNotifyList(pageNum, pageSize);
    }
}
