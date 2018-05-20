package enigmaAgent;

import Utilities.LanguageInterpeter;
import javafx.util.Pair;
import java.util.List;

public class RotorLocationCounter {
    private LanguageInterpeter languageInterpeter;
    private Pair<Integer,Integer>[] currentRotorAndNotches;


    public RotorLocationCounter(LanguageInterpeter languageInterpeter, Pair<Integer,Integer>[] currentRotorAndNotches){
        this.languageInterpeter = languageInterpeter;
        this.currentRotorAndNotches = copyRotorsAndLocations(currentRotorAndNotches);
    }

    private Pair<Integer,Integer>[] copyRotorsAndLocations(Pair<Integer, Integer>[] toCopy) {
        Pair<Integer, Integer>[] clone = new Pair[toCopy.length];
        int index = 0;
        for (Pair<Integer, Integer> copyPair : toCopy) {
            clone[index] = new Pair(copyPair.getKey(), copyPair.getValue());
            index++;
        }
        return clone;
    }


    public Pair<Integer,Integer>[] nextRotorsAndLocations(){
       List<Integer> language = languageInterpeter.getLanguageAsNumbers();
        for (int i = currentRotorAndNotches.length - 1; i >= 0; i--) {
            if (currentRotorAndNotches[i].getValue() != language.get(language.size() - 1)){
                currentRotorAndNotches[i] = new Pair(currentRotorAndNotches[i].getKey(), currentRotorAndNotches[i].getValue() + 1);
                break;
            }
            else //rotor was in final location and now his location will be set to the first character in the language.
                currentRotorAndNotches[i] = new Pair(currentRotorAndNotches[i].getKey(), language.get(0));
        }

        return currentRotorAndNotches;
    }

    public Pair<Integer,Integer>[] nextRotorsAndLocations(int count){
        for (int i = 0; i < count; i++) {
            nextRotorsAndLocations();
        }
        return currentRotorAndNotches;
    }


    public Pair<Integer,Integer>[] getCurrentRotorsAndLocations() {
        return copyRotorsAndLocations(currentRotorAndNotches);
    }

    public void setRotorsAndLocations(Pair<Integer,Integer>[] rotorsAndLocations) {
        this.currentRotorAndNotches = copyRotorsAndLocations(rotorsAndLocations);
    }
}
