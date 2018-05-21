package enigmaAgent;

import agentUtilities.EnigmaDictionary;
import Machine.MachineProxy;
import Tasks.EasyTask;
import java.util.concurrent.BlockingQueue;

import static enigmaAgent.AgentConstants.AGENT_FINISHED_TASKS;
import static enigmaAgent.AgentConstants.NO_MORE_TASKS;

public class EnigmaAgent extends Thread {
    private EnigmaDictionary enigmaDictionary;
    private EasyTask easyTask;
    private MachineProxy machineProxy;
    private BlockingQueue<String> encryptedStringsQueue;
    private BlockingQueue<EasyTask> tasksQueue;
    private int id;
    private int taskNumber;
    private RotorLocationCounter rotorLocationCounter;
    private boolean running = true;


    @Override
    public void run() {
        while (running) {
            getNewTask();
            for (int i = 0; i < easyTask.getTaskSize(); i++) {
                try {
                    String processed = machineProxy.encryptCodeToString(easyTask.getStringToEncrypt());
                    if(enigmaDictionary.checkIfExists(processed) == true)
                        encryptedStringsQueue.put(processed + " num: " + taskNumber);
                } catch (InterruptedException ie) {
                    System.out.println("Agent thread was interrupted");
                }
                taskNumber++;
                setNewPermutation();
            }
        }
    }

    private void getNewTask() {
        try {
            easyTask = tasksQueue.take();
            if(easyTask.getTaskSize() == NO_MORE_TASKS){
                running = false;
                encryptedStringsQueue.put(AGENT_FINISHED_TASKS);
            }
            else {
                machineProxy.setChosenRotors(easyTask.getRotorsAndNotches());
                if(rotorLocationCounter == null) //first task so we need to initialize it
                    rotorLocationCounter = new RotorLocationCounter(machineProxy.getLanguageInterpeter(), machineProxy.getCurrentRotorAndLocations());
                else
                rotorLocationCounter.setRotorsAndLocations(easyTask.getRotorsAndNotches());
            }

        } catch (InterruptedException ie) {
            System.out.println("Agent thread was interrupted");
        }
    }

    public EnigmaAgent(MachineProxy machineProxy, BlockingQueue<String> encryptedStringsQueue, BlockingQueue<EasyTask> tasksQueue, EnigmaDictionary dictionary, int id) {
        this.machineProxy = machineProxy;
        this.encryptedStringsQueue = encryptedStringsQueue;
        taskNumber = 0;
        this.tasksQueue = tasksQueue;
        this.enigmaDictionary = dictionary;
        this.id = id;
    }

    public void setNewPermutation(){
        machineProxy.setMachineToInitialState();
        machineProxy.setChosenRotors(rotorLocationCounter.nextRotorsAndLocations());
    }
}
