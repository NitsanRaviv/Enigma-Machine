package agentUtilities;
import java.io.Serializable;
import java.util.Set;

public class EnigmaDictionary implements Serializable {

    private Set<String> theDictionary;

    public EnigmaDictionary(Set<String> dictionary ){
        theDictionary = dictionary;
    }

    public boolean checkIfExists(String word){
        return theDictionary.contains(word.toUpperCase());
    }

}
