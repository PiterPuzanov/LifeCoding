package org.example.moneyTransferService;

import java.math.BigDecimal;

public class TransferService {

    public void transfer(Account from, Account to, BigDecimal amount) {

        if (from == null || to == null) {
            throw new IllegalArgumentException("Accounts must not be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account firstLock = from.getId() < to.getId() ? from : to;
        Account secondLock = from.getId() < to.getId() ? to : from;

        firstLock.getLock().lock();
        secondLock.getLock().lock();

        try {

            if (from.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient balance");
            }

            from.debit(amount);
            to.credit(amount);

        } finally {
            secondLock.getLock().unlock();
            firstLock.getLock().unlock();
        }
    }
}