
public class Main
{
    public static void main(String[] args) {

        MainMenu mainMenu = new MainMenu();
        createMenu(mainMenu);
        mainMenu.start();
    }

    public static void createMenu(MainMenu mainMenu)
    {
        MenuItem readFile = new MenuItem( MainMenuOptions.readMachineFile, "Read the machine details file");
        MenuItem machineSpecifications = new MenuItem( MainMenuOptions.machineSpecifications, "Display the machine specifications");
        MenuItem initialCodeManually = new MenuItem( MainMenuOptions.initialCodeManually, "Select Initial Code Configuration (manually)");
        MenuItem automaticallyAutomatically = new MenuItem( MainMenuOptions.initialCodeAutomatically, "Select Initial Code Configuration (automatically)");
        MenuItem inputProcessing = new MenuItem( MainMenuOptions.inputProcessing, "Input processing");
        MenuItem resetCode = new MenuItem( MainMenuOptions.resetCode, "Reset current code");
        MenuItem historyAndStatistics = new MenuItem( MainMenuOptions.historyAndStatistics, "Show history and statistics");
        MenuItem automaticDecoding = new MenuItem( MainMenuOptions.automaticDecoding, "Automatic decoding");
        MenuItem exit = new MenuItem( MainMenuOptions.exit, "Exit");

        mainMenu.addNewItem(readFile);
        mainMenu.addNewItem(machineSpecifications);
        mainMenu.addNewItem(initialCodeManually);
        mainMenu.addNewItem(automaticallyAutomatically);
        mainMenu.addNewItem(inputProcessing);
        mainMenu.addNewItem(resetCode);
        mainMenu.addNewItem(historyAndStatistics);
        mainMenu.addNewItem(automaticDecoding);
        mainMenu.addNewItem(exit);
    }

}
