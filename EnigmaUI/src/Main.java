public class Main
{
    public static void main(String[] args) {

        MainMenu mainMenu = new MainMenu();
        createMenue(mainMenu);
        mainMenu.start();
    }


    public static void createMenue(MainMenu mainMenu)
    {
        MenuItem readFile = new MenuItem("readFile", 1, "Read the machine details file", () -> System.out.println("1"));
        MenuItem machineSpecifications = new MenuItem("machineSpecifications", 2, "Display the machine specifications",() -> System.out.println("2"));
        MenuItem initialCodeManually = new MenuItem("initialCodeManually", 3, "Select Initial Code Configuration (manually)",() -> System.out.println("3"));
        MenuItem automaticallyAutomatically = new MenuItem("automaticallyAutomatically", 4, "Select Initial Code Configuration (automatically)",() -> System.out.println("4"));
        MenuItem inputProcessing = new MenuItem("inputProcessing", 5, "Input processing",() -> System.out.println("5"));
        MenuItem resetCode = new MenuItem("resetCode", 6, "Reset current code",() -> System.out.println("6"));
        MenuItem historyAndStatistics = new MenuItem("historyAndStatistics", 7, "Show history and statistics",() -> System.out.println("7"));
        MenuItem exit = new MenuItem("exit", 8, "Exit",() -> System.out.println("8"));

        mainMenu.addNewItem(readFile);
        mainMenu.addNewItem(machineSpecifications);
        mainMenu.addNewItem(initialCodeManually);
        mainMenu.addNewItem(automaticallyAutomatically);
        mainMenu.addNewItem(inputProcessing);
        mainMenu.addNewItem(resetCode);
        mainMenu.addNewItem(historyAndStatistics);
        mainMenu.addNewItem(exit);
    }

}
