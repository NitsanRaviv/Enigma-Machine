import LogicManager.ErrorsMessages;
import LogicManager.Integrator;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainMenu {

    private ArrayList<MenuItem> theMenu = new ArrayList<>();
    private ArrayList<MenuItem> levelsMenu = new ArrayList<>();
    private String path;
    private Scanner getInput = new Scanner(System.in);

    public void addNewItem(MenuItem newItem) {
        theMenu.add(newItem);
    }

    public void start() {
        int userChoice = 0;
        boolean fileExists = false, choiceSucc, codeInitialized = false;
        createLevelsMenu();

        while (userChoice != MainMenuOptions.exit) {
            printMenu();
            userChoice = getInputFromUser(MainMenuOptions.readMachineFile,MainMenuOptions.exit);

            if(!(priorityChecks(userChoice,fileExists,codeInitialized)))
            {
                getInput.nextLine();
                continue;
            }

            choiceSucc = (doChoice(userChoice));
            if (userChoice == MainMenuOptions.readMachineFile && choiceSucc)
                fileExists = true;
            if ((userChoice == MainMenuOptions.initialCodeManually || userChoice == MainMenuOptions.initialCodeAutomatically)
                    && choiceSucc)
                codeInitialized = true;
        }
    }

    private void createLevelsMenu() {
        MenuItem easyTask = new MenuItem(TaskLevels.levelEasy,"Easy");
        MenuItem mediumTask = new MenuItem(TaskLevels.levelMedium,"Medium");
        MenuItem hardTask = new MenuItem(TaskLevels.levelHard,"Haed");
        MenuItem impossibleTask = new MenuItem(TaskLevels.levelImpossible,"Impossible");

        levelsMenu.add(easyTask);
        levelsMenu.add(mediumTask);
        levelsMenu.add(hardTask);
        levelsMenu.add(impossibleTask);
    }

    private boolean priorityChecks(int userChoice, boolean fileExists, boolean codeInitialized) {
        if (userChoice == MainMenuOptions.errorSign)
            return false;

        if (userChoice != MainMenuOptions.readMachineFile && userChoice != MainMenuOptions.exit && !fileExists) {
            System.out.println("There is no file in the system. First, you need to read a file");
            return false;
        }
        if ((userChoice == MainMenuOptions.inputProcessing ||userChoice == MainMenuOptions.resetCode ||
                userChoice == MainMenuOptions.automaticDecoding) && !codeInitialized ) {
            System.out.println("First, you must select an initial code configuration(manually or automatically)");
            return false;
        }

        return true;
    }

    private boolean doChoice(int userChoice) {

        boolean res = true;
        switch (userChoice) {
            case MainMenuOptions.readMachineFile:
                res = readMachineFile();
                break;
            case MainMenuOptions.machineSpecifications:
                res = showMachineSpecification();
                break;
            case MainMenuOptions.initialCodeManually:
                initialCodeManually();
                break;
            case MainMenuOptions.initialCodeAutomatically:
                initialCodeAutomatically();
                break;
            case MainMenuOptions.inputProcessing:
                res = processInput();
                break;
            case MainMenuOptions.resetCode:
                res = resetCurrentCode();
                break;
            case MainMenuOptions.historyAndStatistics:
                res = gethistoryAndStatistics();
                break;
            case MainMenuOptions.automaticDecoding:
                res = doAutomaticDecoding();
                break;
            case MainMenuOptions.exit:
                turnOff();
                break;
        }
        return res;
    }

    //TODO
    private boolean doAutomaticDecoding() {

       String inputToProcess = getStringToProcess();
       int chosenTaskLevel = getTaskLevel();
       showTaskSize(chosenTaskLevel);//TODO in preformer
       int numberOfAgents = getNumberOfAgents();
       int missionSize = getMissionSize(); // TODO: there is any limit?? if no-> Nice need to add to getInputFromUser also get without limits
       System.out.println("To confirm the beginning of the automatic decryption process, please press any key");
       getInput.next();

       // START here the balagan!!!!!!!!!!
       return true;
    }

    private int getMissionSize() {
        int res = MainMenuOptions.errorSign;

        while (res == MainMenuOptions.errorSign)
        {
            System.out.println("Please select the mission size:");
            res = getInputFromUser(2,1000000); // need limits?
            getInput.nextLine();
        }

        return res;
    }

    private void showTaskSize(int chosenTaskLevel) {
       long res =  Integrator.getIntegrator().getTaskSize(chosenTaskLevel);
       System.out.println("The difficulty of the task is: " + res);
    }

    private int getNumberOfAgents() {

        int maxAgents = Integrator.getIntegrator().getNumberOfAgents();
        int res = MainMenuOptions.errorSign;

        while (res == MainMenuOptions.errorSign)
        {
            System.out.println("Please select the number of agents for the task: (between 2 to " + maxAgents + " )");
            res = getInputFromUser(2,maxAgents);
            getInput.nextLine();
        }

        return res;
    }

    private int getTaskLevel() {
        int res = MainMenuOptions.errorSign;

        while (res == MainMenuOptions.errorSign)
        {
            printLevelsMenu();
            res = getInputFromUser(TaskLevels.levelEasy,TaskLevels.levelImpossible);
            getInput.nextLine();
        }

        return res;
    }

    private void printLevelsMenu() {
        System.out.println("---------------------------------------------------------\n");
        System.out.println("Please select the task difficulty level:");

        for (MenuItem item : levelsMenu)
            System.out.println(item.toString());


        System.out.println("---------------------------------------------------------\n");
    }

    private String getStringToProcess() {

        boolean validString = false;
        String input, cleanInput = null;
        getInput.nextLine();

        while (!validString)
        {
            System.out.println("Please enter a string to process:");
            input = getInput.nextLine();
            cleanInput = Integrator.getIntegrator().cleanStringFromExcludeChars(input);
            validString = Integrator.getIntegrator().checkStringForAutomaticDecoding(cleanInput);

            if(!validString)
                System.out.println("You enter invalid string. Try again:");
        }

        return cleanInput;
    }

    private void initialCodeAutomatically() {

        String msg = Integrator.getIntegrator().setRandomMachineCode();
        System.out.println(msg);
    }

    private boolean gethistoryAndStatistics() {
        try {
            System.out.println(Integrator.getIntegrator().getStatistics());
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    private void initialCodeManually() {
        String[] rotors;
        String[] rotorMap;
        String chosenReflector;
        boolean rotorsValid, rotorMapValid, reflectorValid ,sizeWasValid = false, sizeProblemR = false, sizeProblemRm = false;

        rotors = getRotorsFromUser();
        rotorMap = getRotorMapFromUser();
        chosenReflector = getReflectorFromUser();
        rotorsValid = Integrator.getIntegrator().checkInitialRotors(rotors);
        rotorMapValid = Integrator.getIntegrator().checkInitialRotorsMap(rotorMap);
        reflectorValid = Integrator.getIntegrator().checkChosenReflector(chosenReflector);

        while(!sizeWasValid)
        {
            sizeWasValid = true;
            while(!rotorsValid || sizeProblemR)
            {
                if (!rotorsValid)
                    System.out.println("You have inserted invalid rotor numbers. Please Try again");
                rotors = getRotorsFromUser();
                rotorsValid = Integrator.getIntegrator().checkInitialRotors(rotors);
                sizeProblemR = false;
            }
            while(!rotorMapValid || sizeProblemRm)
            {
                if(!rotorMapValid)
                    System.out.println("You have inserted invalid initial location for rotors. Please Try again");
                rotorMap = getRotorMapFromUser();
                rotorMapValid = Integrator.getIntegrator().checkInitialRotorsMap(rotorMap);
                sizeProblemRm = false;
            }
            while(!reflectorValid)
            {
                System.out.println("You have inserted an invalid reflector number. Please Try again");
                chosenReflector = getReflectorFromUser();
                reflectorValid = Integrator.getIntegrator().checkChosenReflector(chosenReflector);
            }
            if (rotors.length != rotorMap.length)
            {
                System.out.println("You must insert the same number of rotors and initial location for them. Please Try again");
                sizeWasValid = false;
                sizeProblemR = true;
                sizeProblemRm = true;
            }
        }

        Integrator.getIntegrator().setInitialCode(rotors,rotorMap,chosenReflector);
    }

    private String getReflectorFromUser() {
        System.out.println("Please enter the chosen reflector(example format: II)");
        return getInput.next().toUpperCase();
    }

    private String[] getRotorMapFromUser() {
        System.out.println("Please enter initial location for rotors in order without spaces(example format: ABC)");
        return getInputAndMakeArr("");
    }

    private String[] getRotorsFromUser() {
        System.out.println("Please enter rotor numbers by order and separate by ','(example format: 1,2,3)");
        return getInputAndMakeArr(",");
    }

    private String[] getInputAndMakeArr(String splitSign) {
        String[] res;
        String input;

        input = getInput.next().toUpperCase();
        res = input.split(splitSign);

        return res;
    }

    private void turnOff() {
        System.out.println("Bye bye!");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean resetCurrentCode() {
        String msg = Integrator.getIntegrator().resetCode();

        if (msg == ErrorsMessages.errNoMachine) {
            System.out.println(msg);
            return false;
        }

        return true;
    }

    private boolean showMachineSpecification() {
        List<String> msgs = Integrator.getIntegrator().getMachineSpecification();
        System.out.println(msgs);

        if (msgs.get(0) == ErrorsMessages.errNoMachine)
            return false;

        return true;
    }

    private boolean readMachineFile() {
        System.out.println("Please enter a path to the xml file:");
        path = getInput.next();
        String msg = Integrator.getIntegrator().loadMachineFromXml(path);

        if (msg != ErrorsMessages.noErrors) {
            System.out.println(msg);
            getInput.nextLine();
            return false;
        }
        else
            System.out.println("Reading file...");

        return true;
    }

    private boolean processInput() {
        boolean validInput;
        String msg;
        System.out.println("Please enter an input to process");
        String input = getInput.next().toUpperCase();
        validInput = Integrator.getIntegrator().checkValidOfProcessInput(input);

        if (validInput) {
            msg = Integrator.getIntegrator().processInput(input);
            System.out.println(msg);
            if (msg == ErrorsMessages.errNoMachine)
                return false;
        } else
            return false;

        return true;
    }

    private int getInputFromUser(int minNumber, int maxNumber) {
        int userChoice;

        try {
            userChoice = getInput.nextInt();
        } catch (IllegalArgumentException ilae) {
            System.out.println("Invaild input! Please enter a number between " + minNumber + " to " + maxNumber );
            return MainMenuOptions.errorSign;
        } catch (InputMismatchException ime) {
            System.out.println("Invaild input! Please enter a number between " + minNumber + " to " + maxNumber);
            return MainMenuOptions.errorSign;
        }

        if (userChoice > maxNumber || userChoice < minNumber) {
            System.out.println("Out of range input! Please enter a number between "+ minNumber + " to " + maxNumber);
            return MainMenuOptions.errorSign;
        }

        return userChoice;
    }

    private void printMenu() {
        System.out.println("---------------------------------------------------------\n");
        System.out.println("Welcome to NicEnigma\n");
        System.out.println("Please select one of the following options:");

        for (MenuItem item : theMenu)
            System.out.println(item.toString());


        System.out.println("---------------------------------------------------------\n");
    }
}





