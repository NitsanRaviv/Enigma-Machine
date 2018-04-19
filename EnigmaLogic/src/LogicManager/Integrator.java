package LogicManager;

public class Integrator implements LogicApi,ErrorsMessages {
    private Tester tester;

    @Override
    public String getMachineSpecification() {
       return Performer.getPerformer().getMachineSpecification();
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
       return Performer.getPerformer().processInput(input);
    }

    @Override
    public void resetCode() {
        Performer.getPerformer().resetCode();
    }
}
