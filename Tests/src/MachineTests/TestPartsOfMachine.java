package MachineTests;


import Machine.MachineBuilder;
import Machine.MachineProxy;
import Parts.Reflector;
import Parts.Rotor;
import Utilities.LanguageInterpeter;
import javafx.util.Pair;
import org.junit.Assert;
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
        Assert.assertEquals(rotor.getLeftEntries().toString(), "[1, 3, 2]");


    }

    @Test
    public void testNotifyRotor() {
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
        Assert.assertEquals(rotor1.getLeftEntries().toString(), "[2, 1, 3]");
        Assert.assertEquals(rotor1.getRightEntries().toString(), "[1, 2, 3]");

    }

    @Test
    public void testEncription() {
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
        Assert.assertEquals(rotor.outputComingFromRight(2), 2);
        rotor.rotateRotorOneRound();
        Assert.assertEquals(rotor.outputComingFromRight(2), 3);
        rotor.rotateRotorOneRound();
        Assert.assertEquals(rotor.outputComingFromRight(3), 3);
    }

    @Test
    public void testInterpeter() {
        char[] lang = new char[]{'*', '&', '%', '9', '1'};
        List<Integer> numbers = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            numbers.add(lang.length - i);
        }
        LanguageInterpeter languageInterpeter = new LanguageInterpeter(lang);
        List<Character> res = languageInterpeter.numberToLetters(numbers);
        Assert.assertEquals(res.toString(), "[1, 9, %, &, *]");

    }

    @Test
    public void testMachineEncriptionAndInitMachine() {
        LanguageInterpeter languageInterpeter = new LanguageInterpeter(new char[]{'A', 'B', 'C', 'D', 'E', 'F'});
        List<Integer> numbers = languageInterpeter.getLanguageAsNumbers();
        List<Integer> leftEntries1 = new ArrayList<>(numbers.size());
        leftEntries1.add(6);
        leftEntries1.add(5);
        leftEntries1.add(4);
        leftEntries1.add(3);
        leftEntries1.add(2);
        leftEntries1.add(1);

        Rotor rotorRight = new Rotor(leftEntries1, numbers);
        rotorRight.setNotch(3);
        rotorRight.setPosition(3);

        List<Integer> leftEntries2 = new ArrayList<>(numbers.size());
        leftEntries2.add(5);
        leftEntries2.add(2);
        leftEntries2.add(4);
        leftEntries2.add(6);
        leftEntries2.add(3);
        leftEntries2.add(1);

        Rotor rotorLeft = new Rotor(leftEntries2, numbers);
        rotorLeft.setPosition(3);


        Map<Integer, Integer> reflectorMap = new HashMap<>(6);
        reflectorMap.put(1, 4);
        reflectorMap.put(2, 5);
        reflectorMap.put(3, 6);

        Reflector reflector = new Reflector(reflectorMap, 0);
        List<Rotor> rotors = new ArrayList<>(2);
        rotors.add(rotorLeft);
        rotors.add(rotorRight);
        List<Reflector> chosenReflector = new ArrayList<>(1);
        chosenReflector.add(reflector);


        EnigmaMachine machine = new EnigmaMachine(rotors, chosenReflector)
                .setChosenReflector(0)
                .setChosenRotors(new Pair<>(0, 3), new Pair<>(1, 3));

        List<Integer> resNumbers;
        List<Character> resCharacters;
        resNumbers = machine.encryptCode(languageInterpeter.lettersToNumbers(new char[]{'A', 'A', 'B', 'B', 'C', 'C', 'D', 'D', 'E', 'E', 'F', 'F'}));
        resCharacters = languageInterpeter.numberToLetters(resNumbers);
        Assert.assertEquals(resCharacters.toString(), "[B, D, E, A, B, D, A, C, D, F, A, C]");

        machine.setToInitialState();
        resNumbers = machine.encryptCode(languageInterpeter.lettersToNumbers(new char[]{'A', 'A', 'B', 'B', 'C', 'C', 'D', 'D', 'E', 'E', 'F', 'F'}));
        resCharacters = languageInterpeter.numberToLetters(resNumbers);
        Assert.assertEquals(resCharacters.toString(), "[B, D, E, A, B, D, A, C, D, F, A, C]");

    }

    @Test
    public void testMachineProxyAndMachineBuilder() {
        List<Character> resCharacters;
        MachineBuilder mb = new MachineBuilder();
        MachineProxy mp = mb.initMachine(new char[]{'A', 'B', 'C', 'D', 'E', 'F'}, 2)
                .setReflector(new int[]{1, 2, 3}, new int[]{4, 5, 6}, "I")
                .setRotor("ABCDEF", "EBDFCA", 4, 0)
                .setRotor("ABCDEF", "FEDCBA", 1, 1)
                .create();
        mp.setChosenRotors(new Pair<>(0, 3), new Pair<>(1, 3)); //left to right
        mp.setChosenReflector("I");

        resCharacters = mp.encryptCode("AABBCCDDEEFF");
        Assert.assertEquals(resCharacters.toString(), "[B, D, E, A, B, D, A, C, D, F, A, C]");
        mp.setMachineToInitialState();
        resCharacters = mp.encryptCode("AABBCCDDEEFF");
        Assert.assertEquals(resCharacters.toString(), "[B, D, E, A, B, D, A, C, D, F, A, C]");
        String test = "43,34,32";
    }

}
