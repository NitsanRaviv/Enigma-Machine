package XmlParsing;

import LogicManager.Integrator;
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
        jabDictionary = Integrator.getIntegrator().getDictionary();
        excludeChars =  jabDictionary.getExcludeChars();
        words = jabDictionary.getWords();
        String wordsWithoutExcludeChars = replaceExcludeChars(excludeChars,words);
        String [] allWords = wordsWithoutExcludeChars.split(" ");

        for (String word : allWords)
            resDictionary.add(word.toUpperCase());

        return resDictionary;
    }

    private String replaceExcludeChars(String excludeChars, String words) {

        String res = null;

        for(char excludeChar : excludeChars.toCharArray())
        {
           res =  words.replace("" + excludeChar,"");
        }

        return res;
    }

    public static DictionaryXmlParser getDictionaryXmlParser(){
        if(dictionaryXmlParser == null)
        {
            dictionaryXmlParser = new DictionaryXmlParser();
        }
        return dictionaryXmlParser;
    }
}
