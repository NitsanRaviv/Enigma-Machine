package LogicTests;

import Machine.MachineProxy;
import XmlParsing.*;
import XmlParsing.JaxbClasses.Battlefield;
import XmlParsing.JaxbClasses.Decipher;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import LogicManager.Performer;

import javax.xml.bind.JAXBException;
import java.util.List;

public class XmlParsingTests {
    private Performer performer = new Performer();
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
            performer.loadMachineFromXml("ex1-sanity-small.xml");
        }catch (Exception e) {
            e.printStackTrace();
        }
        //performer.setInitialCode("1,3".split(","), "2,3".split(","),"I");
        System.out.println(performer.getMachineSpecification());
    }

    @Test
    public void testInitialCodeParserAndPerformer(){
        performer.loadMachineFromXml("ex1-sanity-small.xml");
        performer.setInitialCode("2,1".split(","), "C,C".split(","),"I");
        String a = String.valueOf(performer.processInput("AABBCCDDEEFF"));
        Assert.assertEquals(a, "[B, D, E, A, B, D, A, C, D, F, A, C]");
        System.out.println(performer.getStatistics());
    }

    @Test
    public void testEx3Parser(){
        Battlefield battlefield = null;
        try {
            battlefield = Ex3XmlParser.parseXmltoJaxbBattlefield("ex3-basic.xml");
        }catch (Exception e)
        {
            System.out.println("hii");
        }
        System.out.println(battlefield.toString());

    }

//
//    @Test
//    public void dictionaryParserTest(){
//        Tester test = new Tester();
//        performer.loadMachineFromXml("ex2-basic.xml");
//        boolean temp = test.getMachine("ex2-basic.xml");
//        EnigmaDictionary enigmaDictionary = new EnigmaDictionary();
//
//        System.out.println(enigmaDictionary.checkIfExists("whom?"));
//        System.out.println(enigmaDictionary.checkIfExists("whom"));
//        System.out.println(enigmaDictionary.checkIfExists("KomBat"));
//        System.out.println(enigmaDictionary.checkIfExists("swap"));
//        System.out.println(enigmaDictionary.checkIfExists("Nice"));
//        System.out.println(enigmaDictionary.checkIfExists("poland"));
//
//
//    }
}
