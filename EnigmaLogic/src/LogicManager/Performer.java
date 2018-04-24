package LogicManager;

import LogicManager.InitialCode.InitialCodeParser;
import Machine.MachineProxy;
import Utilities.RomanInterpeter;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Random;

public class Performer implements LogicApi {
    public MachineProxy getMachineProxy() {
        return machineProxy;
    }

    private MachineProxy machineProxy;
    private static Performer performer;

    @Override
    public String loadMachineFromXml(String path) {
        try {
            machineProxy = MachineXmlParser.parseXmlToMachineProxy(path);
        }catch (JAXBException je) {
            return ErrorsMessages.errGetMachine;
        }

        return ErrorsMessages.noErrors;
    }

    @Override
    public String processInput(String input) {
        List<Character> processedInput =  machineProxy.encryptCode(input);
        return processedInput.toString();
    }

    @Override
    public String resetCode() {
        machineProxy.setMachineToInitialState();
        return null;
    }

    @Override
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
        int rotorNum  = Math.abs(random.nextInt()) % maxRotorNum + 1;
        int insertedrotors = 0;
        if(rotorNum == 1)
            rotorNum++;
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

    @Override
    public List<String> getMachineSpecification()
    {
        List<String> spec = machineProxy.getMachineSpecifications();
        return spec;
    }

    public String getStatistics(){
        return this.machineProxy.getStatistics();
    }



    public static Performer getPerformer(){
        if(performer == null)
        {
            performer = new Performer();
        }
        return performer;
    }


}
