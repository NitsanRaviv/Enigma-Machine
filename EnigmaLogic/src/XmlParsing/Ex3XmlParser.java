package XmlParsing;

import XmlParsing.JaxbClasses.Decipher;
import XmlParsing.JaxbClasses.Battlefield;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class Ex3XmlParser {

    public static Decipher parseXmltoJaxbDecipher(String filePath) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = null;
        Decipher decipher = null;
        File xmlFilePath = new File(filePath);
        JAXBContext jaxbContext = null;
        jaxbContext = JAXBContext.newInstance(Decipher.class);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        decipher = (Decipher) jaxbUnmarshaller.unmarshal(xmlFilePath);
        return decipher;
    }

    public static Battlefield parseXmltoJaxbBattlefield(String filePath) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = null;
        Battlefield battlefield = null;
        File xmlFilePath = new File(filePath);
        JAXBContext jaxbContext = null;
        jaxbContext = JAXBContext.newInstance(Decipher.class);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        battlefield = (Battlefield) jaxbUnmarshaller.unmarshal(xmlFilePath);
        return battlefield;
    }
}
