package LogicTests;

import EnigmaCracking.Dictionary;
import LogicManager.Tester;
import Machine.MachineProxy;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import LogicManager.Performer;

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

        mp.setChosenRotors(new Pair<>(2, 3), new Pair<>(1, 3)); //left to right
        mp.setChosenReflector("I");
        resCharacters = mp.encryptCode("AABBCCDDEEFF");
        Assert.assertEquals(resCharacters.toString(), "[B, D, E, A, B, D, A, C, D, F, A, C]");

        mp.setMachineToInitialState();
        resCharacters = mp.encryptCode("AABBCCDDEEFF");
        Assert.assertEquals(resCharacters.toString(), "[B, D, E, A, B, D, A, C, D, F, A, C]");
    }

    @Test
    public void loadMachineWithLogicApi() {
        try {
            Performer.getPerformer().loadMachineFromXml("ex1-sanity-small.xml");
        }catch (Exception e) {
            e.printStackTrace();
        }
        //Performer.getPerformer().setInitialCode("1,3".split(","), "2,3".split(","),"I");
        System.out.println(Performer.getPerformer().getMachineSpecification());
    }

    @Test
    public void testInitialCodeParserAndPerformer(){
        Performer.getPerformer().loadMachineFromXml("ex1-sanity-small.xml");
        Performer.getPerformer().setInitialCode("2,1".split(","), "C,C".split(","),"I");
        Assert.assertEquals(Performer.getPerformer().processInput("AABBCCDDEEFF"), "[B, D, E, A, B, D, A, C, D, F, A, C]");
        System.out.println(Performer.getPerformer().getStatistics());
    }

    @Test
    public void dictionaryParserTest(){
        Tester test = new Tester();
        Performer.getPerformer().loadMachineFromXml("ex2-basic.xml");
        boolean temp = test.getMachine("ex2-basic.xml");
        Dictionary dictionary = new Dictionary();

        System.out.println(dictionary.checkIfExists("whom?"));
        System.out.println(dictionary.checkIfExists("whom"));
        System.out.println(dictionary.checkIfExists("KomBat"));
        System.out.println(dictionary.checkIfExists("swap"));
        System.out.println(dictionary.checkIfExists("Nice"));
        System.out.println(dictionary.checkIfExists("poland"));


    }
}
