package me.ellebelle;

import java.time.LocalDate;

public record Transaction(String category, double amount, LocalDate date, TransactionType transactionType) {

    // Konstruktor
    public Transaction {
        if (amount <= 0) {
            throw new InvalidTransactionException("Beloppet måste vara större än 0.");
        }
    }
}
