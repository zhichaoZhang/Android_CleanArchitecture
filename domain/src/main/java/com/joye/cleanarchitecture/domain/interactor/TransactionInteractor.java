package com.joye.cleanarchitecture.domain.interactor;

import com.joye.cleanarchitecture.domain.exception.net.NetWorkException;
import com.joye.cleanarchitecture.domain.model.common.Money;
import com.joye.cleanarchitecture.domain.model.transaction.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 交易相关操作
 */
public class TransactionInteractor extends BaseInteractor {

    @Inject
    public TransactionInteractor() {
    }

    /**
     * 查询交易记录
     *
     * @param transChannel 交易渠道
     * @param month        交易月份
     * @param lastTransId  上次查询的最后一条交易id
     * @return 交易记录列表
     */
    private int maxItemCount = 50;
    private int curCount = 0;

    public Observable<List<Transaction>> queryTransactionRecords(String transChannel, String month, String lastTransId) {
        return Observable.just(new ArrayList<>());
//        if (lastTransId == null) {
//            curCount = 0;
//        }
//        if (curCount >= maxItemCount) {
//            return Observable.just(new ArrayList<>());
//        }
//        return Observable.just(20)
//                .delay(3, TimeUnit.SECONDS)
//                .map(new Function<Integer, List<Transaction>>() {
//                    @Override
//                    public List<Transaction> apply(Integer count) throws Exception {
//                        List<Transaction> transactions = new ArrayList<>(count);
//                        for (int i = 0; i < count; i++) {
//                            Transaction transaction = new Transaction(String.valueOf(++curCount), new Money(10, 100, "¥"), new Date());
//                            transactions.add(transaction);
//                        }
//                        return transactions;
//                    }
//                });
    }
}
