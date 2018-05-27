package EnigmaCracking;

import Machine.MachineProxy;
import agentUtilities.EnigmaDictionary;

import java.util.concurrent.Semaphore;

public class DMadapter extends Thread {
    private DM dm;
    private Semaphore suspendDmLock;
    public DMadapter(MachineProxy machine, EnigmaDictionary dictionary, String encrtyptedString, int numAgents, int taskSize, int level) {
        suspendDmLock = new Semaphore(1);
        dm = new DM(machine, dictionary, encrtyptedString, numAgents, taskSize, level);
        dm.setMainSemaphore(suspendDmLock);
    }

    public void runDM(){
        dm.start();
    }

    @Override
    public void run(){
        this.runDM();
    }

    public void suspendDM()
    {
        dm.setInterruptReason(DM.InterruptReason.SUSPEND);
        dm.interrupt();
    }

    public void unSuspendDM(){
        dm.getInterruptReason().notifyAll();
    }

}
