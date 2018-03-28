package Machine;

import Parts.*;

import java.util.ArrayList;
import java.util.List;


public class EnigmaMachine {
    List<Rotor> rotors;
    List<Reflector> reflectors;
    List<Rotor> chosenRotors;
    Reflector chosenReflector;

    public EnigmaMachine(List<Rotor> rotors, List<Reflector> reflectors) {
        this.rotors = new ArrayList<>(rotors);
        this.reflectors = new ArrayList<>(reflectors);
    }

    public EnigmaMachine setChosenRotors(int[] rotorNumbers)
    {
        this.chosenRotors = new ArrayList<>(rotorNumbers.length);
        for(Integer i : rotorNumbers)
        {
            chosenRotors.add(this.rotors.get(i));
        }
        for(int i = rotors.size() - 1; i > 0; i--)
        {
            this.chosenRotors.get(i).setLeftRotor(this.chosenRotors.get(i - 1));
        }
        return this;
    }

    public EnigmaMachine setChosenReflector(int reflectorNum)
    {
        this.chosenReflector = reflectors.get(reflectorNum);
        return this;
    }



    public List<Integer> encryptCode(List<Integer> numbersToCode)
    {
        List<Integer> res = new ArrayList<>(numbersToCode.size());
        for(Integer letter : numbersToCode)
        {
           res.add(encryptLetter(letter));
        }
        return res;
    }

    private Integer encryptLetter(Integer letterToEncrypt) {
        Integer encryptedLetter = - 1;
        for(int i = chosenRotors.size() - 1; i >=0; i-- )
        {
            if( i == chosenRotors.size() - 1)
            {
                chosenRotors.get(i).rotateRotorOneRound();
            }
            letterToEncrypt = chosenRotors.get(i).outputComingFromRight(letterToEncrypt);
        }
        letterToEncrypt = this.chosenReflector.getReflectedValue(letterToEncrypt);
        for(Rotor rotor : chosenRotors)
        {
            letterToEncrypt = rotor.outputComingFromLeft(letterToEncrypt);
        }
        encryptedLetter =  letterToEncrypt;
        return encryptedLetter;
    }

}
