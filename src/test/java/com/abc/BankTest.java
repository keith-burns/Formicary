package com.abc;

import org.junit.Test;
import java.util.Date;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class BankTest {
    private static final double DOUBLE_DELTA = 1e-15;

    
    // TODO: add test for multiple accounts
    @Test
    public void customerSummary() {
        Bank bank = new Bank();
        Customer john = new Customer("John");
        john.openAccount(new Account(Account.CHECKING));
        bank.addCustomer(john);

        assertEquals("Customer Summary\n - John (1 account)", bank.customerSummary());
    }

    
    // TODO: add multiple transactions: deposits, withdrawals, transfers
    @Test
    public void checkingAccount() {
        Bank bank = new Bank();
        Account checkingAccount = new Account(Account.CHECKING);
        Customer bill = new Customer("Bill").openAccount(checkingAccount);
        bank.addCustomer(bill);
        
        Date firstDeposit;
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateProvider.getInstance().now());
        cal.add(Calendar.DATE, -730);
        firstDeposit = cal.getTime();
        
        Date transferToSavingsDate;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(DateProvider.getInstance().now());
        cal1.add(Calendar.DATE, -365);
        transferToSavingsDate = cal1.getTime();

        checkingAccount.deposit(5000.0, firstDeposit);
        checkingAccount.deposit(1.0, DateProvider.getInstance().now());

        assertEquals(10.003408789360037, bank.totalInterestPaid(), DOUBLE_DELTA);
    }

    
    // TODO: add multiple transactions: deposits, withdrawals, transfers
    @Test
    public void savings_account_over_1000() {
        Bank bank = new Bank();
        Account savingsAccount = new Account(Account.SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(savingsAccount));

        Date firstDeposit;
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateProvider.getInstance().now());
        cal.add(Calendar.DATE, -365);
        firstDeposit = cal.getTime();
        
        savingsAccount.deposit(1500.0, firstDeposit);
        savingsAccount.deposit(1.0, DateProvider.getInstance().now());

        assertEquals(2.000180546028787, bank.totalInterestPaid(), DOUBLE_DELTA);
    }
    
    @Test
    public void savings_account_under_1000() {
        Bank bank = new Bank();
        Account savingsAccount = new Account(Account.SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(savingsAccount));

        
        savingsAccount.deposit(500.0, DateProvider.getInstance().now());

        assertEquals(0.0, bank.totalInterestPaid(), DOUBLE_DELTA);
    }

    
    // TODO: add multiple transactions: deposits, withdrawals, transfers
    // TODO: add withdrawals more than 10 days apart
    // TODO: rename these shitty variables and refactor
    @Test
    public void maxi_savings_account() {
        Bank bank = new Bank();
        Account maxi_savingsAccount = new Account(Account.MAXI_SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(maxi_savingsAccount));

        Date recentWithdrawal;
        Date oldWithdrawal;
        Date firstDeposit;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        cal1.setTime(DateProvider.getInstance().now());
        cal2.setTime(DateProvider.getInstance().now());
        cal3.setTime(DateProvider.getInstance().now());
        cal1.add(Calendar.DATE, -9);
        cal2.add(Calendar.DATE, -11);
        cal3.add(Calendar.DATE, -1461);
        recentWithdrawal = cal1.getTime();
        oldWithdrawal = cal2.getTime();
        firstDeposit = cal3.getTime();
        
        maxi_savingsAccount.deposit(3000.0, firstDeposit);
        maxi_savingsAccount.withdraw(200.0, oldWithdrawal);
        maxi_savingsAccount.withdraw(200.0, recentWithdrawal);
        maxi_savingsAccount.deposit(500.0, DateProvider.getInstance().now());

        assertEquals(658.7642123470669, bank.totalInterestPaid(), DOUBLE_DELTA);
    }
}