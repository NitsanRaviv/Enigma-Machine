package XmlParsing;

import agentUtilities.EnigmaDictionary;
import LogicManager.Integrator;
import XmlParsing.JaxbClasses.Dictionary;

import javax.xml.bind.JAXBException;
import java.util.HashSet;
import java.util.Set;


public class DictionaryXmlParser {

    private Dictionary jabDictionary;
    private String excludeChars;
    private String words;
    private static DictionaryXmlParser dictionaryXmlParser;

    public EnigmaDictionary getDictionary(String filepPath){

        Set<String> resDictionary = new HashSet<>();
        try {
            jabDictionary = MachineXmlParser.parseXmltoJaxbMachine(filepPath).getDecipher().getDictionary();
        }catch (JAXBException e){
            e.printStackTrace();
        }

        excludeChars =  jabDictionary.getExcludeChars();
        words = jabDictionary.getWords();
        String wordsWithoutExcludeChars = replaceExcludeChars(excludeChars,words);
        String [] allWords = wordsWithoutExcludeChars.split(" ");

        for (String word : allWords)
            resDictionary.add(word.toUpperCase());

        return new EnigmaDictionary(resDictionary);
    }

    public String replaceExcludeChars(String excludeChars, String words) {

        for(char excludeChar : excludeChars.toCharArray())
        {
            words =  words.replace("" + excludeChar,"");
        }

        words = words.replaceAll("[\t\n]+","");

        return words;
    }

    public static DictionaryXmlParser getDictionaryXmlParser(){
        if(dictionaryXmlParser == null)
        {
            dictionaryXmlParser = new DictionaryXmlParser();
        }
        return dictionaryXmlParser;
    }
}
