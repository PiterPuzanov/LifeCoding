package moneyTransferService;

import org.example.moneyTransferService.Account;
import org.example.moneyTransferService.TransferService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest {

    private final TransferService transferService = new TransferService();

    @Test
    void shouldTransferMoneySuccessfully() {
        Account from = new Account(1, new BigDecimal("100"));
        Account to = new Account(2, new BigDecimal("50"));

        transferService.transfer(from, to, new BigDecimal("30"));

        assertEquals(new BigDecimal("70"), from.getBalance());
        assertEquals(new BigDecimal("80"), to.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance() {
        Account from = new Account(1, new BigDecimal("20"));
        Account to = new Account(2, new BigDecimal("50"));

        assertThrows(IllegalStateException.class,
                () -> transferService.transfer(from, to, new BigDecimal("30")));
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {
        Account from = new Account(1, new BigDecimal("100"));
        Account to = new Account(2, new BigDecimal("50"));

        assertThrows(IllegalArgumentException.class,
                () -> transferService.transfer(from, to, new BigDecimal("-10")));
    }

    @Test
    void shouldHandleConcurrentTransfersCorrectly() throws InterruptedException {
        Account from = new Account(1, new BigDecimal("1000"));
        Account to = new Account(2, new BigDecimal("0"));

        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    transferService.transfer(from, to, new BigDecimal("10"));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(new BigDecimal("900"), from.getBalance());
        assertEquals(new BigDecimal("100"), to.getBalance());
    }
}