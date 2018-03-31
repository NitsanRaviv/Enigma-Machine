package Machine;

import Parts.*;
import Utilities.LanguageInterpeter;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineBuilder {
    private  EnigmaMachine machine;
    private  MachineProxy machineProxy;
    private  List<Rotor> rotors;
    private  List<Reflector> reflectors;
    private  LanguageInterpeter languageInterpeter;

    public  MachineBuilder initMachine(int rotorNum,int reflectorNum, char[] language)
    {
        rotors = new ArrayList<>(rotorNum);
        reflectors = new ArrayList<>(reflectorNum);
        languageInterpeter = new LanguageInterpeter(language);
        return this;
    }

    public MachineBuilder setRotor(String rightEntries, String leftEntries, int zeezIndex)
    {
        Rotor rotor = new Rotor(languageInterpeter.lettersToNumbers(leftEntries.toCharArray()), languageInterpeter.lettersToNumbers(rightEntries.toCharArray()));
        rotors.add(rotor);
        return this;
    }

    public MachineBuilder setReflector(int[] from, int[] to)
    {
        Map<Integer, Integer> oneWayMapping = new HashMap<>(from.length);
        for(int i = 0; i < from.length; i++)
        {
            oneWayMapping.put(from[i],to[i]);
        }

        Reflector reflector = new Reflector(oneWayMapping);
        reflectors.add(reflector);
        return this;
    }


    public MachineProxy create() {
        machine = new EnigmaMachine(rotors, reflectors);
        machineProxy = new MachineProxy(machine, languageInterpeter);
        return machineProxy;
    }
}
