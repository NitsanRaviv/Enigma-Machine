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

    @Ignore
    public void testRotor() {
        List<Integer> beforeRotatation = new ArrayList<>(5);
        for (int i = 1; i <= 5; i++) {
            beforeRotatation.add(i);
        }
        Rotor rotor = new Rotor(beforeRotatation);
        rotor.rotateRotorOneRound();
        List<Integer> rotorsList = rotor.getLeftEntries();
        for (int i = 0; i < rotorsList.size(); i++) {
            System.out.println(rotorsList.get(i));
        }
    }

    @Ignore
    public void testNotifyRotor()
    {
        List<Integer> beforeRotatation = new ArrayList<>(5);
        beforeRotatation.add(3);
        beforeRotatation.add(1);
        beforeRotatation.add(2);
        Rotor rotor = new Rotor(beforeRotatation, 0);
        Rotor rotor1 = new Rotor(beforeRotatation);
        rotor.setLeftRotor(rotor1);
        rotor.rotateRotorOneRound();
        for (int i = 0; i < rotor1.getLeftEntries().size(); i++) {
            System.out.println("left " + rotor1.getLeftEntries().get(i) + " right " +  rotor1.getRightEntries().get(i));
        }
    }

    @Test
    public void ecription()
    {
        List<Integer> beforeRotatation = new ArrayList<>(5);
        beforeRotatation.add(3); // 1 -> 3
        beforeRotatation.add(1); // 2 -> 1
        beforeRotatation.add(2); // 3 -> 2
        Rotor rotor = new Rotor(beforeRotatation);
        rotor.rotateRotorOneRound();
        System.out.println(rotor.outputComingFromRight(2 ));
        rotor.rotateRotorOneRound();
        System.out.println(rotor.outputComingFromRight(2 ));
        rotor.rotateRotorOneRound();
        System.out.println(rotor.outputComingFromRight(3 ));

    }
}
