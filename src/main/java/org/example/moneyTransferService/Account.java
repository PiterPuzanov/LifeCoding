package org.example.moneyTransferService;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final long id;
    private BigDecimal balance;
    private final ReentrantLock lock = new ReentrantLock();

    public Account(long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public ReentrantLock getLock() {
        return lock;
    }
}