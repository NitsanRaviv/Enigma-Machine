package enigmaAgent;

import Machine.MachineProxy;
import Tasks.EasyTask;
import agentUtilities.EnigmaDictionary;
import sun.awt.Mutex;

import java.util.concurrent.BlockingQueue;

public class EnigmaWebAgent extends EnigmaAgent{

    @Override
    public void run() {
        super.run();
    }

    public EnigmaWebAgent(MachineProxy machineProxy, BlockingQueue<AgentAnswer> agentAnsewerQueue, BlockingQueue<EasyTask> tasksQueue, EnigmaDictionary dictionary, int id, Mutex lock) {
        super(machineProxy, agentAnsewerQueue, tasksQueue, dictionary, id, lock);
    }


}
