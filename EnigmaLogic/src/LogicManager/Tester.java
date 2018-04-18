package LogicManager;

import XmlParsing.JaxbClasses.*;
import XmlParsing.MachineXmlParser;

import javax.xml.bind.JAXBException;
import java.util.*;

public class Tester {

    private Enigma enigma;
    private Machine machine;
    private Rotors rotors;
    private List<Rotor> theRotors;

    // first method
    public boolean theFileExists(String filePath){

        String extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());

        if(extension == "xml")
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

        return true;
    }

    public boolean lettersAmountIsEven(){

        String letters = machine.getABC();

        if(letters.length() % 2 == 0)
            return true;

        return false;

    }

    public boolean numOfRotorosIsValid(){
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
                mappingFrom.add(mapp.getFrom());
                mappingTO.add(mapp.getFrom());
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

}
