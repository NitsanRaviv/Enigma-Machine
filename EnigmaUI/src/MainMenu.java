import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainMenu {

    private ArrayList<MenuItem> theMenu = new ArrayList<>();
    private final int errorSign = -1;

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

    public void start() {
        int userChoice = 0;
        int exitItemNumber = getSerialNumberByName("exit");
        int readFileItemNUmber = getSerialNumberByName("readFile");
        boolean fileExists = false;

        while (userChoice != exitItemNumber) {
            printMenu();
            userChoice = getInputFromUser();

            if (userChoice == errorSign)
                continue;

            if (userChoice == readFileItemNUmber && fileExists) {
                System.out.println("There is a file in the system so you can not read a new file. In order to read a new file, the system must be restarted");
                continue;
            }

            if (userChoice == readFileItemNUmber)
                fileExists = true;

            if (userChoice != readFileItemNUmber && userChoice != exitItemNumber && !fileExists) {
                System.out.println("There is no file in the system. First, you need to read a file");
                continue;
            }

            doChoice(userChoice -1); //menu items are in an array
        }
    }

    private void doChoice(int userChoice) {
        this.theMenu.get(userChoice).run();
    }


    private int getInputFromUser() {
        int userChoice;
        Scanner getInput = new Scanner(System.in);

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


