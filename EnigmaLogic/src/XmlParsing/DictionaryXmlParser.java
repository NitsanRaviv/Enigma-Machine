package XmlParsing;

import XmlParsing.JaxbClasses.Dictionary;

import java.util.HashSet;
import java.util.Set;


public class DictionaryXmlParser {

    private Dictionary jabDictionary;
    private String excludeChars;
    private String words;
    private static DictionaryXmlParser dictionaryXmlParser;

    public Set<String> getDictionary(){

        Set<String> resDictionary = new HashSet<>();
        excludeChars =  jabDictionary.getExcludeChars();
        words = jabDictionary.getWords();
        String wordsWithoutExcludeChars = words.replaceAll(excludeChars,"");
        String [] allWords = wordsWithoutExcludeChars.split(" ");

        for (String word : allWords)
            resDictionary.add(word.toUpperCase());

        return resDictionary;
    }

    public static DictionaryXmlParser getDictionaryXmlParser(){
        if(dictionaryXmlParser == null)
        {
            dictionaryXmlParser = new DictionaryXmlParser();
        }
        return dictionaryXmlParser;
    }
}
