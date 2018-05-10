package EnigmaCracking;

import XmlParsing.DictionaryXmlParser;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {

    private Set<String> theDictionary;

    public Dictionary(){
        theDictionary = new HashSet<>();
        theDictionary = DictionaryXmlParser.getDictionaryXmlParser().getDictionary();
    }

    public boolean checkIfExists(String word){
        return theDictionary.contains(word.toUpperCase());
    }
}
