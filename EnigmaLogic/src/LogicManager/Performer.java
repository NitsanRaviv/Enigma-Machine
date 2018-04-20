package LogicManager;

import Machine.MachineProxy;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.util.List;

public class Performer implements LogicApi {
    private MachineProxy machineProxy;
    private static Performer performer;

    @Override
    public boolean loadMachineFromXml(String path, String msg) {
        try {
            machineProxy = MachineXmlParser.parseXmlToMachineProxy(path);
        }catch (JAXBException je) {
            return false;
        }

        return true;
    }

    @Override
    public String processInput(String input) {
        ////Temp Code!!!!/////
        machineProxy.setChosenRotors(new Pair<>(2, 3), new Pair<>(0, 3)); //left to right
        machineProxy.setChosenReflector(0);
        /////Temp code finish////
        List<Character> processedInput =  machineProxy.encryptCode(input);
        return processedInput.toString();
    }

    @Override
    public String resetCode() {
        machineProxy.setMachineToInitialState();
        return null;
    }

    @Override
    public String getMachineSpecification()
    {
        String spec = machineProxy.toString();
        return spec;
    }


    public static Performer getPerformer(){
        if(performer == null)
        {
            performer = new Performer();
        }
        return performer;
    }
}
