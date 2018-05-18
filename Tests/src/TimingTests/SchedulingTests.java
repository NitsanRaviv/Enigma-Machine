package TimingTests;
import EnigmaCracking.DM;
import Machine.MachineProxy;
import Tasks.EasyTask;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.xml.bind.JAXBException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import EnigmaAgent.*;
import EnigmaCracking.DM;

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
       DM dm = new DM(1, mp, "BDEAFAC",10, 5);
       dm.handleEasyTasks();
    }
}
