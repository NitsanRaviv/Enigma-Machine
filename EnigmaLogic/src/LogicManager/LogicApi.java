package LogicManager;

import Machine.MachineProxy;
import XmlParsing.MachineXmlParser;
import javax.xml.bind.JAXBException;

public class LogicApi {
    private static MachineProxy machineProxy;
    public static void loadMachineFromXml(String path) {
        try {
            machineProxy = MachineXmlParser.parseXmlToMachineProxy(path);
        }catch (JAXBException je) {
            je.printStackTrace();
        }
    }

    public static String getMachineSpecification()
    {
        String spec = machineProxy.toString();
        return spec;
    }
}
