package me.ellebelle;

import java.time.LocalDate;

public record Transaction(String category, double amount, LocalDate date, TransactionType transaktionType) {
}
