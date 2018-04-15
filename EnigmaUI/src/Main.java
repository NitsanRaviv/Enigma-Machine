public class Main
{
    public static void main(String[] args) {

        MainMenu mainMenu = new MainMenu();
        createMenue(mainMenu);
        mainMenu.start();
    }


    public static void createMenue(MainMenu mainMenu)
    {
        MenuItem readFile = new MenuItem("readFile", 1, "Read the machine details file");
        MenuItem machineSpecifications = new MenuItem("machineSpecifications", 2, "Display the machine specifications");
        MenuItem initialCodeManually = new MenuItem("initialCodeManually", 3, "Select Initial Code Configuration (manually)");
        MenuItem automaticallyAutomatically = new MenuItem("automaticallyAutomatically", 4, "Select Initial Code Configuration (automatically)");
        MenuItem inputProcessing = new MenuItem("inputProcessing", 5, "Input processing");
        MenuItem resetCode = new MenuItem("resetCode", 6, "Reset current code");
        MenuItem historyAndStatistics = new MenuItem("historyAndStatistics", 7, "Show history and statistics");
        MenuItem exit = new MenuItem("exit", 8, "Exit");

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
