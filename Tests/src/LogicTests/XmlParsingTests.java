package LogicTests;

import Machine.MachineProxy;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.util.List;

public class XmlParsingTests {
    @Test
    public void testMachineJaxbParser()
    {
        List<Character> resCharacters;
        MachineXmlParser mxp = new MachineXmlParser();
        MachineProxy mp = null;
        try {
             mp = mxp.parseXmlToMachineProxy("ex1-sanity-small.xml");
        } catch (JAXBException e) {
            e.printStackTrace();
        }


        mp.setChosenRotors(new Pair<>(2, 3), new Pair<>(0, 3)); //left to right
        mp.setChosenReflector(0);
        resCharacters = mp.encryptCode("AABBCCDDEEFF");
        System.out.println(resCharacters);

        mp.setMachineToInitialState();
        resCharacters = mp.encryptCode("AABBCCDDEEFF");
        System.out.println(resCharacters);


    }
}
