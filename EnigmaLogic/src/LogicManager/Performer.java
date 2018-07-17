package LogicManager;

import EnigmaCracking.DM;
import EnigmaCracking.DMadapter;
import EnigmaCracking.Tasks.TaskLevels;
import LogicManager.InitialCode.InitialCodeParser;
import Machine.MachineProxy;
import Utilities.LanguageInterpeter;
import Utilities.RomanInterpeter;
import XmlParsing.DictionaryXmlParser;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;
import sun.nio.cs.ext.MacThai;

import javax.xml.bind.JAXBException;
import java.math.MathContext;
import java.util.List;
import java.util.Random;

public class Performer {
    public MachineProxy getMachineProxy() {
        return machineProxy;
    }

    private MachineProxy machineProxy;
    private DMadapter dmAdapter;
    private String xmlFilePath;


    public String loadMachineFromXml(String path) {
        try {
            machineProxy = MachineXmlParser.parseXmlToMachineProxy(path);
        }catch (JAXBException je) {
            return ErrorsMessages.errGetMachine;
        }
        xmlFilePath = path;
        return ErrorsMessages.noErrors;
    }

    public String processInput(String input) {
        List<Character> processedInput =  machineProxy.encryptCode(input);
        return processedInput.toString();
    }

    public String resetCode() {
        machineProxy.setMachineToInitialState();
        return null;
    }

    public void setInitialCode(String[] rotorIds, String[] rotorMap, String chosenReflector) {
        List<Pair<Integer, Integer>> rotorsAndNotch = InitialCodeParser.parseRotors(rotorIds, rotorMap, machineProxy.getLanguageInterpeter());
        Pair<Integer, Integer>rotorsAndNotchArr [] = new Pair[rotorsAndNotch.size()];
        int index = 0;
        for(Pair p : rotorsAndNotch){
          rotorsAndNotchArr[index] = p;
          index++;
        }
        machineProxy.setChosenRotors(rotorsAndNotchArr);
        machineProxy.setChosenReflector(chosenReflector);
        machineProxy.isMachineSet(true);
    }

    public String setRandomMachineCode(){
        Random random = new Random();
        boolean exists = false;
        int rotorId = 0;
        int maxRotorNum = Math.min(5, machineProxy.getNumRotors());
        int rotorNum  = machineProxy.getAppliedRotors();
        int insertedrotors = 0;
        Pair<Integer, Integer>rotorsAndNotchArr [] = new Pair[rotorNum];
        while (rotorNum > insertedrotors) {
            rotorId = Math.abs(random.nextInt()) % machineProxy.getNumRotors() + 1;
            for (Pair<Integer, Integer> p : rotorsAndNotchArr) {
                if (p != null) {
                    if (p.getKey() == rotorId)
                        exists = true;
                }
            }
                if(exists == false){
                    rotorsAndNotchArr[insertedrotors] = new Pair<>(rotorId, Math.abs(random.nextInt()) % machineProxy.getLanguageInterpeter().getLanguageAsNumbers().size() + 1);
                    insertedrotors++;
                }
                exists = false;
        }
        machineProxy.setChosenRotors(rotorsAndNotchArr);
        int randomRefl = Math.abs(random.nextInt());
        Integer reflRand = randomRefl % machineProxy.getNumReflectors() + 1;
        machineProxy.setChosenReflector(RomanInterpeter.numToRoman(reflRand));
        machineProxy.isMachineSet(true);
        return machineProxy.getCurrentCode();
    }

    public List<String> getMachineSpecification()
    {
        List<String> spec = machineProxy.getMachineSpecifications();
        return spec;
    }

    public String getStatistics(){
        return this.machineProxy.getStatistics();
    }


    public int getTaskDifficulty(int chosenTaskLevel) {
        int res;
        int abcSize = machineProxy.getLanguage().length;
        int selectedRotorsQuantity = machineProxy.getAppliedRotors();
        int allRotorsQuantity = machineProxy.getNumRotors();
        int reflectorsQuantity = machineProxy.getNumReflectors();
        int factorial = getFactorial(selectedRotorsQuantity);
        int rotorosOptions = binomialCoefficient(selectedRotorsQuantity, allRotorsQuantity);
        res = (int)Math.pow(abcSize, selectedRotorsQuantity);

        switch (chosenTaskLevel) {
            case TaskLevels.levelEasy:
                break;
            case TaskLevels.levelMedium:
                res *= reflectorsQuantity;
                break;
            case TaskLevels.levelHard:
                res *= factorial * reflectorsQuantity;
                break;
            case TaskLevels.levelImpossible:
                res *= rotorosOptions * factorial * reflectorsQuantity;
                break;

        }

        return res;
    }

    private int binomialCoefficient(int k, int n) {
        int nFactoial = getFactorial(n);
        int kFactoial = getFactorial(k);
        int nMinuskFactoial = getFactorial(n-k);

        return (nFactoial / (kFactoial*nMinuskFactoial));
    }

    private int getFactorial(int selectedRotorsQuantity) {
        int res = 1;
        while (selectedRotorsQuantity > 1) {
            res *= selectedRotorsQuantity;
            selectedRotorsQuantity--;
        }

        return res;
    }

    public void startDM(String stringToEncrypt, int numberOfAgents, int missionSize, int chosenTaskLevel) {
        machineProxy.setMachineToInitialState();
        String stringToDecrypt = machineProxy.encryptCodeToString(stringToEncrypt.toUpperCase());
        machineProxy.setMachineToInitialState();
        dmAdapter = new DMadapter(machineProxy,DictionaryXmlParser.getDictionaryXmlParser().getDictionary(xmlFilePath),
                stringToDecrypt, numberOfAgents,missionSize, chosenTaskLevel);

        dmAdapter.start();
    }


    public boolean dmStillRunning() {
        return dmAdapter.dmStillRunning();
    }

    public void setTotalTasksOptions(int numberOfTotalTasks) {
        dmAdapter.setTotalTasksOptions(numberOfTotalTasks);
    }

    public String getStatusOfDecryption() {
        return dmAdapter.getHalfWayInfos().toString();
    }

    public void delayProcess() {
        dmAdapter.suspendDM();
    }

    public void resumeProcess() {
        dmAdapter.unSuspendDM();
    }

    public void stopDM() {
        dmAdapter.stopDM();
    }

    public LanguageInterpeter getLanguageInterpeter() {
        return machineProxy.getLanguageInterpeter();
    }
}
