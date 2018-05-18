package EnigmaCracking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import EnigmaAgent.EnigmaAgent;
import EnigmaAgent.RotorLocationCounter;
import Machine.MachineProxy;
import Tasks.*;

public class DM {
    private int numAgents;
    private BlockingQueue<EasyTask> tasksQueue;
    private BlockingQueue<String> decryptedStrings;
    private List<String> decryptPotentials;
    private MachineProxy machine;
    private int numOfTasks;
    private int currentNumOfTasks;
    private int missionPerAgent;
    private int taskSize;
    private String encryptedString;
    private RotorLocationCounter rotorLocationCounter;
    private List<Thread> Agents;


    //TODO:: add mission-level
    public DM(int numAgents, MachineProxy machine,String encrtyptedString, int numOfTasks, int taskSize){
        this.numAgents = numAgents;
        this.machine = machine;
        this.numOfTasks = numOfTasks;
        this.currentNumOfTasks = numOfTasks;
        this.taskSize = taskSize;
        this.decryptPotentials = new ArrayList<>();
        rotorLocationCounter = new RotorLocationCounter(machine.getLanguageInterpeter(), machine.getCurrentRotorAndLocations());
        this.tasksQueue = new ArrayBlockingQueue(20);
        this.decryptedStrings = new ArrayBlockingQueue(20);
        this.Agents = new ArrayList<>();
        this.encryptedString = encrtyptedString;

        this.Agents.add(new EnigmaAgent(machine, decryptedStrings, tasksQueue));
        this.Agents.get(0).setDaemon(true);
        this.Agents.get(0).start();
    }
    public void handleEasyTasks(){
        while (numOfTasks > 0){
            try{
                if(numOfTasks < taskSize)
                    taskSize = numOfTasks;
                EasyTask easyTask = new EasyTask(rotorLocationCounter.nextRotorsAndLocations(taskSize),encryptedString,taskSize);
                tasksQueue.put(easyTask);
                String potential = decryptedStrings.poll();
                if (potential!= null){
                    decryptPotentials.add(potential);
                }
               }catch (InterruptedException ie) {
                //report info to user
            }
            numOfTasks -= taskSize;
        }
        try {
            Thread.sleep(1000);
            String potential = decryptedStrings.poll();
            if (potential!= null){
                decryptPotentials.add(potential);
            }
        }catch (InterruptedException ie)
        {
            System.out.println("DM interrupted");
        }
    }

    private void calcNumOfTasks()
    {
        int numLetters = machine.getLanguage().length;
        int numRotors = machine.getAppliedRotors();
        //check this casting
        numOfTasks =(int)Math.pow(numRotors, numLetters);
    }
}
