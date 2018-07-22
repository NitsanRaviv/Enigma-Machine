package Tasks;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EasyTask implements Serializable{

    private int taskSize;
    private String stringToEncrypt;
    private Pair<Integer,Integer>[] rotorsAndNotches;

    public Pair<Integer, Integer>[] getRotorsAndNotches() {
        return rotorsAndNotches;
    }

    public EasyTask(Pair<Integer,Integer>[] rotorsAndNotches, String stringToEncrypt, int taskSize){
        this.rotorsAndNotches = rotorsAndNotches;
        this.taskSize = taskSize;
        this.stringToEncrypt = stringToEncrypt;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public String getStringToDecrypt() {
        return stringToEncrypt;
    }

    public List<Integer> getNumberLocations() {
        List<Integer> numbers = new ArrayList<>();
        for (Pair<Integer, Integer> rotorsAndNotch : rotorsAndNotches) {
            numbers.add(rotorsAndNotch.getValue());
        }
        return numbers;
    }
}
