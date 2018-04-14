package Parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import Constants.*;



public class Rotor {
    private List<Integer> leftEntries;
    private List<Integer> rightEntries;
    private int notch;
    private Rotor leftRotor = null;
    private int initialPosition;
    private int initialNotch;
    private int initialStateBeforeRotation;

    public Rotor(List<Integer> leftEntries, List<Integer> rightEntries) {
        this(leftEntries, rightEntries, MachineConstants.DEFAULT_ZEEZ_INDEX);
    }


    public List<Integer> getRightEntries() {
        return rightEntries;
    }

    public Rotor(List<Integer> leftEntries, List<Integer> rightEntries, int notch) {
        this.leftEntries = new ArrayList<>(leftEntries);
        this.rightEntries = new ArrayList<>(rightEntries);
        this.notch = notch;
        this.initialNotch = notch;
        this.initialStateBeforeRotation = rightEntries.get(0);
    }

    public void rotateRotorOneRound() {
        if (this.leftEntries != null && rightEntries != null) {
            Collections.rotate(leftEntries, leftEntries.size() - 1);
            Collections.rotate(rightEntries, rightEntries.size() - 1);
            notch++;
        }

        if (notch == leftEntries.size()) // time to rotate rotor to my left
        {
            if (leftRotor != null) {
                leftRotor.rotateRotorOneRound();
            }
            notch = 0;
        }
    }


    public List<Integer> getLeftEntries() {
        return leftEntries;
    }

    //should use it only when initializing or reseting machine , maybe init only in constructor
    public void setNotch(int notch) {
        this.notch = notch;
        this.initialNotch = notch;
    }

    public void setPosition(Integer pos) {
        while (this.rightEntries.get(0) != pos) {
            Collections.rotate(rightEntries, rightEntries.size() - 1);
            Collections.rotate(leftEntries, leftEntries.size() - 1);
            notch++;
            if (notch == leftEntries.size())
                notch = 0;
        }
        this.initialPosition = pos;
    }

    public void setLeftRotor(Rotor leftRotor) {
        this.leftRotor = leftRotor;
    }

    public int outputComingFromRight(int entranceNumber) {
        //TODO add checks on index bound
        int searched;
        searched = rightEntries.get(entranceNumber - 1);
        int index = 1;
        for (int i : leftEntries) {
            if (i == searched) {
                break;
            }
            index++;
        }
        return index;
    }

    public int outputComingFromLeft(int entranceNumber) {
        //TODO add checks on index bound
        int searched = leftEntries.get(entranceNumber - 1);
        int index = 1;
        for (int i : rightEntries) {
            if (i == searched) {
                break;
            }
            index++;
        }
        return index;
    }

    public void setToInitialState() {
        this.initialFirstPosition();
    }

    private void initialFirstPosition() {
        this.setInitialPosition(initialStateBeforeRotation);
        this.setNotch(initialNotch);
        this.setPosition(initialPosition);
    }

    private void setInitialPosition(int initialPos) {
        while (this.rightEntries.get(0) != initialPos) {
            Collections.rotate(rightEntries, rightEntries.size() - 1);
            Collections.rotate(leftEntries, leftEntries.size() - 1);
        }
    }
}
