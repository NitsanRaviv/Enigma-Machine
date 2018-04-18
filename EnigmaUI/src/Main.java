import LogicManager.LogicApi;
import LogicManager.Performer;

import java.util.Scanner;

public class Main
{
    private static LogicApi logicApi = Performer.getPerformer();
    private static String xmlFilePath = "Tests/ex1-sanity-small.xml";
    public static void main(String[] args) {

        MainMenu mainMenu = new MainMenu();
        createMenue(mainMenu);
        mainMenu.start();
    }


    public static void createMenue(MainMenu mainMenu)
    {
        MenuItem readFile = new MenuItem("readFile", 1, "Read the machine details file",() -> logicApi.loadMachineFromXml(xmlFilePath));
        MenuItem machineSpecifications = new MenuItem("machineSpecifications", 2, "Display the machine specifications",() -> System.out.println((logicApi.getMachineSpecification())));
        MenuItem initialCodeManually = new MenuItem("initialCodeManually", 3, "Select Initial Code Configuration (manually)",() -> System.out.println("3"));
        MenuItem automaticallyAutomatically = new MenuItem("automaticallyAutomatically", 4, "Select Initial Code Configuration (automatically)",() -> System.out.println("4"));
        MenuItem inputProcessing = new MenuItem("inputProcessing", 5, "Input processing",() -> { Scanner getInput = new Scanner(System.in); String input = getInput.next();System.out.println(logicApi.processInput(input));});
        MenuItem resetCode = new MenuItem("resetCode", 6, "Reset current code",() -> logicApi.resetCode());
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
