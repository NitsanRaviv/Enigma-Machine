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
    public String loadMachineFromXml(String path) {

        String msg;

        msg = doAllFileProperChecks(path);
        if(msg != ErrorsMessages.noErrors)
            return msg;

        msg = Performer.getPerformer().loadMachineFromXml(path);
        if(msg != ErrorsMessages.noErrors)
            return msg;

        return ErrorsMessages.noErrors;
    }

    private String doAllFileProperChecks(String path) {
        String msg;

        msg = fileValidChecks(path);
        if(msg != ErrorsMessages.noErrors)
            return msg;

        msg = rotorsChecks();
        if(msg != ErrorsMessages.noErrors)
            return msg;

        msg = reflectorChecks();
        if(msg != ErrorsMessages.noErrors)
            return msg;

        return ErrorsMessages.noErrors;
    }

    private String reflectorChecks()
    {
        if(!(tester.allReflectorsIdsValid()))
            return ErrorsMessages.errIDsReflector;

        if(!(tester.allReflectVaild()))
            return ErrorsMessages.errReflect;

        return ErrorsMessages.noErrors;
    }

    private String rotorsChecks() {
        if (!(tester.amountOfRotorosIsValid()))
            return ErrorsMessages.errAmountOfRotor;

        if (!(tester.moreThenOneRotor()))
            return ErrorsMessages.errLessThenTwoRotor;

        if (!(tester.allRotorsIdsValid()))
            return ErrorsMessages.errIDsRotors;

        if (!(tester.NoDuplicateMappings()))
            return ErrorsMessages.errDuplicateMappings;

        if (!(tester.notchIsValid()))
            return ErrorsMessages.errNotch;

        return ErrorsMessages.noErrors;
    }

    private String fileValidChecks(String path)
    {
        if(!(tester.theFileIsXml(path)))
            return ErrorsMessages.errXML;

        if(!(tester.getMachine(path)))
            return ErrorsMessages.errGetMachine;

        if(!(tester.lettersAmountIsEven()))
            return ErrorsMessages.errABCSize;

        return ErrorsMessages.noErrors;
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
