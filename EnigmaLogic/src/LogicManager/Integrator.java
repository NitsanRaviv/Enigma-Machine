package LogicManager;

import Machine.MachineProxy;
import Utilities.LanguageInterpeter;
import XmlParsing.JaxbClasses.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class Integrator {
    private Tester tester;
    private Performer performer;
    public Dictionary getDictionary(){
        return tester.getDictionary();
    }

    public List<String> getMachineSpecification() {
        List<String> res = new ArrayList<>();
        try {
            res = performer.getMachineSpecification();
        }
        catch (Exception e)
        {
            res.add(ErrorsMessages.errNoMachine);
        }

        return res;
    }

    public Integrator(){
        tester = new Tester();
        performer = new Performer();
    }

    public String loadMachineFromXml(String path) {

        String msg;

        msg = doAllFileProperChecks(path);
        if(msg != ErrorsMessages.noErrors)
            return msg;

        msg = performer.loadMachineFromXml(path);
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

        if(!(tester.NumberOfAgentsValid()))
            return ErrorsMessages.errAgents;

        return ErrorsMessages.noErrors;
    }

    public String processInput(String input) {
        String res;
        try {
            res = performer.processInput(input);
        }
        catch (Exception e)
        {
            return ErrorsMessages.errNoMachine;
        }

        return res;
    }

    public String resetCode() {
        try {
            performer.resetCode();
        }
        catch (Exception e)
        {
            return ErrorsMessages.errNoMachine;
        }

        return null;
    }

    public void setInitialCode(String[] rotors, String[] rotorMap, String chosenReflector) {
        performer.setInitialCode(rotors, rotorMap, chosenReflector);
    }

    public String setRandomMachineCode() {
        return performer.setRandomMachineCode();
    }

    public boolean checkValidOfProcessInput(String input) {
        String machineLanguage = getMachineLanguage();

        for (char ch : input.toCharArray()) {
            if (machineLanguage.indexOf(ch) == -1) {
                System.out.println("Incorrect input. you can enter only letters from the machine׳s language.");
                return false;
            }
        }

        return true;
    }

    public String getMachineLanguage(){
       return  String.valueOf(performer.getMachineProxy().getLanguage());
    }
    public LanguageInterpeter getLanguageInterpeter(){
        return performer.getLanguageInterpeter();
    }

    public String getStatistics(){
        return performer.getStatistics();
    }


    public boolean checkInitialRotors(String[] rotors) {
        return tester.allTheRotorIdsExists(rotors);
    }

    public boolean checkInitialRotorsMap(String[] rotorMap) {
       char[] lang = performer.getMachineProxy().getLanguage();
        return tester.allTheRotorsInitialValid(rotorMap,lang);
    }

    public boolean checkChosenReflector(String chosenReflector) {
        return tester.TheReflectorIdExists(chosenReflector);
    }

    public MachineProxy getMachine() {
        return performer.getMachineProxy();
    }

    public boolean checkStringForAutomaticDecoding(String input) {
        return tester.allWordsFromDictionary(input);
    }

    public String cleanStringFromExcludeChars(String input) {
        return tester.getCleanString(input);
    }

    public int getNumberOfAgents() {
        return tester.getNumberOfAgents();
    }

    public int getTaskDifficulty(int chosenTaskLevel) {
        return performer.getTaskDifficulty(chosenTaskLevel);
    }

    public void startTheDecrypt(String stringToDecrypt, int numberOfAgents, int missionSize, int chosenTaskLevel) {
        performer.startDM(stringToDecrypt,numberOfAgents,missionSize,chosenTaskLevel);
    }

    public String getStatusOfDecryption() {
       return performer.getStatusOfDecryption();
    }

    public boolean dmStillRunning() {
       return performer.dmStillRunning();
    }

    public void setTotalTasksOptions(int numberOfTotalTasks) {
        performer.setTotalTasksOptions(numberOfTotalTasks);
    }

    public void delayProcess() {
        performer.delayProcess();
    }

    public void resumeProcess() {
        performer.resumeProcess();
    }

    public void stopProcess() {
        performer.stopDM();
    }

}
