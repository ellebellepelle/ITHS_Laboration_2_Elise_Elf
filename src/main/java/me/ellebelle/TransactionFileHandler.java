package me.ellebelle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransactionFileHandler {

    // skapar en logger som hör till klassen TransactionFileHandler
    // Logger.getLogger() --> hämtar en logger
    // TransactionFileHandler.class.getName() --> anger klassens namn för att se varifrån loggmeddelandena kommer
    private static final Logger logger = Logger.getLogger(TransactionFileHandler.class.getName());

    // skapar en sökväg (mapp och fil är ej skapade)
    private static final Path FILE_PATH = Path.of(
            // hämtar sökvägen till hemkatalogen - Path.of() omvandlar den till ett Path-objekt
            System.getProperty("user.home")
            // resolve() bygger vidare på sökvägen genom att lägga till mappen Budgethanteraren och filen transactions.csv
    ).resolve("Budgethanteraren", "transactions.csv");

    // metod som skapar mappen som min csv-fil sen ska ligga i (om mappen inte redan finns så skapas den)
    public static boolean createFolder() {
        try {
            // försöker skapa meppen om den inte finns
            Files.createDirectories(FILE_PATH.getParent());
            // mappen finns och kan användas
            return true;
        } catch (IOException e) {
            IO.println("Kunde inte skapa mappen: " + e.getMessage());
            logger.severe("kunde inte skapa mappen: " + e.getMessage());
            // meddelar den anropade metoden att det misslyckades
            return false;
        }
    }

    // metod som tar emot transaktioner och sparar dem i filen
    // tar emot en List<Transaction> för mitt repository har en lista så jag kan använda findAll() för att få hela
    // listan och skicka den till denna metod
    public static void saveTransactions(List<Transaction> transactions) {

        // försöker skapa mappen, om createFolder() returnerar false avbryts sparningen
        if (!createFolder()) {
            return;
        }

        try {
            //skapar filen om den inte finns, om den finns - töm den från innehåll innan nytt läggs till
            Files.writeString(FILE_PATH, "category;amount;date;type\n", // skriver rubriker till filen
                    StandardOpenOption.CREATE, // filen skapas om den inte finns redan
                    StandardOpenOption.TRUNCATE_EXISTING); // tar bort gamalt innehåll om filen redan finns
        } catch (IOException e) {
            // visar felmeddelande om filen inte kan skapas eller tömmas
            IO.println("Kunde inte förbereda filen: " + e.getMessage());
            logger.severe("Kunde inte förbereda filen: "  + e.getMessage());
            return; // avbryter sparningen om något gick fel
        }
        for (Transaction transaction : transactions) {
            String objektString = transaction.category() + ";" +
                    transaction.amount() + ";" +
                    transaction.date() + ";" +
                    transaction.transactionType() + "\n";
            try {
                //skriver transaktionen till csv-filen
                // APPEND fyller på efter hela tiden
                Files.writeString(FILE_PATH, objektString,
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                // felmeddelande om transaktion inte kan sparas
                IO.println("Kunde inte spara transaktionen: " + e.getMessage());
                logger.severe("Kunde inte spara transaktionen: "  + e.getMessage());
                return; // avbryter sparning om nått går fel
            }
        }
        logger.info("Transaktioner sparade till CSV-filen. Antal: " + transactions.size());
    }

    public static List<Transaction> loadTransactions() {
        // lista som sparar transaktionerna som hämtas från filen
        List<Transaction> transactions = new ArrayList<>();

        // om csv-filen inte finns returneras den tomma listan
        if (Files.notExists(FILE_PATH)) {
            return transactions;
        }

        try {
            // läser alla rader från csv-filen och sparar dem i en lista med String
            List<String> lines = Files.readAllLines(FILE_PATH);
            // börja på index 1 för att hoppa över rubrikraden på index 0
            for (int i = 1; i < lines.size(); i++) {
                // hämta en textrad från csv-filen
                String line = lines.get(i);
                // delar upp textraden vid varje ; och sparar delarna i en String-array
                String[] parts = line.split(";");

                // kontrollera att csv-raden innehåller exakt 4 delar
                if (parts.length !=4) {
                    IO.println("Felaktig rad i CSV-filen: " + line);
                    // använder warning här för att programmet kan hoppa över den och fortsätta
                    logger.warning("Felaktig rad i CSV-filen: " + line);
                    continue;
                }

                // fånga felaktiga värden
                try {
                    // omvandla belopp från String till double
                    double amount = Double.parseDouble(parts[1].trim());
                    // omvandla datum från String till LocalDate
                    LocalDate date = LocalDate.parse(parts[2].trim());
                    // omvandla typen till en enum-transactionType
                    TransactionType type = TransactionType.valueOf(parts[3].trim());

                    // skapa ett transaction-objekt med värderna fårn csv-raden
                    Transaction transaction = new Transaction(
                            parts[0].trim(),
                            amount,
                            date,
                            type
                    );
                    // lägg till i transaktionslistan
                    transactions.add(transaction);
                } catch (RuntimeException e) {
                    // skriver ut vilken rad som var felaktig och fortsätter med nästa
                    IO.println("Kunde inte läsa transaktionen: " + line);
                    logger.warning("Kunde inte läsa transaktionen: " + line + ". Orsak: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            IO.println("Kunde inte läsa filen:" + e.getMessage());
            logger.severe("Kunde inte läsa filen: " + e.getMessage());
        }
        logger.info("Antal inlästa transaktioner: " +  transactions.size());

        // returnerar listan med transactioner som lästs in från csv-filen
        return transactions;
    }
}
