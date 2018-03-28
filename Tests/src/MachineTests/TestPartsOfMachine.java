package MachineTests;


import Parts.Reflector;
import Parts.Rotor;
import Utilities.LanguageInterpeter;
import org.junit.Test;
import Machine.EnigmaMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPartsOfMachine {

    private List<Integer> rotorList = new ArrayList<>(5);

    @Test
    public void testRotor() {
        List<Integer> rightEntries = new ArrayList<>(5);
        rightEntries.add(1);
        rightEntries.add(2);
        rightEntries.add(3);
        List<Integer> leftEntries = new ArrayList<>(5);
        leftEntries.add(2);
        leftEntries.add(1);
        leftEntries.add(3);
        Rotor rotor = new Rotor(leftEntries, rightEntries);
        rotor.rotateRotorOneRound();
        TestUtilities.printList(rotor.getLeftEntries());

    }

    @Test
    public void testNotifyRotor()
    {
        List<Integer> rightEntries = new ArrayList<>(5);
        rightEntries.add(1);
        rightEntries.add(2);
        rightEntries.add(3);
        List<Integer> leftEntries = new ArrayList<>(5);
        leftEntries.add(2);
        leftEntries.add(1);
        leftEntries.add(3);
        Rotor rotor = new Rotor(leftEntries, rightEntries);
        Rotor rotor1 = new Rotor(leftEntries, rightEntries);
        rotor.setLeftRotor(rotor1);
        rotor.rotateRotorOneRound();
        for (int i = 0; i < rotor1.getLeftEntries().size(); i++) {
            System.out.println("left " + rotor1.getLeftEntries().get(i) + " right " +  rotor1.getRightEntries().get(i));
        }
        System.out.println();
    }

    @Test
    public void testEcription()
    {
        List<Integer> rightEntries = new ArrayList<>(5);
        rightEntries.add(1);
        rightEntries.add(2);
        rightEntries.add(3);
        List<Integer> leftEntries = new ArrayList<>(5);
        leftEntries.add(2);
        leftEntries.add(1);
        leftEntries.add(3);
        Rotor rotor = new Rotor(leftEntries, rightEntries);
        rotor.rotateRotorOneRound();
        System.out.println(rotor.outputComingFromRight(2 ));
        rotor.rotateRotorOneRound();
        System.out.println(rotor.outputComingFromRight(2 ));
        rotor.rotateRotorOneRound();
        System.out.println(rotor.outputComingFromRight(3 ));
        System.out.println();
    }

    @Test
    public void testInterpeter()
    {
        char[] lang = new char[]{'*','&','%','9','1'};
        List<Integer> numbers = new ArrayList<>(5);
        for(int i = 0; i < 5; i ++)
        {
            numbers.add(lang.length - i );
        }
        LanguageInterpeter languageInterpeter = new LanguageInterpeter(lang);
        List<Character> res = languageInterpeter.numberToLetters(numbers);
        TestUtilities.printList(res);

    }

    @Test
    public void testMachineEncription()
    {
        LanguageInterpeter  languageInterpeter = new LanguageInterpeter(new char[]{'A','B','C','D','E','F'});
        List<Integer> numbers = languageInterpeter.getLanguageAsNumbers();
        List<Integer> leftEntries1 = new ArrayList<>(numbers.size());
        leftEntries1.add(6);
        leftEntries1.add(5);
        leftEntries1.add(4);
        leftEntries1.add(3);
        leftEntries1.add(2);
        leftEntries1.add(1);

        Rotor rotorRight = new Rotor(leftEntries1,numbers);
        rotorRight.setNotch(3);
        rotorRight.setZeezIndex(0);

        List<Integer> leftEntries2 = new ArrayList<>(numbers.size());
        leftEntries2.add(5);
        leftEntries2.add(2);
        leftEntries2.add(4);
        leftEntries2.add(6);
        leftEntries2.add(3);
        leftEntries2.add(1);

        Rotor rotorLeft = new Rotor(leftEntries2, numbers);
        rotorLeft.setNotch(3);


        Map<Integer, Integer>reflectorMap = new HashMap<>(6);
        reflectorMap.put(1,4);
        reflectorMap.put(2,5);
        reflectorMap.put(3,6);

        Reflector reflector = new Reflector(reflectorMap);
        List<Rotor> rotors = new ArrayList<>(2);
        rotors.add(rotorLeft);
        rotors.add(rotorRight);
        List<Reflector> chosenReflector= new ArrayList<>(1);
        chosenReflector.add(reflector);


        EnigmaMachine machine = new EnigmaMachine(rotors,chosenReflector)
                .setChosenReflector(0)
                .setChosenRotors(new int[]{0,1});

        List<Integer> resNumbers;
        List<Character> resCharacters;
        resNumbers = machine.encryptCode(languageInterpeter.lettersToNumbers(new char[]{'A','A','B','B','C','C','D','D','E','E','F','F'}));
        resCharacters = languageInterpeter.numberToLetters(resNumbers);
        TestUtilities.printList(resCharacters);
    }


}
