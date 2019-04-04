package com.joye.cleanarchitecture.domain.model.common;

import java.util.Objects;

/**
 * 金钱抽象
 */
public class Money {
    /**
     * 金额，最小单位
     */
    private int amount;

    /**
     * 将金额转换为标准单位的比率
     * 例如分转换为元的比率是100
     */
    private int rate;

    /**
     * 货币符号
     */
    private String symbol;

    public Money(int amount, int rate, String symbol) {
        this.amount = amount;
        this.rate = rate;
        this.symbol = symbol;
    }

    public int getAmount() {
        return amount;
    }

    public int getRate() {
        return rate;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount &&
                rate == money.rate &&
                Objects.equals(symbol, money.symbol);
    }

    @Override
    public int hashCode() {

        return Objects.hash(amount, rate, symbol);
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", rate=" + rate +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
