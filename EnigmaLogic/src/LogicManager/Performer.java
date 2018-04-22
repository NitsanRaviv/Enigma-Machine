package LogicManager;

import LogicManager.InitialCode.InitialCodeParser;
import Machine.MachineProxy;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.util.List;

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
        List<Pair<Integer, Integer>> rotorsAndNotch = InitialCodeParser.parseRotors(rotorIds, rotorMap);
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
