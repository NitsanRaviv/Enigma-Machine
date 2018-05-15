package EnigmaAgent;

import Machine.MachineProxy;
import Tasks.EasyTask;
import java.util.concurrent.BlockingQueue;

public class EnigmaAgent implements Runnable {
    private EasyTask easyTask;
    private MachineProxy machineProxy;
    private BlockingQueue<String> encryptedStringsQueue;
    private BlockingQueue<EasyTask> tasksQueue;
    private int id;
    private int taskNumber;
    private RotorLocationCounter rotorLocationCounter;


    @Override
    public void run() {
        //TODO :: first we need to take a task from the tasks queue
        for(int i = 0; i < easyTask.getTaskSize(); i++) {
            String processed = machineProxy.encryptCode(easyTask.getStringToEncrypt()).toString();
            try {
                //TODO:: first, the agent will check in the dictionary if strings are nomenies
                encryptedStringsQueue.put(processed + " num: " + i);
            }catch (Exception e){
                System.out.println("Agent thread was interrupted");
            }
            taskNumber++;
            setNewPermutation();
        }
    }

    //TODO :: easyTask is in constructor for tests, need to remove it
    public EnigmaAgent(EasyTask easyTask, MachineProxy machineProxy, BlockingQueue<String> encryptedStringsQueue, BlockingQueue<EasyTask> tasksQueue) {
        this.easyTask = easyTask;
        this.machineProxy = machineProxy;
        this.encryptedStringsQueue = encryptedStringsQueue;
        taskNumber = 0;
        rotorLocationCounter = new RotorLocationCounter(machineProxy.getLanguageInterpeter(), easyTask.getRotorsAndNotches());
        this.tasksQueue = tasksQueue;
    }

    public void setNewPermutation(){
        machineProxy.setMachineToInitialState();
        machineProxy.setChosenRotors(rotorLocationCounter.nextRotorsAndLocations());
    }
}
