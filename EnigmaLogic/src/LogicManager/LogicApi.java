package LogicManager;

import Machine.MachineProxy;
import XmlParsing.MachineXmlParser;
import javax.xml.bind.JAXBException;

public class LogicApi {
    private static MachineProxy machineProxy;
    public static MachineProxy loadMachineFromXml(String path) throws JAXBException {
        machineProxy = MachineXmlParser.parseXmlToMachineProxy(path);
        return machineProxy;
    }

    public static String getMachineSpecification()
    {
        String spec = machineProxy.toString();
        return spec;
    }
}
