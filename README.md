BUDGETHANTERAREN.

Beskrivning:
Programmet låter användaren registrera transaktioner i form av inkomster eller utgifter. Annvändaren väljer själv vilken 
kategori transaktionen skall tillhör samt vilket belopp det gäller, programmet sätter dagens datum automatiskt.
Vidare kan användaren välja att se sina registrerade transaktioner, visa sitt aktuella saldo, se sina sammanställningar
per kategori, filtrera transaktioner genom inkomster, utgifter eller datum. När användaren har registrerat sina 
transaktioner kan hen välja att spara dessa till en fil som automatiskt skapas i hens hemhatalog i sin dator. Denna fil
öppnas också automatiskt till programmet när det startas så att alla transaktioner finns kvar. 

Teknik:
Programmet använder:
* Collections Framework i form av List och Map. 
* Generics (i både klass och metoder) för att kunna återanvända Repository<T> klassen (samt dess metoder) till flera olika 
typer. (Detta utnyttjas dock inte i programmet då det enbart arbetar med Transaction.)
Om mitt program även hade haft användare skulle jag först behöva ha någon typ som bekriver en användare, ex en
User-klsas eller en User-record. Den kanske hade innehållit information som namn och användar ID. Jag skulle då kunna
skapa ett annat Repository där T istället är User. Det Repositoryt skulle ha sin egen ArrayList som bara innehåller
User-objket. Jag har alltså inte en anda ArrayList där Uers och Transactions balndas. Jag har två olika Repositor-
objekt som båda bygger på samma generiska Repository<T>-klass, men där T har fått olika betydelse.
* Undantagshantering med try/catch för att fånga exceptions (både Javas egna samt ett eget som jag har skapat).
* Filhantering som gör att datan kan sparas till fil.
* Enhetstester med JUnit för att kunna kontrollera delar av koden utan att behöva köra hela programmet. (Detta utnytjas 
på ett bra sätt i början av kodningen när metoder skrivs och det ännu inte finns ett färdigt program samt att jag 
slipper skriva ut felmeddelanden i konsollen).
* Debugger och Loggning för att programmeraren skall kunna felsöka.
* Stream API för att förenkla koden och tex kunna använda metoder ist för att gå igenom kod med loopar.
I BudgetService-klassen finns exempel på kod som först är skriven utan Stream API där en Map görs manuellt och sedan 
används for-loop och metoder för att lösa uppgiften. (den är sedan utkommenterad)
Stream API ger kortare kod och tydligare vad som skall beräknas.

Köra programmet:
Programmet är skrivet i Java och använder Maven.
För att köra programmet öppnar man projektet i IntelliJ IDEA och ser till att rätt JDK är installerat (jag har använt
JDK 25, detta är inte den senaste men den har stöd).
Öppna Main.java som ligger i src/main/java/me/ellebelle och starta programmet med den gröna Run-pilen vid main()-metoden
(current file kan användas)
Programmet startar i konsollen och visar menyn där användaren kan göra sitt val.
När programmet startar läses eventuella tidigare sparade transaktioner automatiskt in från CSV-filen som ligger i mappen
Budgethanteraren i användarens hemkatalog. Om filen inte finns startar programmet med en tom transaktionslista.
För att spara sina transaktioner väljer användaren alternativ 5 i menyn och mappen skapas med CSV-filen automatiskt om 
den inte finns.

Testning, Loggning och felsökning:
Programmet använder loggning i form av Logger med nivån WARNING, INFO och SEVERE (ERROR). INFO --> när transaktioner 
läggs till, sparas eller läses in. WARNING --> vid felaktig användarinmatning och felaktiga CSV-rader. SEVERE -->
vid filfel som hindrar inläsning eller sparning.
*******************************************************************************************************************

* Provkörde min meny och fick ett exception (ett runtime exception):
  Exception in thread "main" java.lang.IllegalArgumentException: No enum constant me.ellebelle.TransactionType.inkomst...
* Meddelandet förklarar att jag inte har en enum som heter inkomst. Jag debuggar Main.java rad 37 för där finns kod som
  jag själv har skrivit.
* När jag debuggade och kom till rad 37 valde jag Step Over och då kördes hela raden eftersom den innehåller både
  IO.readln() och valueOf() och då hann programmet läsa in inkomst, försöka omvandla texten och krascha. Så därför
  gör jag om debugen igen och väljer Step Into ist. Jag får nu välja vilken metod jag vill gå in i (eftersom det finns
  flera). Jag väljer valueOf() och innan debuggen kan köra den raden måste Java först utföra IO.readln() för att få
  texten som skall skickas in så jag skriver inkomst. Nu kommer jag vidare och intelliJ öppnar TransactionType.java
  för att metoden valueOf() fick texten "inkomst" som argument men samtidigt ser jag att mitt enum kan bara ta emot
  INKOMST och UTGIFT. Jag har alltså hittat orsaken till felet med hjälp av debuggern: valueOf() försöker hitta inkomst
  men det värdet finns inte i min enum eftersom java är casesensitive.
* EV lägg senare till kontroll för användarinmatning så det bara går skriva INKOMST eller UTGIFT, ev gör ett menyval
  där användaren får välja mellan de två alternativen.
* Hantera felet (åtgärdades senare i addTransaction()).
    * La till .trim().toUpperCase() på användarens inmatning innan den skickas till valueOf().
      .trim() --> tar bort mellansag
      .toUpperCase() --> omvandla till stora bokstäver
    * Om anv skriver något som inte finns i min enum (ex hej) kommer valueOf() fortfarande att kasta IllegalArgumentException
      la därför till try/catch runt valueOf() så om anv skriver ogilltig transaktionstyp fångar catchen IllegalArgumentException
      det och skriver felmeddelande. Sen avslutas addTransaction() med return och anv kommer till huvudmenyn ist för krasch.

*******************************************************************************************************************

* Felhantering i addTransaction() med TransactionType INKOMST UTGIFT, samt summan ska va double.
* Provkör programmet och skrev fel, skrev -100 när jag la till en transaktion fast att alla summor ska va plus.
  fick exception InvalidTransactionException.
    * Satte brakepoint i Transaction-recordet på raden där jag kastar ett exception om summan är 0 eller lägre, kör debug.
    * Måste skriva in hela transaktionen i konsollen för att programmet måste få alla uppgifter innan det försöker skapa
      ett Transaktionsobjekt.
    * När det är inskrivet stannar programmet på min brakepoint i Transaction.java
      I fönstret Threads & Variables som visas ser jag vädrena jag skrivit in som skickats till konstruktorn. Där syns att
      amount = -100.0, och de andra väderna är rätt. Det står "Object is being initialized" som betyder att Java håller på
      att skapa Transaktionsobjektet. Eftersom beloppet är mindre än eller lika med 0, uppfylls villkoret amount <= 0, som
      finns i Transactions-recordet och programmet kommer därför att kasta mitt egna InvalidTransactionException jag har i
      min Transaktion-record och som jag har gjort en egen klass för --> InvalidTransactionException.
    * Jag väljer "Strp in to" i debuggen på raden:
      throw new InvalidTransactionException("Beloppet måste vara större än 0.");
      Då går debuggen från Transaction-recordet till konstruktorn i min egen InvalidTransactionException-klass på rad 6.
      Det gör den för att Java först måste skapa ett objekt av InvalidTransactionException innan det kastas med throw.
    * Till vänster i Threds & Variables:
      <init>:6, InvalidTransactionException (me.ellebelle)
      anropskedjan: Main.main() --> Main.addTransaction() --> Transaction --> InvalidTransactionException.
      (i anropskedjan kan jag följa hur programmet gått från main(), via addTransaction() och konstruktorn i Transaction,
      till min egen exceptionklass)
    * Till höger i Threds & Variables:
      this = InvalidTransactionException --> ett nytt exception-objekt håller på att skapas.
      message = "Beloppet måste vara större än 0." --> felmeddelandet som skickades från mitt Transaction-record.
    * Jag välje "Step over" och då körs super(message) i konstruktorn för InvalidTransactionException. Min egen exceptionklass
      ärverfrån RuntimeException eftersom jag har extendat RuntimeException i klassdeklarationen.
      super(message) anropar konstruktorn i RuntimeException och skickar vidare felmeddelandet dit. Eftersom jag använder
      "Step over" går debugen inte in i RuntimeException utan fortsätter till slutet av konstruktoen i min egen
      InvalidTransactionException-klass.
    * Jag väljer "Step out" och lämnar konstruktorn i InvalidTransactionException-klassen och kommer tillbaka till min kod
      som anropade den (koden i Transaction). Nu har mitt exception-objekt skapats och debugen står åter igen på raden där
      det ska kastas med throw.
    * Jag väljer "Step over" igen och Java fortsätter med throw som kastar min InvalidTransactionException.
    * Eftersom jag ännu inte fångar InvalidTransactionException med en catch i addTransaction() fortsätter felet upp genom
      anropskedjan till main(). Där har jag inte heller någon catch som kan hantera/fånga felet och därför avslutas programmet
      och ajg får felmeddelandet "Beloppet måste vara större än 0." i konsollen. Det står också "Process finished with exit
      code 1", som betyder att programmet avslutades pga ett fel.
    * Min InvalidTransactionException fungerar som den ska eftersom den förhindrar att ett Transaction-objekt med ett
      negativt belopp skapas. Men jag behöver lägga till en catch i Main så att programmet kan hantera felet utan att avslutas.
* Hantera felet.
    * Lägger till en catch i Main.java i metoden addTransaction() för att komma tillbaka till huvudmenyn istället för
      att programmet kraschar. Catchen fångar InvalidTransactionExceptionet och skriver ut ett felmeddelande och avslutar
      addTransaction() med return.
    * Testar programmet igen med - pelopp och felmeddelandet visas och jag skickas tillbaka till menyn utan att programmet
      kraschar.

*******************************************************************************************************************

* Felhantering i filtrateTransactions() med att skriva in fel datum
    * Provkör programmet och skriver in fel datum när jag ska söka efter transaktion.
    * Satte brakepoint i Main.java på LocalDate.parse() - startar debug. (skrev 2026-10-80)
    * I Threads & Variables:
      input = "3" - jag har valt att filtrera efter datum
      dateInput = "2026-10-80" - det felaktiga datumet skrevs in
      filtrateTransactions:135 - metoden och raden där programmet har stannat
    * Problemet - oktober har inte 80 dagar, men Java har inte försökt tolka datumet än eftersom raden inte har körts.
    * Jag väljer "Step into" så Java försöker köra LocalDate.parse(datumInput) men eftersom datumet int är gilltigt
      kastas ett DateTimeParseException.
    * I konsollen står:
      java.time.format.DateTimeParseException - Java kunde inte omvandla texten till ett gilltigt datum.
    * Caused by: java.time.DateTimeException: Invalid value for DayOfMonth...:80 --> 80 dagar är inte tillåtet.
    * I anropskedjan ser jag Main.filtrateTransactions(Main.java:135) --> visar vilken rad i min kod som felet uppstod.
    * Jag har ingen catch som fångar DateTimeParseException så programmet avslutas och jag får "Process finished with
      code 1".
    * Jag behöver en try/catch runt LocalDate.parse(dateInput) så att ett felmeddelande skrivs ut och programmet går
      tillbaka till menyn ist för att krascha.
    * Eftersom jag inte har skapat nån egen exception här så går jag inte in i javas inbyggda metoder så som jag gick in
      i min egen metod för att debugga.
* Hantera felet.
    * I case 3 i menyn för val 4 (i vanliga menyn) i filtrateTransaction()- metoden i Main-klassen deklarerar jag variablen
      LocalDate date; utanför try för att jag också sedna använder den utanför tyr i budgetService.findByDate(date). Sen
      lägger jag en try runt date = LocalDate.parse(dateInput); och skriver ut felmeddelande och retur i catchen.
    * Testar programmet igen med ett fel och ett rätt datum och det funkar.
    * Kan dock skriva in ett tatum som är längre fram i tiden, jag får så klart inte fram några transaktioner men
      åtgärdar ändå för att få ett felmeddelande att jag inte kan ha transaktioner som inte ännu har ett datum som varit.
    * Ser att jag inte fåt nått meddelande om min lista inte har några transaktioner med det datumet och lägger till ett
      meddelande i case 3 i filtrateTransaction()- metoden med en if-sats (lägger där till att jag vill ha de hämtade
      transaktionerna i en lista List<Transaction> transactions ... ) för att se om listan är tom så skrivs meddelandet ut.

*******************************************************************************************************************