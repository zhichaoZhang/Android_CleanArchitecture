package com.joye.cleanarchitecture.domain.interactor;

import com.joye.cleanarchitecture.domain.model.message.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 通知相关业务
 */
public class NotifyInteractor extends BaseInteractor {

    @Inject
    public NotifyInteractor() {

    }

    private int mCurPageNum;

    public Observable<List<Notification>> getNotifyList(int pageNum, int pageSize) {
        mCurPageNum = pageNum;

        if(mCurPageNum == 2) {
            return Observable.just(1).delay(3, TimeUnit.SECONDS).map(integer -> {
                throw new Exception("Maybe some error happened.");
            });
        }

        if(mCurPageNum == 5) {
            return Observable.just(pageNum)
                    .delay(3, TimeUnit.SECONDS)
                    .map(integer -> new ArrayList<>());
        }

        return Observable.just(pageSize)
                .delay(3, TimeUnit.SECONDS)
                .map(integer -> {
                    List<Notification> notifications = new ArrayList<>();
                    for (int i = 0; i < integer; i++) {
                        Notification notification = new Notification(String.valueOf((pageNum - 1) * pageSize + i), new Date());
                        notifications.add(notification);
                    }
                    return notifications;
                });
    }

}
