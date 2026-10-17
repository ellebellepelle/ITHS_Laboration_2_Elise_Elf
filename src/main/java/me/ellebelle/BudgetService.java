package me.ellebelle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class BudgetService {

    private final Repository<Transaction> repository;

    // Konstruktor som tar emot befintligt repository, så att BudgetService kan komma åt samma Repository<Transaction>
    // som resten av programmet använder.
    public BudgetService(Repository<Transaction> repository) {
        this.repository = repository;
    }

//    // Beräkningsmetod för total balans.
//    public double calculateBalance() {
//        double balance = 0;
//
//        // repository.findAll() --> hämtar alla transaktioner som finns i repositoryt.
//        // forEach-loopen går igenom dem en i taget och lägger den aktuella transaktionen i variablen transaction.
//        for (Transaction transaction : repository.findAll()) {
//            // är TransactionType INKOMST läggs den till
//            if (transaction.transactionType() == TransactionType.INKOMST) {
//                balance += transaction.amount();
//            }
//            // är TransactionType UTGIFT dras den bort
//            else if (transaction.transactionType() == TransactionType.UTGIFT) {
//                balance -= transaction.amount();
//            }
//        }
//        return balance;
//    }

    // Beräkningsmetod för total balans med Stream API - uppdelad med separata variabler för att se vad som händer.
    public double calculateBalance() {
        // Hämtar alla transaktioner från Repository och skapar et Stream<Transaction>
        Stream<Transaction> transactions = repository.findAll().stream();

        // Går igenom varje Transaction och tar fram ett double-värde.
        // Inkomster blir positiva och utgifter blir negativa.
        // mapToDouble() returnerar en DoubleStream.
        DoubleStream amounts = transactions.mapToDouble(transaction -> {
            if (transaction.transactionType() == TransactionType.INKOMST) {
                return transaction.amount();
            }
            else {
                return -transaction.amount();
            }
        });

        // Sumerar alla tal i DoubleStream och sparar resultatet i balance
        double balance = amounts.sum();

        // Returnerar det färdiga saldot
        return balance;
    }

//    public Map<String, Double> calculateCategoryTotals() {
//
//        // skapa en tom Map som ska innehålla kategori -> totalsumma
//        Map<String, Double> categoryTotals = new HashMap<>();
//
//        // för varje transaktion som finns i repositoryt..
//        for (Transaction transaction : repository.findAll()) {
//            // spara den kategorin (m små bokstäver) i category
//            String category = transaction.category().toLowerCase();
//            // currentTotal -> det som redan finns för kategorin i Map-en
//            // getOrDefault -> om kategorin inte finns ännu används 0.0
//            double currentTotal = categoryTotals.getOrDefault(category, 0.0);
//            // transaction.amount() -> det nya beloppet
//            // lägger ihop tidigare summa med det nya beloppet
//            double newTotal = currentTotal + transaction.amount();
//            // sparar kategorin och den nya totalsumman i Map-en
//            categoryTotals.put(category, newTotal);
//        }
//        // returnerar Map-en med alla kategorier och deras totalsummor
//        return categoryTotals;
//    }

    // Beräkna totalsumman för varje kategori med Stream API
    public Map<String, Double> calculateCategoryTotals() {

        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.category().toLowerCase(),
                        Collectors.summingDouble(transaction -> transaction.amount())
                ));
    }

    // Hittar alla med samma TransactionType
    public List<Transaction> findByType(TransactionType type) {
        return repository.findWhere(
                // Kommande Lambda-uttryck består av två delar:
                // transaction --> är lambdans egen parameter, den representerar den transaktion som ska undersökas
                // transaction.transactionType() == type --> är konstrollen som ger true eller false
                // Hela Lambdan motsvarar ett Predicate<Transaction> och tas emot av parametern condiction i findWhere()
                transaction -> transaction.transactionType() == type
        );
    }
}
