package Tasks;

import javafx.util.Pair;

import java.util.List;

public class EasyTask implements BasicTask {

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

    public String getStringToEncrypt() {
        return stringToEncrypt;
    }
}
