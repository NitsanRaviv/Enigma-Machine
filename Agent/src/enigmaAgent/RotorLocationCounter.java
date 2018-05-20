package enigmaAgent;

import Utilities.LanguageInterpeter;
import javafx.util.Pair;
import java.util.List;

public class RotorLocationCounter {
    private LanguageInterpeter languageInterpeter;
    private Pair<Integer,Integer>[] currentRotorAndNotches;
    private Pair<Integer,Integer>[] initialRotorAndNotches;


    public RotorLocationCounter(LanguageInterpeter languageInterpeter, Pair<Integer,Integer>[] currentRotorAndNotches){
        this.languageInterpeter = languageInterpeter;
        this.currentRotorAndNotches = currentRotorAndNotches;
        //check if clone works
        initialRotorAndNotches = currentRotorAndNotches.clone();
    }

    //TODO:: implement constructors for various mission level scenarios
    public RotorLocationCounter(LanguageInterpeter languageInterpeter,int numRotors){
        Pair<Integer,Integer>[] rotorsAndLocations = new Pair[numRotors];
        for (int i = 0; i < numRotors; i++) {
            rotorsAndLocations[i] = new Pair<>(i+1, languageInterpeter.getLanguageAsNumbers().get(0));
        }
        this.languageInterpeter = languageInterpeter;
        this.currentRotorAndNotches = rotorsAndLocations;
        this.initialRotorAndNotches = rotorsAndLocations.clone();
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

    public void initToFirstState(){
        this.currentRotorAndNotches = this.initialRotorAndNotches.clone();
    }

    public void setRotorsAndLocations(Pair<Integer,Integer>[] rotorsAndLocations) {
        this.currentRotorAndNotches = rotorsAndLocations;
        this.initialRotorAndNotches = rotorsAndLocations.clone();
    }

    public Pair<Integer,Integer>[] getCurrentRotorsAndLocations() {
        return currentRotorAndNotches.clone();
    }
}
