package TimingTests;
import EnigmaCracking.DM;
import EnigmaCracking.DMadapter;
import EnigmaCracking.HalfWayInfo;
import EnigmaCracking.Tasks.TaskLevels;
import LogicManager.Integrator;
import Machine.MachineProxy;
import XmlParsing.DictionaryXmlParser;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.xml.bind.JAXBException;
import enigmaAgent.*;

import java.util.List;


public class SchedulingTests {
    private static MachineProxy mp;

    @BeforeClass
    public static void init() {
        mp = null;
        try {
            mp = MachineXmlParser.parseXmlToMachineProxy("ex2-basic.xml");
            mp.setChosenRotors(new Pair<>(2, 3), new Pair<>(1, 3)); //left to right
            mp.setChosenReflector("I");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRotorCounter() {
        RotorLocationCounter rotorLocationCounter = new RotorLocationCounter(mp.getLanguageInterpeter(), mp.getCurrentRotorAndLocations());
        for (Pair<Integer, Integer> pair : rotorLocationCounter.nextRotorsAndLocations(4)) {
            System.out.println(pair.getValue());
        }
    }

    @Test
    public void testDMmedium() {
        Integrator.getIntegrator().loadMachineFromXml("ex2-basic.xml");
        mp.setChosenRotors(new Pair<>(2, 1), new Pair<>(3, 1), new Pair(5, 1)); //left to right
        // mp.setChosenReflector("II");
        String test = mp.encryptCodeToString("encapsulation".toUpperCase());
        DM dm = new DM(mp, DictionaryXmlParser.getDictionaryXmlParser().getDictionary(), test, 1, 20, TaskLevels.levelMedium);
        dm.run();
    }

    @Test
    public void testDMhard() {
        Integrator.getIntegrator().loadMachineFromXml("ex2-basic.xml");
        mp.setChosenRotors(new Pair<>(3, 1), new Pair<>(5, 1), new Pair(2, 1)); //left to right
        // mp.setChosenReflector("II");
        DM dm = new DM(mp, DictionaryXmlParser.getDictionaryXmlParser().getDictionary(), "?KUUCYVAQS", 10, 20, TaskLevels.levelHard);
        dm.run();
    }

    @Test
    public void testDMeasy() {
        Integrator.getIntegrator().loadMachineFromXml("ex2-basic.xml");
        mp.setChosenRotors(new Pair<>(5, 1), new Pair<>(2, 1), new Pair(3, 1)); //left to right
        mp.setChosenReflector("II");
        DM dm = new DM(mp, DictionaryXmlParser.getDictionaryXmlParser().getDictionary(), "?KUUCYVAQS", 10, 20, TaskLevels.levelEasy);
        dm.run();
    }

    @Test
    public void testDMImpossible() {
        Integrator.getIntegrator().loadMachineFromXml("ex2-basic.xml");
        DM dm = new DM(mp, DictionaryXmlParser.getDictionaryXmlParser().getDictionary(), "?KUUCYVAQS", 10, 20, TaskLevels.levelImpossible);
        dm.run();
    }

    @Test
    public void cloneMachine() {
        MachineProxy mpClone = null;
        try {
            mpClone = mp.clone();
            mpClone.setChosenRotors(new Pair<>(2, 3), new Pair<>(1, 3)); //left to right
            mpClone.setChosenReflector("I");
        } catch (CloneNotSupportedException e) {
            System.out.println("exeption clone");
        }
        mp.encryptCode("AABBCCDDEEFF");
        mp.encryptCode("AABBCCDDEEFF");
        mp.encryptCode("AABBCCDDEEFF");
        System.out.println(mpClone.encryptCode("AABBCCDDEEFF"));
    }

    @Test
    public void testSuspend() {
        Integrator.getIntegrator().loadMachineFromXml("ex2-basic.xml");
        mp.setChosenRotors(new Pair<>(5, 1), new Pair<>(2, 1), new Pair(3, 1)); //left to right
        //mp.setChosenReflector("II");
        DMadapter dMadapter = new DMadapter(mp, DictionaryXmlParser.getDictionaryXmlParser().getDictionary(), "?KUUCYVAQS", 10, 20, TaskLevels.levelHard);
        dMadapter.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        dMadapter.stopDM();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

}
