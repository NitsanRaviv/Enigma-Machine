package AgentTests;
import Machine.MachineProxy;
import Tasks.EasyTask;
import XmlParsing.MachineXmlParser;
import javafx.util.Pair;
import org.junit.Test;
import javax.xml.bind.JAXBException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import EnigmaAgent.EnigmaAgent;

public class AgentBlockingQueueTest {
    @Test
    public void testQueue()
    {
        MachineProxy mp = null;
        try {
            mp = MachineXmlParser.parseXmlToMachineProxy("ex1-sanity-small.xml");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        Pair<Integer,Integer> rotorsNothces[] = new Pair[]{new Pair<>(2, 3), new Pair<>(1, 3)};
        mp.setChosenRotors(new Pair<>(2, 3), new Pair<>(1, 3)); //left to right
        mp.setChosenReflector("I");
        BlockingQueue<String> sq = new ArrayBlockingQueue<>(10);
        BlockingQueue<EasyTask> mq = new ArrayBlockingQueue<>(10);
        EasyTask bt = new EasyTask(rotorsNothces,"AABBCCDDEEFF",4);
        EnigmaAgent agent = new EnigmaAgent(bt, mp, sq,mq);
        Thread thread = new Thread(agent);
        thread.start();
        for (int i = 0; i <4 ; i++) {
            try {
                System.out.println(sq.take());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        mp.setChosenRotors(new Pair<>(2, 3), new Pair<>(1, 5)); //left to right
        mp.setChosenReflector("I");
        System.out.println(mp.encryptCode("AABBCCDDEEFF"));
    }
}
