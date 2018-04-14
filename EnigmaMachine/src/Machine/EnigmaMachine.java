package Machine;

import Parts.*;
import javafx.util.Pair;

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

    public EnigmaMachine setChosenRotors(Pair<Integer, Integer>... rotorAndNotch)
    {
        this.chosenRotors = new ArrayList<>(rotorAndNotch.length);
        for(Pair p : rotorAndNotch)
        {
            chosenRotors.add(this.rotors.get((int)p.getKey()));
            this.rotors.get((int)p.getKey()).setPosition((int)p.getValue());
        }
        for(int i = chosenRotors.size() - 1; i > 0; i--)
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

    public void setToInitialState()
    {
        for(Rotor rotor : chosenRotors)
        {
            rotor.setToInitialState();
        }
    }

}
