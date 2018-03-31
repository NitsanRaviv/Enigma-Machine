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
    private int initialNotch;
    private int initialZeezIndex;

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
        this.initialZeezIndex = zeezIndex;
    }

    public void rotateRotorOneRound()
    {
        if(this.leftEntries != null && rightEntries != null) {
            Collections.rotate(leftEntries, leftEntries.size() - 1);
            Collections.rotate(rightEntries,  rightEntries.size() - 1);
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

    //should use it only when initializing or reseting machine , maybe init only in constructor
    public void setZeezIndex(int zeezIndex) {
        this.zeezIndex = zeezIndex;
        this.initialZeezIndex = zeezIndex;
    }

    public void setNotch(Integer notch)
    {
        while (this.rightEntries.get(0) != notch)
        {
            Collections.rotate(rightEntries, rightEntries.size() - 1);
            Collections.rotate(leftEntries, leftEntries.size() - 1);
        }
        this.initialNotch = notch;
    }

    public void setLeftRotor(Rotor leftRotor) {
        this.leftRotor = leftRotor;
    }

    public int outputComingFromRight(int entranceNumber)
    {
        //TODO add checks on index bound
        int searched;
        searched = rightEntries.get(entranceNumber - 1);
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
    public void setToInitialState()
    {
        this.setZeezIndex(initialZeezIndex);
        this.setNotch(initialNotch);
    }
}
