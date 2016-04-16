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

    public void deposit(double interestAmount, Date date) {
        if (interestAmount <= 0) {
            throw new IllegalArgumentException("interestAmount must be greater than zero");
        } else {
            transactions.add(new Transaction(interestAmount, date));
        }
    }

    // TODO: check that user has enough money to withdraw
    public void withdraw(double interestAmount, Date date) {
        if (interestAmount <= 0) {
            throw new IllegalArgumentException("interestAmount must be greater than zero");
        } else {
            transactions.add(new Transaction(-interestAmount, date));
        }
    }

    /* TODO: add security to determine if person can make transfer 
    (security -> can user deposit into any bank account, or just one he owns?) */
    public void transfer(double amount, Account to, Date date) {
        if (amount <= 0 ) {
            throw new IllegalArgumentException("interestAmount must be greater than zero");
        }
        else {
            if (to != null) {
                this.withdraw(amount, date); // withdraw from this account
                to.deposit(amount, date); //deposit into other account
            }
            else {
                throw new IllegalArgumentException("Recipient account does not exist"); // TODO: test this
            }
        }
    }

    public double interestEarned() {
        
        // double interestAmount = sumTransactions();
        double interestAmount;
        double sumTransactionsToDate;
        double accountTotal;
        double interestRate;
        double daysPerYear = 365.24;
        Date previousTransactionDate;
        Date currentTransactionDate;
        int numDaysBetween;
        
        switch(accountType){
            case SAVINGS:

                interestAmount = 0.0;
                accountTotal = 0.0;
                sumTransactionsToDate = 0.0;
                if (transactionsExist()) {
                    
                    previousTransactionDate = null;
                    
                    for (Transaction t: this.transactions) {
                        
                        currentTransactionDate = t.getTransactionDate();
                        
                        if (previousTransactionDate != null) { // if it is null then this is the first transaction in the account                      
                            numDaysBetween = getNumberOfDaysBetween(previousTransactionDate, currentTransactionDate);
                            
                            // calc the current bank interestAmount * interest rate
                            if (accountTotal <= 1000) {
                                interestRate = 0.001;
                                interestAmount = calcCompoundInterest(accountTotal, interestRate, daysPerYear, numDaysBetween) - sumTransactionsToDate; 
                            }
                            else {
                                interestRate = 0.001;
                                double bigInterestRate = 0.002;
                                interestAmount = calcCompoundInterest(1000, interestRate, daysPerYear, numDaysBetween) + calcCompoundInterest(accountTotal-1000, bigInterestRate, daysPerYear, numDaysBetween) - sumTransactionsToDate;
                            }
                        }
                        sumTransactionsToDate += t.getTransactionAmount();
                        previousTransactionDate = currentTransactionDate;
                        accountTotal = sumTransactionsToDate + interestAmount;
                    }       
                }
                return interestAmount;
                
            case MAXI_SAVINGS:
                interestAmount = 0.0;
                accountTotal = 0.0;
                sumTransactionsToDate = 0.0;
                
                Date lastWithdrawalDate = null;
                
                if (transactionsExist()) {
                    
                    previousTransactionDate = null;
                    
                    for (Transaction t: this.transactions) {
                        currentTransactionDate = t.getTransactionDate();
                        
                        if (previousTransactionDate != null) { // if it is null then this is the first transaction in the account
                            numDaysBetween = getNumberOfDaysBetween(previousTransactionDate, currentTransactionDate);

                            interestRate = 0.001;
                            //System.out.println(t.getTransactionAmount() + ", " + currentTransactionDate + ", " + lastWithdrawalDate);
                            if (recentWithdrawal(currentTransactionDate, lastWithdrawalDate) == false) { // check if there has been a withdrawal in the past ten days
                                interestRate = 0.05;
                            }
                            
                            interestAmount = calcCompoundInterest(accountTotal, interestRate, daysPerYear, numDaysBetween) - sumTransactionsToDate;
                            //System.out.println("Current transaction: " + t.getTransactionAmount() + ", accountTotal : " + accountTotal);
                            //System.out.println("Interest amount: " + interestAmount);
                        }
                        
                        if (t.getTransactionAmount() <= 0) {
                            lastWithdrawalDate = t.getTransactionDate();
                        }
                        
                        sumTransactionsToDate += t.getTransactionAmount();
                        previousTransactionDate = currentTransactionDate;
                        accountTotal = sumTransactionsToDate + interestAmount;
                    }
                }
                //System.out.println(interestAmount + ", " + accountTotal);
                return interestAmount;
                
            default:
                interestAmount = 0.0;
                sumTransactionsToDate = 0.0;
                accountTotal = 0.0;
                
                if (transactionsExist()) {
                    
                    previousTransactionDate = null;
                    
                    for (Transaction t: this.transactions) {
                    
                        currentTransactionDate = t.getTransactionDate();

                        if (previousTransactionDate != null) { // if it is null then this is the first transaction in the account

                            numDaysBetween = getNumberOfDaysBetween(previousTransactionDate, currentTransactionDate);
                            interestRate = 0.001;
                            interestAmount = calcCompoundInterest(accountTotal, interestRate, daysPerYear, numDaysBetween) - sumTransactionsToDate;
                        }
                        sumTransactionsToDate += t.getTransactionAmount();
                        previousTransactionDate = currentTransactionDate;
                        accountTotal = sumTransactionsToDate + interestAmount;
                    }
                }
                return interestAmount;
        }
    }
    
    /* TODO: determine whether withdrawal on this account occurred within past ten days
    */
    public boolean recentWithdrawal(Date currentDate, Date lastWithdrawalDate) {
        
        if (lastWithdrawalDate != null) {
            int numDays = 10; // variable to declare the last number of days to check for withdrawal
            return getNumberOfDaysBetween(lastWithdrawalDate, currentDate) < numDays;
        }
        else {
            return false;
        }
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
        double interestAmount = 0.0;
        for (Transaction t: this.transactions)
            interestAmount += t.getTransactionAmount();
        return interestAmount;
    }

    public int getAccountType() {
        return accountType;
    }
    
    public double calcCompoundInterest(double principal, double interestRate, double daysPerYear, int numDaysBetween) {
        
        double amount;
        amount = principal * Math.pow((1 + interestRate/daysPerYear), daysPerYear*(numDaysBetween/daysPerYear));
        System.out.println(amount + ", " + principal + ", " + interestRate + ", " + daysPerYear + ", " + numDaysBetween);
        return amount;
    }
}
