package Machine;

import Utilities.*;
import javafx.util.Pair;

import java.util.List;

public class MachineProxy {
    private EnigmaMachine machine;
    private LanguageInterpeter languageInterpeter;

    public MachineProxy(EnigmaMachine machine, LanguageInterpeter languageInterpeter)
    {
        this.machine = machine;
        this.languageInterpeter = languageInterpeter;
    }

    public List<Character> encryptCode(String codeToEncrypt)
    {
        return languageInterpeter.numberToLetters(machine.encryptCode(languageInterpeter.lettersToNumbers(codeToEncrypt.toCharArray())));
    }

    public void setMachineToInitialState()
    {
        this.machine.setToInitialState();
    }

    public void setChosenRotors(Pair<Integer, Integer>... rotorsAndNotch)
    {
        machine.setChosenRotors(rotorsAndNotch);
    }

    public void setChosenReflector(int reflectorNum)
    {
        machine.setChosenReflector(reflectorNum);
    }
}
