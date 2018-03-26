package EnigmaMachine;

import Parts.Rotor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestPartsOfMachine {

    private List<Integer> rotorList = new ArrayList<>(5);

    @Test
    public void testRotor()
    {
        List<Integer> beforeRotatation = new ArrayList<>(5);
        for(int i = 1; i<=5; i++)
        {
            beforeRotatation.add(i);
        }
        Rotor rotor = new Rotor(beforeRotatation);
        rotor.rotateRotorOneRound();
        List<Integer> rotorsList = rotor.getEntries();
        for(int i = 0; i< rotorsList.size(); i++)
        {
            System.out.println(rotorsList.get(i));
        }
    }
}
