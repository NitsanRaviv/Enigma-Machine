package TimingTests;
import EnigmaCracking.DM;
import LogicManager.Integrator;
import Machine.MachineProxy;
import XmlParsing.DictionaryXmlParser;
import XmlParsing.MachineXmlParser;
import agentUtilities.EnigmaDictionary;
import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.xml.bind.JAXBException;

import enigmaAgent.*;

import java.util.HashSet;

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

//    @Test
//    public void testQueue()
//    {
//
//        Pair<Integer,Integer> rotorsNothces[] = new Pair[]{new Pair<>(2, 3), new Pair<>(1, 3)};
//        BlockingQueue<String> sq = new ArrayBlockingQueue<>(10);
//        BlockingQueue<EasyTask> mq = new ArrayBlockingQueue<>(10);
//        EasyTask bt = new EasyTask(rotorsNothces,"AABBCCDDEEFF",4);
//        EnigmaAgent agent = new EnigmaAgent(bt, mp, sq,mq);
//        Thread thread = new Thread(agent);
//        thread.start();
//        for (int i = 0; i <4 ; i++) {
//            try {
//                System.out.println(sq.take());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//    }

    @Test
    public void testRotorCounter(){
       RotorLocationCounter rotorLocationCounter = new RotorLocationCounter(mp.getLanguageInterpeter(), mp.getCurrentRotorAndLocations());
        for (Pair<Integer, Integer> pair : rotorLocationCounter.nextRotorsAndLocations(4)) {
            System.out.println(pair.getValue());
        }
    }

    @Test
    public void testDM(){
        Integrator.getIntegrator().loadMachineFromXml("Tests/ex1-sanity-small.xml");
        mp.setChosenReflector("I");
        DM dm = new DM(mp, new EnigmaDictionary(new HashSet<>()), "ABCDEF", 2, 32);
        dm.handleEasyTasks();
    }

    @Test
    public void cloneMachine(){
        mp.setMachineToInitialState();
        MachineProxy mpClone = null;
        try {
             mpClone = mp.clone();
             mpClone.setChosenRotors(new Pair<>(2, 3), new Pair<>(1, 3)); //left to right
             mpClone.setChosenReflector("I");
        }catch (CloneNotSupportedException e) {
            System.out.println("exeption clone");
        }
        mpClone.encryptCode("AABBCCDDEEFF");
        mpClone.encryptCode("AABBCCDDEEFF");
        mpClone.encryptCode("AABBCCDDEEFF");
        System.out.println(mp.encryptCode("AABBCCDDEEFF"));

    }
}
