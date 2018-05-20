package agentUtilities;
import java.util.Set;

public class EnigmaDictionary {

    private Set<String> theDictionary;

    public EnigmaDictionary(Set<String> dictionary ){
        theDictionary = dictionary;
    }

    public boolean checkIfExists(String word){
        return theDictionary.contains(word.toUpperCase());
    }


}
