package WebTests;

import EnigmaCracking.DM;
import EnigmaCracking.Tasks.TaskLevels;
import Machine.MachineProxy;
import XmlParsing.DictionaryXmlParser;
import javafx.util.Pair;
import org.junit.Test;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

public class webClientTests {

    @Test
    public void clientTest()
    {
        try {
            Socket socket = new Socket(InetAddress.getLoopbackAddress(), 9090);
            System.out.println("client connected");
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            MachineProxy machineProxy = (MachineProxy)inputStream.readObject();
            System.out.println(machineProxy);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
