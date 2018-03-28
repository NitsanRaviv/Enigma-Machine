package EnigmaMachine;

import Parts.Rotor;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        List<Integer> rotorsList = rotor.getLeftEntries();
        for (int i = 0; i < rotorsList.size(); i++) {
            System.out.println(rotorsList.get(i));
        }
        System.out.println();

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
}
