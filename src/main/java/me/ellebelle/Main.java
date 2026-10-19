package me.ellebelle;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {

    private static final Repository<Transaction> anvandarensRepositoryTransaktionslista = new Repository<Transaction>();
    private static final BudgetService budgetService = new BudgetService(anvandarensRepositoryTransaktionslista);

    public static void main(String[] arg) {

        // läser in sparade transaktioner från csv-filen
        List<Transaction> savedTransactions = TransactionFileHandler.loadTransactions();
        // lägger in varje inläst transaktion i programmets Repository
        for (Transaction transaction : savedTransactions) {
            anvandarensRepositoryTransaktionslista.add(transaction);
        }

        boolean running = true;
        while (running) {
            IO.println("*** BUDGETHANTERAREN ***");
            IO.println("""
                    Gör ditt val:
                    1. Lägg till transaktion.
                    2. Visa transaktioner.
                    3. Visa saldo och sammanställning per kategori.
                    4. Filtrera transaktioner.
                    5. Spara till fil.
                    6. Avsluta programmet.
                    """);
            String input = IO.readln("Gör ditt val: ");

            switch (input) {
                case "1":
                    addTransaction();
                    break;
                case "2":
                    showAllTransactions();
                    break;
                case "3":
                    showSaldoAndSumOfCategory();
                    break;
                case "4":
                    filtrateTransactions();
                    break;
                case "5":
                    saveToFile();
                    break;
                case "6":
                    IO.println("Du avslutade programmet!");
                    running = false;
                    break;
            }
        }
    }

    private static void addTransaction() {
        String category = IO.readln("Vilken kategori vill du lägga till en transaktion för? ");
        double summa = Double.parseDouble(IO.readln("Vilken summa? "));
        LocalDate date = LocalDate.now();
        TransactionType transactionType = TransactionType.valueOf(IO.readln("INKOMST eller UTGIFT? "));

        Transaction transaction = new Transaction(category, summa, date, transactionType);
        anvandarensRepositoryTransaktionslista.add(transaction);

        if (transactionType == TransactionType.INKOMST) {
            IO.println("Din transaktion är tillagd. Vad duktig du är som har tjänat egna pengar. 👏🏼👏🏼");
        } else if (transactionType == TransactionType.UTGIFT) {
            IO.println("Din transaktion är tillagd. Grattis till att ha spernderat ytterligare lite 😈");
        }
    }

    private static void showAllTransactions() {
        anvandarensRepositoryTransaktionslista.findAll().forEach(System.out::println);
    }

    private static void showSaldoAndSumOfCategory() {
        IO.println(budgetService.calculateBalance());
        Map<String, Double> mapWithCategoryTotals = budgetService.calculateCategoryTotals();
        IO.println(mapWithCategoryTotals);
    }

    private static void filtrateTransactions() {
        IO.println("""
            Hur vill du filtrera transaktionerna?
            1. Visa inkomster.
            2. Visa utgifter.
            3. Visa transaktioner efter datum.
            4. Tillbaka till huvudmenyn.
            """);

        String input = IO.readln("Gör ditt val: ");

        switch (input) {
            case "1":
                budgetService.findByType(TransactionType.INKOMST)
                        .forEach(System.out::println);
                break;

            case "2":
                budgetService.findByType(TransactionType.UTGIFT)
                        .forEach(System.out::println);
                break;

            case "3":
                String dateInput = IO.readln("Ange datum (ÅÅÅÅ-MM-DD): ");
                LocalDate date = LocalDate.parse(dateInput);

                budgetService.findByDate(date)
                        .forEach(System.out::println);
                break;

            case "4":
                break;

            default:
                IO.println("Ogiltigt menyval!");
        }
    }

    private static void saveToFile() {
        // hämta alla transaktioner från repositoryt, skicka listan till metoden som sparar dem i csv-filen
        TransactionFileHandler.saveTransactions(
                anvandarensRepositoryTransaktionslista.findAll()
        );
    }
}
