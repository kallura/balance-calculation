package org.task.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BalanceStatistic {

    private int customerId;
    private BigDecimal balance;
    private Timestamp time;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
