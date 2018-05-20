package EnigmaCracking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import agentUtilities.EnigmaDictionary;
import enigmaAgent.EnigmaAgent;
import enigmaAgent.RotorLocationCounter;
import Machine.MachineProxy;
import Tasks.*;

import static enigmaAgent.AgentConstants.AGENT_FINISHED_TASKS;

public class DM extends Thread {
    private int numAgents;
    private List<BlockingQueue<EasyTask>> tasksQueues;
    private BlockingQueue<String> decryptedStrings;
    private List<String> decryptPotentials;
    private MachineProxy machine;
    private int numOfEasyTasks;
    private int currentNumOfTasks;
    private int missionPerAgent;
    private int taskSize;
    private String encryptedString;
    private RotorLocationCounter rotorLocationCounter;
    private List<Thread> Agents;
    private EnigmaDictionary enigmaDictionary;
    private List<EasyTask> easyTasks;
    private final int taksQueueSize = 10000;
    private int finishedAgents = 0;

    //TODO:: add mission-level
    public DM(MachineProxy machine, EnigmaDictionary dictionary, String encrtyptedString,int numAgents, int taskSize){
        this.numAgents = numAgents;
        this.machine = machine;
        this.taskSize = taskSize;
        this.decryptPotentials = new ArrayList<>();
        this.easyTasks = new ArrayList<>();
        this.Agents = new ArrayList<>();
        rotorLocationCounter = new RotorLocationCounter(machine.getLanguageInterpeter(), machine.getAppliedRotors());
        this.decryptedStrings = new ArrayBlockingQueue(20);
        this.Agents = new ArrayList<>();
        this.encryptedString = encrtyptedString;
        this.enigmaDictionary = dictionary;
        calcNumOfEasyTasks();

    }
    public void handleEasyTasks(){
        this.createEasyTasks()
                .createTasksQueues()
                .createThreadAgents()
                .deliverTasksToQueues()
                .runAgents();

        while (finishedAgents < numAgents) {
            //TODO::possible better processor using here - sleep or interrupt
            String potential = decryptedStrings.poll();
            if (potential != null)
                if(potential == AGENT_FINISHED_TASKS)
                    finishedAgents++;
            else
                decryptPotentials.add(potential);
        }
    }

    private DM deliverTasksToQueues() {
        int deliveredTasks = 0;
        try {
            for (BlockingQueue<EasyTask> tasksQueue : tasksQueues) {
                int numOfTasksPerAgent = (numOfEasyTasks /numAgents);
                for (int i = 0; i < numOfTasksPerAgent; i++) {
                    tasksQueue.put(easyTasks.get(deliveredTasks));
                    deliveredTasks++;
                }
            }
            while(deliveredTasks < numOfEasyTasks){
                tasksQueues.get(0).put(easyTasks.get(deliveredTasks));
            }
        }catch (InterruptedException ie){
            ;//TODO::handle all interrupts in DM
        }
        return this;
    }

    private DM runAgents() {
        for (Thread agent : Agents) {
            agent.start();
        }
        return this;
    }


    @Override
    public void run() {
        //TODO::we will run on each list of levels (ask id not null) and handle the easy tasks within
        handleEasyTasks();
    }

    private DM createThreadAgents(){
        for (int i = 0; i < numAgents ; i++) {
            try {
                this.Agents.add(new EnigmaAgent(machine.clone(), decryptedStrings, tasksQueues.get(i), enigmaDictionary, i+1));
            }catch (CloneNotSupportedException cne)
            {
                System.out.println("DM couldnt copy machine");
            }
        }
        return this;
    }

    private void calcNumOfEasyTasks()
    {
        int numLetters = machine.getLanguage().length;
        int numRotors = machine.getAppliedRotors();
        //check this casting
        numOfEasyTasks =(int)Math.pow(numRotors, numLetters) / taskSize;
        numOfEasyTasks += (int)Math.pow(numRotors, numLetters) % taskSize;

    }

    public DM createEasyTasks() {
        int createdTasks = 0;
        for (createdTasks = 0; createdTasks < numOfEasyTasks; createdTasks++) {
            this.easyTasks.add(new EasyTask(rotorLocationCounter.getCurrentRotorsAndLocations() ,encryptedString,taskSize));
            rotorLocationCounter.nextRotorsAndLocations(taskSize);
        }
        return this;
    }

    public DM createTasksQueues(){
        this.tasksQueues = new ArrayList<>();
        for (int i = 0; i <numAgents ; i++) {
            this.tasksQueues.add(new ArrayBlockingQueue<>(taksQueueSize));
        }
        return this;
    }
}
