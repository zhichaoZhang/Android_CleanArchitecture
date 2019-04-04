package com.joye.cleanarchitecture.busi.main;

import com.joye.cleanarchitecture.app.core.mvp.view.BaseListView;
import com.joye.cleanarchitecture.domain.model.transaction.Transaction;

import java.util.List;

/**
 * 首页视图抽象
 *
 * Created by joye on 2018/8/5.
 */

public interface HomeView extends BaseListView<List<Transaction>> {
}
