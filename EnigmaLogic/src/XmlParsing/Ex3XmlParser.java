package XmlParsing;

import XmlParsing.JaxbClasses.Decipher;
import XmlParsing.JaxbClasses.Battlefield;
import XmlParsing.JaxbClasses.Enigma;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class Ex3XmlParser {

    public static Decipher parseXmltoJaxbDecipher(String filePath) throws JAXBException {
        Enigma enigma = MachineXmlParser.parseXmltoJaxbMachine(filePath);
        return enigma.getDecipher();
    }



    }
}
