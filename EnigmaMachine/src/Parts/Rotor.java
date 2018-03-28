package Parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import Constants.*;



public class Rotor {
    private List<Integer> leftEntries;
    private List<Integer> rightEntries;
    private int zeezIndex;
    private Rotor leftRotor = null;

    public Rotor(List<Integer> leftEntries, List<Integer> rightEntries )
    {
        this(leftEntries,rightEntries, MachineConstants.DEFAULT_ZEEZ_INDEX);
    }


    public List<Integer> getRightEntries() {
        return rightEntries;
    }

    public Rotor(List<Integer> leftEntries,List<Integer> rightEntries, int zeezIndex)
    {
        this.leftEntries = new ArrayList<>(leftEntries);
        this.rightEntries = new ArrayList<>(rightEntries);
        this.zeezIndex = zeezIndex;
    }

    public void rotateRotorOneRound()
    {
        if(this.leftEntries != null && rightEntries != null) {
            Collections.rotate(leftEntries, leftEntries.size() - 1);
            Collections.rotate(rightEntries,  leftEntries.size() - 1);
            zeezIndex--;
        }

        if(zeezIndex == -1) // time to rotate rotor to my left
        {
            if(leftRotor != null)
            {
                leftRotor.rotateRotorOneRound();
            }
            zeezIndex = leftEntries.size() - 1;
        }
    }


    public List<Integer> getLeftEntries() {
        return leftEntries;
    }

    public void setZeezIndex(int zeezIndex) {
        this.zeezIndex = zeezIndex;
    }

    public void setLeftRotor(Rotor leftRotor) {
        this.leftRotor = leftRotor;
    }

    public int outputComingFromRight(int entranceNumber)
    {
        //TODO add checks on index bound
        int searched = rightEntries.get(entranceNumber - 1);
        int index = 1;
        for (int i: leftEntries) {
            if(i == searched)
            {
                break;
            }
            index++;
        }
        return index;
    }

    public int outputComingFromLeft(int entranceNumber)
    {
        //TODO add checks on index bound
        int searched = leftEntries.get(entranceNumber - 1);
        int index = 1;
        for (int i: rightEntries) {
            if(i == searched)
            {
                break;
            }
            index++;
        }
        return index;
    }


}
