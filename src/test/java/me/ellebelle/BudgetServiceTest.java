package me.ellebelle;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void calculateCategoryTotalsShouldReturnEmptyMapWhenRepositoryIsEmpty() {
        // Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);

        // Act
        Map<String, Double> result = budgetService.calculateCategoryTotals();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void calculateCategoryTotalsShouldAddAmountsFromSameCategory() {
        // Arrange
        Repository<Transaction> repository = new Repository<>();

        repository.add(new Transaction(
                "Mat",
                500,
                LocalDate.now(),
                TransactionType.UTGIFT
        ));

        repository.add(new Transaction(
                "Mat",
                200,
                LocalDate.now(),
                TransactionType.UTGIFT
        ));

        BudgetService budgetService = new BudgetService(repository);

        // Act
        Map<String, Double> result = budgetService.calculateCategoryTotals();

        // Assert
        assertEquals(700, result.get("mat"));
    }

    @Test
    void calculateCategoryTotalsShouldHandleMultipleCategories() {
        // Arrange
        Repository<Transaction> repository = new Repository<>();

        repository.add(new Transaction("Mat", 500, LocalDate.now(), TransactionType.UTGIFT));
        repository.add(new Transaction("Bensin", 300, LocalDate.now(), TransactionType.UTGIFT));
        repository.add(new Transaction("Mat", 200, LocalDate.now(), TransactionType.UTGIFT));
        repository.add(new Transaction("Hyra", 8000, LocalDate.now(), TransactionType.UTGIFT));

        BudgetService budgetService = new BudgetService(repository);

        // Act
        Map<String, Double> result = budgetService.calculateCategoryTotals();

        // Assert
        assertEquals(700, result.get("mat"));
        assertEquals(300, result.get("bensin"));
        assertEquals(8000, result.get("hyra"));
        assertEquals(3, result.size());
    }
}
