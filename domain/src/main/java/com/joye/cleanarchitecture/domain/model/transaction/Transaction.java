package com.joye.cleanarchitecture.domain.model.transaction;

import com.joye.cleanarchitecture.domain.model.common.Money;

import java.util.Date;

/**
 * 交易抽象
 */
public class Transaction {
    /**
     * 交易id
     */
    private String id;

    /**
     * 实收金额
     */
    private Money receivedMoney;

    /**
     * 应收金额
     */
    private Money payableMoney;

    /**
     * 交易时间
     */
    private Date transactionDate;

    public Transaction(String id, Money receivedMoney, Date transactionDate) {
        this.id = id;
        this.receivedMoney = receivedMoney;
        this.transactionDate = transactionDate;
    }

    public String getId() {
        return id;
    }

    public Money getReceivedMoney() {
        return receivedMoney;
    }

    public Money getPayableMoney() {
        return payableMoney;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }
}
