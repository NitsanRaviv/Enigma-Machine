package LogicManager;

import java.util.ArrayList;
import java.util.List;

public class Integrator implements LogicApi {
    private Tester tester;
    private static Integrator integrator ;

    @Override
    public List<String> getMachineSpecification() {
        List<String> res = new ArrayList<>();
        try {
            res = Performer.getPerformer().getMachineSpecification();
        }
        catch (Exception e)
        {
            res.add(ErrorsMessages.errNoMachine);
        }

        return res;
    }

    private Integrator(){
        tester = new Tester();
    }

    @Override
    public boolean loadMachineFromXml(String path, String msg) {

        if(!(doAllProperChecks(msg,path)))
            return false;

        if(!(Performer.getPerformer().loadMachineFromXml(path,null)))
            return false;

        return true;
    }

    private boolean doAllProperChecks(String msg, String path) {

        if(!(fileValidChecks(msg,path)))
            return false;
        if(!(rotorsChecks(msg,path)))
            return false;
        if(!(reflectorChecks(msg,path)))
            return false;

        msg = null;
        return true;
    }

    private boolean reflectorChecks(String msg, String path)
    {
        if(!(tester.allReflectorsIdsValid()))
        {
            msg = ErrorsMessages.errIDsReflector;
            return false;
        }
        if(!(tester.allReflectVaild()))
        {
            msg = ErrorsMessages.errReflect;
            return false;
        }

        msg = null;
        return true;
    }

    private boolean rotorsChecks(String msg, String path)
    {
        if(!(tester.amountOfRotorosIsValid()))
        {
            msg = ErrorsMessages.errAmountOfRotor;
            return false;
        }
        if(!(tester.moreThenOneRotor()))
        {
            msg = ErrorsMessages.errLessThenTwoRotor;
            return false;
        }
        if(!(tester.allRotorsIdsValid()))
        {
            msg = ErrorsMessages.errIDsRotors;
            return false;
        }
        if(!(tester.NoDuplicateMappings()))
        {
            msg = ErrorsMessages.errDuplicateMappings;
            return false;
        }
        if(!(tester.notchIsValid()))
        {
            msg = ErrorsMessages.errNotch;
            return false;
        }

        msg = null;
        return true;
    }

    private boolean fileValidChecks(String msg, String path)
    {
        if(!(tester.theFileIsXml(path)))
        {
            msg = ErrorsMessages.errXML;
            return false;
        }
        if(!(tester.getMachine(path)))
        {
            msg = ErrorsMessages.errGetMachine;
            return false;
        }
        if(!(tester.lettersAmountIsEven()))
        {
            msg = ErrorsMessages.errABCSize;
            return false;
        }

        msg = null;
        return true;
    }

    @Override
    public String processInput(String input) {
        String res;
        try {
            res = Performer.getPerformer().processInput(input);
        }
        catch (Exception e)
        {
            return ErrorsMessages.errNoMachine;
        }

        return res;
    }

    @Override
    public String resetCode() {
        try {
            Performer.getPerformer().resetCode();
        }
        catch (Exception e)
        {
            return ErrorsMessages.errNoMachine;
        }

        return null;
    }

    @Override
    public boolean setInitialCode(String[] rotors, String[] rotorMap, String chosenReflector) {
        return false;
    }

    public static Integrator getIntegrator(){
        if(integrator == null)
        {
            integrator = new Integrator();
        }
        return integrator;
    }
}
