package me.ellebelle;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionTest {

    @Test
    void shouldThrowEaceptionWhenAmountIsZero() {

        assertThrows(InvalidTransactionException.class, () -> {
            new Transaction(
                    "Mat",
                    0,
                    LocalDate.now(),
                    TransactionType.UTGIFT
            );
        });
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {

        assertThrows(InvalidTransactionException.class, () -> {
            new Transaction(
                    "Mat",
                    -500,
                    LocalDate.now(),
                    TransactionType.UTGIFT
            );
        });
    }

    @Test
    void shouldCreateTransactionWhenAmountIsPositive() {

        Transaction transaction = new Transaction(
                "Mat",
                500,
                LocalDate.now(),
                TransactionType.UTGIFT
        );

        assertEquals(500, transaction.amount());
    }
}
