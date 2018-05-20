package TimingTests;
import EnigmaCracking.DM;
import LogicManager.Integrator;
import LogicManager.Performer;
import Machine.MachineProxy;
import XmlParsing.DictionaryXmlParser;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.xml.bind.JAXBException;

import enigmaAgent.*;


public class SchedulingTests {
    private static MachineProxy mp;

    @BeforeClass
    public static void init()
    {
         mp = null;
        try {
            mp = MachineXmlParser.parseXmlToMachineProxy("ex1-sanity-small.xml");
            mp.setChosenRotors(new Pair<>(2, 3), new Pair<>(1, 3)); //left to right
            mp.setChosenReflector("I");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRotorCounter(){
       RotorLocationCounter rotorLocationCounter = new RotorLocationCounter(mp.getLanguageInterpeter(), mp.getCurrentRotorAndLocations());
        for (Pair<Integer, Integer> pair : rotorLocationCounter.nextRotorsAndLocations(4)) {
            System.out.println(pair.getValue());
        }
    }

    @Test
    public void testDM(){
      //  Tester test = new Tester();
       // Performer.getPerformer().loadMachineFromXml("ex2-basic.xml");
      //  boolean temp = test.getMachine("ex2-basic.xml");
        Integrator.getIntegrator().loadMachineFromXml("ex2-basic.xml");

        DM dm = new DM(Performer.getPerformer().getMachineProxy(), DictionaryXmlParser.getDictionaryXmlParser().getDictionary(), "ABCDE", 16, 4);
        dm.handleEasyTasks();
    }

    @Test
    public void cloneMachine(){
        MachineProxy mpClone = null;
        try {
             mpClone = mp.clone();
             mpClone.setChosenRotors(new Pair<>(2, 3), new Pair<>(1, 3)); //left to right
             mpClone.setChosenReflector("I");
        }catch (CloneNotSupportedException e) {
            System.out.println("exeption clone");
        }
        mp.encryptCode("AABBCCDDEEFF");
        mp.encryptCode("AABBCCDDEEFF");
        mp.encryptCode("AABBCCDDEEFF");
        System.out.println(mpClone.encryptCode("AABBCCDDEEFF"));

    }
}
