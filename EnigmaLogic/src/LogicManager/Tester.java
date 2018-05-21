package LogicManager;

import XmlParsing.DictionaryXmlParser;
import XmlParsing.JaxbClasses.*;
import XmlParsing.JaxbClasses.Dictionary;
import XmlParsing.MachineXmlParser;
import agentUtilities.EnigmaDictionary;

import javax.xml.bind.JAXBException;
import java.util.*;

public class Tester {

    private static Enigma enigma;
    private Machine machine;
    private Rotors rotors;
    private List<Rotor> theRotors;
    private Reflectors reflectors;
    private List<Reflector> theReflectors;
    private Decipher decipher;
    private final int maxNumOfAgents = 50;
    private final int minNumOfAgents = 2;

    public static Dictionary getDictionary(){
        return enigma.getDecipher().getDictionary();
    }

    // first method
    public boolean theFileIsXml(String filePath){

        String extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());

        if(extension.equals("xml"))
            return true;

        return false;
    }

    // second method
    public boolean getMachine(String filePath)
    {
        try{
            enigma = MachineXmlParser.parseXmltoJaxbMachine(filePath);
        }
        catch (JAXBException jbe)
        {
            return false;
        }

        machine = enigma.getMachine();
        rotors = machine.getRotors();
        theRotors = rotors.getRotor();
        reflectors = machine.getReflectors();
        theReflectors = reflectors.getReflector();
        decipher = enigma.getDecipher();

        return true;
    }

    public boolean lettersAmountIsEven(){

        String letters = machine.getABC();
        letters = letters.replaceAll("\n","");
        letters = letters.replaceAll("\t","");

        if(letters.length() % 2 == 0)
            return true;

        return false;

    }

    public boolean amountOfRotorosIsValid(){
        int rotorsCount = machine.getRotorsCount();

        if(rotorsCount <= theRotors.size())
            return true;

        return false;
    }

    public boolean moreThenOneRotor() {
        if(machine.getRotorsCount() < 2)
            return false;

        return true;
    }

    public boolean allRotorsIdsValid(){

        List<Integer> rotorsIds = new ArrayList<>();
        Integer rotorsCount = 1;

        for (Rotor rotor : theRotors)
            rotorsIds.add(rotor.getId());

        Collections.sort(rotorsIds);

        for(Integer id : rotorsIds)
        {
            if(id != rotorsCount)
                return false;

            rotorsCount++;
        }

        return true;
    }

    public boolean NoDuplicateMappings(){
        List<Mapping> rotorMapping;
        Set<String> mappingTO = new HashSet<>();
        Set<String> mappingFrom = new HashSet<>();
        boolean res = true;

        for (Rotor rotor : theRotors)
        {
            rotorMapping = rotor.getMapping();
            for(Mapping mapp : rotorMapping)
            {
                mappingFrom.add(mapp.getLeft());
                mappingTO.add(mapp.getRight());
            }
            if(rotorMapping.size() != mappingFrom.size() ||
                    rotorMapping.size() != mappingTO.size())
            {
                res = false;
                break;
            }
        }

        return res;
    }

    public boolean notchIsValid(){

        int numOfLetters = machine.getABC().length();

        for(Rotor rotor : theRotors)
        {
            if(rotor.getNotch() >= numOfLetters)
                return false;
        }

        return true;
    }

    public boolean allReflectorsIdsValid(){

        boolean res = true;
        List<String> reflectorsIds = new ArrayList<>();
        Set<String> idsNoDuplicate = new HashSet<>();

        for (Reflector reflector : theReflectors)
            reflectorsIds.add(reflector.getId());

        for(String id : reflectorsIds)
        {
            if(!(id.equals("I")) && !(id.equals("II")) && !(id.equals("III"))
                    && !(id.equals("IV")) && !(id.equals("V")))
                return false;

            idsNoDuplicate.add(id);
        }// if we here -> all the ids are roman letters

        if(idsNoDuplicate.size() != reflectorsIds.size())//check if we have duplicate
            return false;

        return true;
    }

    public boolean allReflectVaild(){

        List<Reflect> theReflects;

        for(Reflector reflector : theReflectors)
        {
            theReflects = reflector.getReflect();

            for(Reflect reflect : theReflects)
                if(reflect.getInput() == reflect.getOutput())
                    return false;
        }

        return true;
    }

    public boolean allTheRotorIdsExists(String[] rotors) {
        List<Integer> rotorsIds = new ArrayList<>();
        boolean res = false;

        for (Rotor rotor : theRotors)
            rotorsIds.add(rotor.getId());

        for(String idToCheck : rotors)
        {
            for (Integer id : rotorsIds)
            {
                if(idToCheck.equals(id.toString()))
                {
                    res = true;
                    break;
                }
            }

            if(!res)
                return false;

            res = false;
        }

        return true;
    }

    public boolean allTheRotorsInitialValid(String[] rotorMap, char[] lang) {
        boolean res = false;

        for(String charToCheck : rotorMap)
        {
            for (char letter : lang)
            {
                if(charToCheck.equals(String.valueOf(letter)))
                {
                    res = true;
                    break;
                }
            }

            if(!res)
                return false;

            res = false;
        }

        return true;

    }

    public boolean TheReflectorIdExists(String chosenReflector) {
        List<String> reflectorsIds = new ArrayList<>();

        for (Reflector reflector : theReflectors)
            reflectorsIds.add(reflector.getId());

        for(String refID : reflectorsIds)
        {
            if(chosenReflector.equals(refID))
                return true;
        }

        return false;
    }

    public boolean NumberOfAgentsValid(){

        int numOfAgents = decipher.getAgents();

        if(numOfAgents >= minNumOfAgents && numOfAgents <= maxNumOfAgents)
            return true;

        return false;
    }

    public boolean allWordsFromDictionary(String input) {

        EnigmaDictionary enigmaDictionary = DictionaryXmlParser.getDictionaryXmlParser().getDictionary();
        String [] allWords = input.split(" ");

        for (String word : allWords)
        {
           if(!(enigmaDictionary.checkIfExists(word)))
               return false;
        }

        return true;
    }

    String getCleanString(String input)
    {
        String excludeChars = decipher.getDictionary().getExcludeChars();
        return DictionaryXmlParser.getDictionaryXmlParser().replaceExcludeChars(excludeChars,input);
    }

    public int getNumberOfAgents() {
        return decipher.getAgents();
    }
}
