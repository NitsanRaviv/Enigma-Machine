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

    public Rotor(List<Integer> leftEntries)
    {
        this(leftEntries, MachineConstants.DEFAULT_ZEEZ_INDEX);
    }


    public List<Integer> getRightEntries() {
        return rightEntries;
    }

    public Rotor(List<Integer> leftEntries, int zeezIndex)
    {
        this.leftEntries = new ArrayList<>(leftEntries);
        this.rightEntries = new ArrayList<>(leftEntries);
        this.zeezIndex = zeezIndex;
        initLeftEntries();
    }

    public void rotateRotorOneRound()
    {
        if(this.leftEntries != null && rightEntries != null) {
            Collections.rotate(leftEntries, leftEntries.size() - 1);
            Collections.rotate(rightEntries,  1);
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

    private void initLeftEntries() {
        for (int i = 0; i < rightEntries.size(); i++)
        {
            leftEntries.set(rightEntries.get(i) - 1, i + 1);
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
        return rightEntries.get(entranceNumber - 1);
    }

    public int outputComingFromLeft(int entranceNumber)
    {
        return leftEntries.get(entranceNumber - 1);
    }


}
