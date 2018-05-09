import LogicManager.LogicApi;
import LogicManager.Performer;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args) {

        MainMenu mainMenu = new MainMenu();
        createMenue(mainMenu);
        mainMenu.start();
    }

    public static void createMenue(MainMenu mainMenu)
    {
        MenuItem readFile = new MenuItem("readMachineFile", MainMenuOptions.readMachineFile, "Read the machine details file");
        MenuItem machineSpecifications = new MenuItem("machineSpecifications", MainMenuOptions.machineSpecifications, "Display the machine specifications");
        MenuItem initialCodeManually = new MenuItem("initialCodeManually", MainMenuOptions.initialCodeManually, "Select Initial Code Configuration (manually)");
        MenuItem automaticallyAutomatically = new MenuItem("initialCodeAutomatically", MainMenuOptions.initialCodeAutomatically, "Select Initial Code Configuration (automatically)");
        MenuItem inputProcessing = new MenuItem("inputProcessing", MainMenuOptions.inputProcessing, "Input processing");
        MenuItem resetCode = new MenuItem("resetCode", MainMenuOptions.resetCode, "Reset current code");
        MenuItem historyAndStatistics = new MenuItem("historyAndStatistics", MainMenuOptions.historyAndStatistics, "Show history and statistics");
        MenuItem automaticDecoding = new MenuItem("automaticDecoding", MainMenuOptions.automaticDecoding, "Automaticd decoding");
        MenuItem exit = new MenuItem("exit", MainMenuOptions.exit, "Exit");

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
