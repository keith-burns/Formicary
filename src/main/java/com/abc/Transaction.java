package com.abc;

import java.util.Date;

public class Transaction {
    private final double amount;
    private final Date transactionDate;

    public Transaction(double amount, Date date) {
        this.amount = amount;
        this.transactionDate = date;
        // this.transactionDate = DateProvider.getInstance().now();
    }
    
    public Date getTransactionDate() {
        return this.transactionDate;
    }
    
    public double getTransactionAmount() {
        return this.amount;
    }

}
