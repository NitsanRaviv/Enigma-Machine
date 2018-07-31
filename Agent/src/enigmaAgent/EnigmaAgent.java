package enigmaAgent;

import agentUtilities.EnigmaDictionary;
import Machine.MachineProxy;
import Tasks.EasyTask;
import sun.awt.Mutex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static enigmaAgent.AgentConstants.AGENT_FINISHED_TASKS;
import static enigmaAgent.AgentConstants.NO_MORE_TASKS;

public class EnigmaAgent extends Thread {
    private EnigmaDictionary enigmaDictionary;
    private EasyTask easyTask;
    private MachineProxy machineProxy;
    private BlockingQueue<AgentAnswer> agentAnsewerQueue;
    private BlockingQueue<EasyTask> tasksQueue;
    private int id;
    private int taskNumber;
    private RotorLocationCounter rotorLocationCounter;
    private boolean running = true;
    private List<String> goodStrings;
    private Mutex agentLock;
    private InterruptReason interruptReason;


    @Override
    public void run() {
        while (running) {
            try {
                getNewTask();
                this.goodStrings = new ArrayList<>();
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < easyTask.getTaskSize(); i++) {
                    String processed = machineProxy.encryptCodeToString(easyTask.getStringToDecrypt());
                    String[] processedArr = processed.split(" ");
                    for (String processedString : processedArr) {
                        if (enigmaDictionary.checkIfExists(processedString) == true) {
                            goodStrings.add(processedString);
                        }
                    }
                    taskNumber++;
                    setNewPositionOfRotorsInMachine();
                }
                if (Thread.currentThread().interrupted() == true) {
                    handleInterrupt();
                }
                long endTime = System.currentTimeMillis();
                try {
                    for (String goodString : goodStrings) {
                        agentAnsewerQueue.put(new AgentAnswer(goodString, easyTask.getStringToDecrypt(), endTime - startTime, id, machineProxy.getCurrentCode()));
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("finished my work as an agent thread, WebAgent will decide whats next. bye! my id: " + this.id);

    }

    private void handleInterrupt() {
        if(interruptReason == InterruptReason.SUSPEND){
            agentLock.lock();
            agentLock.unlock();
        }
        if(interruptReason == interruptReason.STOP)
            running = false;
    }


    private void getNewTask() {
        try {
            easyTask = tasksQueue.take();
            if(easyTask.getTaskSize() == NO_MORE_TASKS){
                running = false;
                agentAnsewerQueue.put(new AgentAnswer(AGENT_FINISHED_TASKS, "", 0, id, ""));
            }
            else {
                System.out.println("current rotor settings (id to location: " + easyTask.getRotorsAndNotches());
                machineProxy.setChosenRotors(easyTask.getRotorsAndNotches());
                if(rotorLocationCounter == null) //first task so we need to initialize it
                    rotorLocationCounter = new RotorLocationCounter(machineProxy.getLanguageInterpeter(), easyTask.getRotorsAndNotches());
                else
                rotorLocationCounter.setRotorsAndLocations(easyTask.getRotorsAndNotches());
            }

        } catch (InterruptedException ie) {
            System.out.println("Agent thread was interrupted");
        }
    }

    public EnigmaAgent(MachineProxy machineProxy, BlockingQueue<AgentAnswer> agentAnsewerQueue, BlockingQueue<EasyTask> tasksQueue, EnigmaDictionary dictionary, int id, Mutex lock) {
        this.machineProxy = machineProxy;
        this.agentAnsewerQueue = agentAnsewerQueue;
        taskNumber = 0;
        this.tasksQueue = tasksQueue;
        this.enigmaDictionary = dictionary;
        this.id = id;
        this.agentLock = lock;
    }

    public void setNewPositionOfRotorsInMachine(){
        machineProxy.setMachineToInitialState();
        machineProxy.setChosenRotors(rotorLocationCounter.nextRotorsAndLocations());
    }

    public int getAgentId() {
        return id;
    }

    public enum InterruptReason{
        FREE, SUSPEND, INFOS, STOP;
    }

    public void setInterruptReason(InterruptReason ie){
        this.interruptReason = ie;
    }


}
