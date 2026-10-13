package me.ellebelle;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetServiceTest {

    @Test
    void calculateBalanceShouldReturnZeroWhenRepositoryIsEmpty() {

        // Arrange -> förbereder ett tomt repository och ger samma repository till min BudgetService.
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);

        // Act -> kör metoden jag vill testa.
        double result = budgetService.calculateBalance();

        // Assert -> Eftersom repositoryt är tomt förväntas 0 som resultat.
        assertEquals(0, result);
    }

    @Test
    void calculateBalanceShouldAddIncomeAndSubtractExpense() {

        // Arrange
        Repository<Transaction> repository = new Repository<>();
        repository.add(new Transaction(
                "Lön",
                20000,
                LocalDate.now(),
                TransactionType.INKOMST
        ));
        repository.add(new Transaction(
                "Mat",
                1000,
                LocalDate.now(),
                TransactionType.UTGIFT
        ));

        BudgetService budgetService = new BudgetService(repository);

        // Act
        double result = budgetService.calculateBalance();

        // Assert
        assertEquals(19000, result);
    }

    @Test
    void calculateBalanceShouldReturnZeroWhenIncomeEqualsExpense() {

        Repository<Transaction> repository = new Repository<>();

        repository.add(new Transaction(
                "Lön",
                1000,
                LocalDate.now(),
                TransactionType.INKOMST
        ));
        repository.add(new Transaction(
                "Mat",
                1000,
                LocalDate.now(),
                TransactionType.UTGIFT
        ));

        BudgetService budgetService = new BudgetService(repository);

        double result = budgetService.calculateBalance();

        assertEquals(0, result);
    }
}
