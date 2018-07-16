package Machine;

import Parts.*;
import Utilities.LanguageInterpeter;
import Utilities.RomanInterpeter;

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
    private int appliedRotorsNum;

    public MachineBuilder initMachine(char[] language, int rotorsCount)
    {
        rotors = new ArrayList<>();
        reflectors = new ArrayList<>();
        languageInterpeter = new LanguageInterpeter(language);
        this.appliedRotorsNum = rotorsCount;
        return this;
    }

    public MachineBuilder setRotor(String rightEntries, String leftEntries, int notch, int id)
    {
        Rotor rotor = new Rotor(languageInterpeter.lettersToNumbers(leftEntries.toCharArray()), languageInterpeter.lettersToNumbers(rightEntries.toCharArray()), notch, id);
        rotors.add(rotor);
        return this;
    }

    public MachineBuilder setReflector(int[] from, int[] to, String id)
    {
        Map<Integer, Integer> oneWayMapping = new HashMap<>(from.length);
        for(int i = 0; i < from.length; i++)
        {
            oneWayMapping.put(from[i],to[i]);
        }

        Reflector reflector = new Reflector(oneWayMapping, RomanInterpeter.romanToNum(id));
        reflectors.add(reflector);
        return this;
    }


    public MachineProxy create() {
        machine = new EnigmaMachine(rotors, reflectors);
        machineProxy = new MachineProxy(machine, languageInterpeter, appliedRotorsNum);
        return machineProxy;
    }
}
