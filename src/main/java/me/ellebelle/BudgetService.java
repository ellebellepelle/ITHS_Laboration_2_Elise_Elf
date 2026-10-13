package me.ellebelle;

public class BudgetService {

    private final Repository<Transaction> repository;

    // Konstruktor som tar emot befintligt repository, så att BudgetService kan komma åt samma Repository<Transaction>
    // som resten av programmet använder.
    public BudgetService(Repository<Transaction> repository) {
        this.repository = repository;
    }

    // Beräkningsmetod för total balans.
    public double calculateBalance() {
        double balance = 0;

        // repository.findAll() --> hämtar alla transaktioner som finns i repositoryt.
        // forEach-loopen går igenom dem en i taget och lägger den aktuella transaktionen i variablen transaction.
        for (Transaction transaction : repository.findAll()) {
            // är TransactionType INKOMST läggs den till
            if (transaction.transactionType() == TransactionType.INKOMST) {
                balance += transaction.amount();
            }
            // är TransactionType UTGIFT dras den bort
            else if (transaction.transactionType() == TransactionType.UTGIFT) {
                balance -= transaction.amount();
            }
        }
        return balance;
    }
}
