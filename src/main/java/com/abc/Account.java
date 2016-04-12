package com.abc;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Account {

    public static final int CHECKING = 0;
    public static final int SAVINGS = 1;
    public static final int MAXI_SAVINGS = 2;

    private final int accountType;
    public List<Transaction> transactions;

    public Account(int accountType) {
        this.accountType = accountType;
        this.transactions = new ArrayList<Transaction>();
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            transactions.add(new Transaction(amount));
        }
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            transactions.add(new Transaction(-amount));
        }
    }

    /* TODO: add security to determine if person can make transfer 
    (security -> can user deposit into any bank account, or just one he owns?) */
    public void transfer(double amount, Account to) {
        if (amount <= 0 ) {
            throw new IllegalArgumentException("amount must be greater than zero");
        }
        else {
            if (to != null) {
                this.withdraw(amount); // withdraw from this account
                to.deposit(amount); //deposit into other account
            }
            else {
                throw new IllegalArgumentException("Recipient account does not exist"); // TODO: test this
            }
        }
    }

    public double interestEarned() {
        
        // double amount = sumTransactions();
        double amount = 0.0;
        double sumTransactionsToDate = 0.0;
        double interestRate;
        double daysPerYear = 365.24;
        Date previousTransactionDate;
        Date currentTransactionDate;
        int numDaysBetween;
        
        switch(accountType){
            case SAVINGS:
                if (transactionsExist()) {
                    previousTransactionDate = null;
                    amount = 0.0;
                    for (Transaction t: this.transactions) {
                    
                        sumTransactionsToDate += t.getTransactionAmount();
                        currentTransactionDate = t.getTransactionDate();
                        System.out.println(t.getTransactionAmount() + " , " + t.getTransactionDate());
                        System.out.println(t);
                        
                        if (previousTransactionDate != null) { // if it is null then this is the first transaction in the account
                            amount += sumTransactionsToDate;                           
                            numDaysBetween = getNumberOfDaysBetween(previousTransactionDate, currentTransactionDate);

                            // calc the current bank amount * interest rate
                            if (amount <= 1000) {
                                interestRate = 0.001 / daysPerYear;
                                amount = amount * interestRate * numDaysBetween;
                            }
                            else {
                                interestRate = 0.001 / daysPerYear;
                                double bigInterestRate = 0.002 / daysPerYear;
                                amount = (interestRate * numDaysBetween * 1000) + (amount-1000) * bigInterestRate * numDaysBetween;
                            }
                        }
                        previousTransactionDate = currentTransactionDate;
                    }       
                }
                
            case MAXI_SAVINGS:
                if (transactionsExist()) {
                    previousTransactionDate = null;
                    amount = 0.0;
                    for (Transaction t: this.transactions) {
                        
                        sumTransactionsToDate += t.getTransactionAmount();
                        currentTransactionDate = t.getTransactionDate();

                        if (previousTransactionDate != null) { // if it is null then this is the first transaction in the account
                            numDaysBetween = getNumberOfDaysBetween(previousTransactionDate, currentTransactionDate);
                            amount += sumTransactionsToDate;
                            interestRate = 0.001 / daysPerYear;
                            if (recentWithdrawal(this) == false) { // check if there has been a withdrawal in the past ten days
                                interestRate = 0.05 / daysPerYear;
                            }
                            amount = amount * interestRate * numDaysBetween;
                        }
                        previousTransactionDate = currentTransactionDate;  
                    }
                }
                
            default:
                if (transactionsExist()) {
                    previousTransactionDate = null;
                    amount = 0.0;
                    for (Transaction t: this.transactions) {
                    
                        sumTransactionsToDate += t.getTransactionAmount();
                        currentTransactionDate = t.getTransactionDate();

                        if (previousTransactionDate != null) { // if it is null then this is the first transaction in the account
                            amount += sumTransactionsToDate;
                            numDaysBetween = getNumberOfDaysBetween(previousTransactionDate, currentTransactionDate);
                            interestRate = 0.001 / daysPerYear;
                            amount = amount * interestRate * numDaysBetween;
                        }
                        previousTransactionDate = currentTransactionDate;  
                    }
                } 
        }
        return amount;
    }
    
    /* TODO: determine whether withdrawal on this account occurred within past ten days
    */
    public boolean recentWithdrawal(Account account) {
        DateProvider today = new DateProvider();
        if (transactionsExist()) {
            for (Transaction t: this.transactions) {
                if (t.getTransactionAmount() <= 0 ) {
                    
                    Date todayDate = today.now();
                    int numDays = 10; // variable to declare the last number of days to check for withdrawal
                    return getNumberOfDaysBetween(t.getTransactionDate(), todayDate) < numDays;
                }
            }
        }
        else {
            return false;
        }
        return false; // TODO: fix this, I think it makes recentWithdrawal always return false
    }
    
    public int getNumberOfDaysBetween(Date date1, Date date2) {
        long numDaysLong;
        int numDays;
        
        numDaysLong = (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24); // this does not properly account for irregular time changes ie. daylight savings time
        numDays = (int) numDaysLong;
        
        return numDays;
    }

    public boolean transactionsExist() {
        return this.transactions != null;
    }
    
    // TODO: check public/private of this
    public double sumTransactions() {
        double amount = 0.0;
        for (Transaction t: this.transactions)
            amount += t.getTransactionAmount();
        return amount;
    }

    public int getAccountType() {
        return accountType;
    }

}
