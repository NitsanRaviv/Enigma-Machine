package XmlParsing;

import Machine.MachineBuilder;
import Machine.MachineProxy;
import XmlParsing.JaxbClasses.Enigma;
import XmlParsing.JaxbClasses.Machine;

import javax.xml.bind.*;
import java.io.File;


public class MachineXmlParser {

    private Enigma parseXmltoJaxbMachine(String filePath) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = null;
        Enigma jaxbEnigma = null;
        File xmlFilePath = new File(filePath);
        JAXBContext jaxbContext = null;
        jaxbContext = JAXBContext.newInstance(Enigma.class);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        jaxbEnigma = (Enigma) jaxbUnmarshaller.unmarshal(xmlFilePath);
        return jaxbEnigma;
    }

    public MachineProxy parseXnlToMachineProxy(String filePath) throws JAXBException {
        Enigma jaxbEnigma = parseXmltoJaxbMachine(filePath);
        Machine jaxbMachine = jaxbEnigma.getMachine();
        MachineBuilder machineBuilder = new MachineBuilder();
        machineBuilder.initMachine(jaxbMachine.getRotorsCount(),jaxbMachine.)
    }
}
