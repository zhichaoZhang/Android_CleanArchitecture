package com.joye.cleanarchitecture.busi.main;

import android.content.Context;
import android.os.Bundle;

import com.joye.cleanarchitecture.app.core.mvp.presenter.BaseListPresenter;
import com.joye.cleanarchitecture.app.core.mvp.presenter.PagingByLastDataIdListPresenter;
import com.joye.cleanarchitecture.domain.interactor.TransactionInteractor;
import com.joye.cleanarchitecture.domain.interactor.UserInteractor;
import com.joye.cleanarchitecture.domain.model.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * 首页视图逻辑
 * <p>
 * Created by joye on 2018/8/5.
 */

public class HomePresenter extends PagingByLastDataIdListPresenter<HomeView, List<Transaction>> {
    /**
     * 交易列表
     */
    private List<Transaction> mTransList = new ArrayList<>();
    private UserInteractor userInteractor;
    private TransactionInteractor mTransInteractor;

    @Inject
    public HomePresenter(Context context, HomeView homeView, UserInteractor userInteractor, TransactionInteractor transactionInteractor) {
        super(context);
        this.userInteractor = userInteractor;
        this.mTransInteractor = transactionInteractor;
        this.mView = homeView;
    }

    @Override
    public void onCreate(Bundle params) {
//        refresh();
    }

    @Override
    public void onEnterAnimEnd() {
        mView.showRefresh();
        refresh();
    }

    @Override
    protected Observable<List<Transaction>> createRequest(String lastDataId) {
        return mTransInteractor.queryTransactionRecords("", "", lastDataId);
    }

    @Override
    protected String getLastDataId() {
        int dataSize = mTransList.size();
        if (dataSize == 0) {
            return null;
        }
        return mTransList.get(dataSize - 1).getId();
    }

    @Override
    protected void loadFinish(List<Transaction> value, BaseListPresenter.LoadType loadType) {
        if (LoadType.LOAD_TYPE_REFRESH == loadType) {
            mTransList.clear();
        }
        mTransList.addAll(value);
        mView.renderList(mTransList);
    }

    @Override
    protected int getDataListSize() {
        return mTransList.size();
    }
}
