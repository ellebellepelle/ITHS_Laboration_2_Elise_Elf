package me.ellebelle;

import java.time.LocalDate;

public class Main {

    private static final Repository<Transaction> anvandarensRepositoryTransaktionslista = new Repository<Transaction>();

    public static void main(String[] arg) {
        boolean running = true;
        while (running) {
            IO.println("*** BUDGETHANTERAREN ***");
            IO.println("""
                   Gör ditt val:
                   1. Lägg till transaktion.
                   2. Visa transaktioner.
                   3. Avsluta programmet.
                   """);
            String input = IO.readln("Gör ditt val: ");

            switch (input) {
                case "1": addTransaction();
                break;
                case "2": showAllTransactions();
                break;
                case "3": IO.println("Du avslutade programmet!");
                running = false;
                break;
            }
        }
    }

    private static void addTransaction() {
        String category = IO.readln("Vilken kategori vill du lägga till en transaktion för?");
        double summa = Double.parseDouble(IO.readln("Vilken summa?"));
        LocalDate date = LocalDate.now();
        TransactionType transactionType = TransactionType.valueOf(IO.readln("INKOMST eller UTGIFT?"));

        Transaction transaction = new Transaction(category, summa, date, transactionType);
        anvandarensRepositoryTransaktionslista.add(transaction);

        if (transactionType == TransactionType.INKOMST) {
            IO.println("Din transaktion är tillagd. Vad duktig du är som har tjänat egna pengar. 👏🏼👏🏼");
        }
        else if (transactionType == TransactionType.UTGIFT) {
            IO.println("Din transaktion är tillagd. Grattis till att ha spernderat ytterligare lite 😈");
        }
    }

    private static void showAllTransactions() {
        anvandarensRepositoryTransaktionslista.findAll().forEach(System.out::println);
    }
}
