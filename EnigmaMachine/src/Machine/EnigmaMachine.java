package Machine;

import Parts.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class EnigmaMachine {
   private List<Rotor> rotors;
   private List<Reflector> reflectors;
   private List<Rotor> chosenRotors;
   private Reflector chosenReflector;
   private int chosenReflNumber;

    public EnigmaMachine(List<Rotor> rotors, List<Reflector> reflectors) {
        this.rotors = new ArrayList<>(rotors);
        this.reflectors = new ArrayList<>(reflectors);
    }

    public int getChosenRotorsSize(){
        return this.chosenRotors.size();
    }

    public EnigmaMachine setChosenRotors(Pair<Integer, Integer>... rotorAndNotch)
    {
        this.chosenRotors = new ArrayList<>(rotorAndNotch.length);
        for(Pair p : rotorAndNotch)
        {
            chosenRotors.add(this.getRotorById((int)p.getKey()));
            this.getRotorById((int)p.getKey()).setPosition((int)p.getValue());
        }
        for(int i = chosenRotors.size() - 1; i > 0; i--)
        {
            this.chosenRotors.get(i).setLeftRotor(this.chosenRotors.get(i - 1));
        }
        return this;
    }

    private Rotor getRotorById(int id) {
        for(Rotor rotor : rotors){
            if(rotor.getId() == id)
                return rotor;
        }
        return null;
    }

    public EnigmaMachine setChosenReflector(int reflectorNum)
    {
        this.chosenReflector = getReflectorByNum(reflectorNum);
        this.chosenReflNumber = reflectorNum;
        return this;
    }

    private Reflector getReflectorByNum(int reflectorNum) {
        for(Reflector refl : reflectors){
            if (refl.getId() == reflectorNum)
                return refl;
        }
        return null;
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

    public Integer encryptLetter(Integer letterToEncrypt) {
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

    public List<Rotor> getRotors(){
        return this.rotors;
    }

    public int getReflectorsSize() {
        return this.reflectors.size();
    }

    public List<Rotor> getChosenRotors() {
        return chosenRotors;
    }

    public int getChosenReflector() {
        return chosenReflNumber;
    }

    public int getRotorsSize() {
        return this.rotors.size();
    }

    public int getNumRotors() {
        return this.rotors.size();
    }

    public int getNumReflectors() {
        return this.reflectors.size();
    }

    @Override
    protected EnigmaMachine clone() throws CloneNotSupportedException {
        EnigmaMachine clone = new EnigmaMachine(new ArrayList<>(rotors), new ArrayList<>(reflectors));
        return clone;
    }
}
