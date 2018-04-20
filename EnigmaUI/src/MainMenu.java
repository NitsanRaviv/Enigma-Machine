import LogicManager.ErrorsMessages;
import LogicManager.Integrator;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainMenu {

    private ArrayList<MenuItem> theMenu = new ArrayList<>();
    private final int errorSign = -1;
    private String path;
    private Scanner getInput = new Scanner(System.in);

    public void addNewItem(MenuItem newItem)
    {
        theMenu.add(newItem);
    }

    private int getSerialNumberByName(String name)
    {
        int res = errorSign;
        for(MenuItem item : theMenu)
        {
            if(item.getName() == name)
                res = item.getSerialNumber();
        }

        return res;
    }

    public void start(String xmlFilePath) {
        int userChoice = 0;
        int exitItemNumber = getSerialNumberByName("exit");
        int readFileItemNUmber = getSerialNumberByName("readFile");
        boolean fileExists = false , choiceSucc;
        path = xmlFilePath;

        while (userChoice != exitItemNumber) {
            printMenu();
            userChoice = getInputFromUser();

            if (userChoice == errorSign)
                continue;
            if (userChoice == readFileItemNUmber && fileExists) {
                System.out.println("There is a file in the system so you can not read a new file. In order to read a new file, the system must be restarted");
                continue;
            }
            if (userChoice != readFileItemNUmber && userChoice != exitItemNumber && !fileExists) {
                System.out.println("There is no file in the system. First, you need to read a file");
                continue;
            }

            choiceSucc = (doChoice(userChoice));
            if (userChoice == readFileItemNUmber && choiceSucc)
                fileExists = true;
        }
    }

    //have to separate cases to methods(after we will be sure we work like that)
    private boolean doChoice(int userChoice) {

        String msg = null , input;
        boolean res = true;
        switch (userChoice) {
            case 1:
                res = Integrator.getIntegrator().loadMachineFromXml(path,msg);
                if(!res)
                    System.out.println(msg);
                break;

            case 2:
                msg = Integrator.getIntegrator().getMachineSpecification();
                System.out.println(msg);
                if(msg == ErrorsMessages.errNoMachine)
                    res = false;
                break;

            case 3:
                System.out.println("do 3...");
                break;
            case 4:
                System.out.println("do 4...");
                break;
            case 5:
                System.out.println("Pleas enter input to process");
                input = getInput.next();
                // TODO: need to check if the input is from the ABC machine
                msg = Integrator.getIntegrator().processInput(input);
                System.out.println(msg);
                if(msg == ErrorsMessages.errNoMachine)
                    res = false;
                break;
            case 6:
                msg = Integrator.getIntegrator().resetCode();
                if(msg == ErrorsMessages.errNoMachine)
                {
                    System.out.println(msg);
                    res = false;
                }
                break;
            case 7:
                System.out.println("do 7...");
                break;
            case 8:
                System.out.println("Bye bye!");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }

        return res;
    }

    private int getInputFromUser() {
        int userChoice;

        try {
            userChoice = getInput.nextInt();
        } catch (IllegalArgumentException ilae) {
            System.out.println("Invaild input! Please enter a number between 1 to 8");
            return errorSign;
        }
        catch (InputMismatchException ime)
        {
            System.out.println("Invaild input! Please enter a number between 1 to 8");
            return errorSign;
        }

        if (userChoice > 8 || userChoice < 1) {
            System.out.println("Out of range input! Please enter a number between 1 to 8");
            return errorSign;
        }

        return userChoice;
    }

    private void printMenu()
    {
        System.out.println("---------------------------------------------------------\n");
        System.out.println("Welcome to NicEnigma\n");
        System.out.println("Please select one of the options:");

        for( MenuItem item : theMenu)
        {
            System.out.println(item.getSerialNumber() + " - " + item.getDescription());
        }

        System.out.println("---------------------------------------------------------\n");
    }

}


