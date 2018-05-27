package EnigmaCracking;

import Machine.MachineProxy;
import XmlParsing.JaxbClasses.Enigma;
import agentUtilities.EnigmaDictionary;
import enigmaAgent.EnigmaAgent;
import sun.awt.Mutex;
import sun.management.Agent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DMadapter extends Thread {
    private DM dm;
    private Mutex suspendDmLock;
    private EnigmaAgent.InterruptReason interruptReason;
    private BlockingQueue<HalfWayInfo> halfWayInfos;
    public DMadapter(MachineProxy machine, EnigmaDictionary dictionary, String encrtyptedString, int numAgents, int taskSize, int level) {
        suspendDmLock = new Mutex();
        dm = new DM(machine, dictionary, encrtyptedString, numAgents, taskSize, level, suspendDmLock);
        dm.setMainMutex(suspendDmLock);
        this.halfWayInfos = new ArrayBlockingQueue<HalfWayInfo>(1);
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
        suspendDmLock.lock();
        interruptReason = EnigmaAgent.InterruptReason.SUSPEND;
        dm.setInterruptReason(EnigmaAgent.InterruptReason.SUSPEND);
        dm.interrupt();
    }

    public void unSuspendDM(){
          suspendDmLock.unlock();
          interruptReason = EnigmaAgent.InterruptReason.FREE;
          dm.setInterruptReason(EnigmaAgent.InterruptReason.FREE);
    }

    public HalfWayInfo getHalfWayInfos(){
        dm.setInterruptReason(EnigmaAgent.InterruptReason.INFOS);
        dm.setHalfWayInfoQueue(halfWayInfos);
        HalfWayInfo halfWayInfo = null;
        try {
             halfWayInfo = halfWayInfos.take();
        }catch (InterruptedException ie){
            ;
        }
        interruptReason = EnigmaAgent.InterruptReason.FREE;
        dm.setInterruptReason(EnigmaAgent.InterruptReason.FREE);
        return halfWayInfo;
    }
}
