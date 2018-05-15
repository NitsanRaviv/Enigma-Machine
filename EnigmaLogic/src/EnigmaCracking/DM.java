package EnigmaCracking;

import java.util.concurrent.BlockingQueue;

import Machine.MachineProxy;
import Tasks.*;

public class DM {
    private int numAgents;
    private BlockingQueue<EasyTask> tasksQueue;
    private BlockingQueue<String> encryptedStrings;
    private MachineProxy machine;
    private int numOfTasks;
    private int missionPerAgent;

    //TODO:: add mission-level
    public DM(int numAgents, MachineProxy machine){
        this.numAgents = numAgents;
        this.machine = machine;
    }

    public void handleEasyTasks(){
        calcNumOfTasks(); //check not dividing in zero
        missionPerAgent = numOfTasks / numAgents;

    }

    private void calcNumOfTasks()
    {
        int numLetters = machine.getLanguage().length;
        int numRotors = machine.getAppliedRotors();
        //check this casting
        numOfTasks =(int)Math.pow(numRotors, numLetters);
    }

}
