package LogicTests;

import Machine.MachineProxy;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import LogicManager.LogicApi;

import javax.xml.bind.JAXBException;
import java.util.List;

public class XmlParsingTests {
    @Test
    public void testMachineJaxbParser()
    {
        List<Character> resCharacters;
        MachineProxy mp = null;
        try {
             mp = MachineXmlParser.parseXmlToMachineProxy("ex1-sanity-small.xml");
        } catch (JAXBException e) {
            e.printStackTrace();
        }


        mp.setChosenRotors(new Pair<>(2, 3), new Pair<>(0, 3)); //left to right
        mp.setChosenReflector(0);
        resCharacters = mp.encryptCode("AABBCCDDEEFF");
        Assert.assertEquals(resCharacters.toString(), "[B, D, E, A, B, D, A, C, D, F, A, C]");

        mp.setMachineToInitialState();
        resCharacters = mp.encryptCode("AABBCCDDEEFF");
        Assert.assertEquals(resCharacters.toString(), "[B, D, E, A, B, D, A, C, D, F, A, C]");
    }

    @Test
    public void loadMachineWithLogicApi() {
        try {
            LogicApi.loadMachineFromXml("ex1-sanity-small.xml");
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(LogicApi.getMachineSpecification());
    }
}
