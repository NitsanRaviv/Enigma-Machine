package EnigmaCracking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import EnigmaCracking.Tasks.TaskLevels;
import Parts.*;
import Utilities.RomanInterpeter;
import agentUtilities.EnigmaDictionary;
import enigmaAgent.AgentAnswer;
import enigmaAgent.EnigmaAgent;
import enigmaAgent.RotorLocationCounter;
import Machine.MachineProxy;
import Tasks.*;
import javafx.util.Pair;
import sun.awt.Mutex;

import static enigmaAgent.AgentConstants.AGENT_FINISHED_TASKS;

public class DM extends Thread {
    private int numAgents;
    private List<BlockingQueue<EasyTask>> tasksQueues;
    private BlockingQueue<AgentAnswer> decryptedStrings;
    private Set<AgentAnswer> decryptPotentials;
    private MachineProxy machine;
    private int numOfEasyTasks;
    private int currentNumOfTasks;
    private int missionPerAgent;
    private int taskSize;
    private String encryptedString;
    private RotorLocationCounter rotorLocationCounter;
    private List<EnigmaAgent> Agents;
    private EnigmaDictionary enigmaDictionary;
    private List<EasyTask> easyTasks;
    private final int taksQueueSize = 10000;
    private int finishedAgents = 0;
    private List<Reflector> reflectors;
    private List<Runnable> levels;
    private List<List<Integer>> rotorIdsPermutations;
    private List<List<Integer>> rotorIdsNoverK;
    private int wantedLevel;
    private Mutex mainMutex;
    private EnigmaAgent.InterruptReason interruptReason = EnigmaAgent.InterruptReason.FREE;
    private Mutex agentLock;
    private BlockingQueue<HalfWayInfo> halfWayInfoQueue;
    private int totalTasksDelivered = 0;


    public EnigmaAgent.InterruptReason getInterruptReason() {
        return interruptReason;
    }

    public void setInterruptReason(EnigmaAgent.InterruptReason interruptReason) {
        this.interruptReason = interruptReason;
    }


    //TODO:: add mission-level
    public DM(MachineProxy machine, EnigmaDictionary dictionary, String encrtyptedString, int numAgents, int taskSize, int level){
        this.numAgents = numAgents;
        this.machine = machine;
        this.taskSize = taskSize;
        this.decryptPotentials = new HashSet<>();
        this.Agents = new ArrayList<>(numAgents);
        this.decryptedStrings = new ArrayBlockingQueue(10000);
        this.encryptedString = encrtyptedString;
        this.enigmaDictionary = dictionary;
        this.reflectors = new ArrayList<>(machine.getReflectors());
        this.levels = new ArrayList<>();
        this.wantedLevel = level;
        this.agentLock = new Mutex();
        this.init();
    }

    public DM(MachineProxy machine, EnigmaDictionary dictionary, String encrtyptedString, int numAgents, int taskSize, int level, Mutex mutex){
        this(machine, dictionary, encrtyptedString, numAgents,taskSize, level);
        this.mainMutex = mutex;
    }


        public DM init(){
        this.calcNumOfEasyTasks()
                .createTasksQueues();
        this.levels.add(() ->this.handleEasyTasks());
        this.levels.add(() ->this.handleMediumTasks());
        this.levels.add(() -> this.handleHardTasks());
        this.levels.add(() ->this.handleImpossibleTasks());
        return this;
    }

    public void handleEasyTasks(){
        this.createEasyTasks()
                .createThreadAgents()
                .deliverTasksToQueues()
                .runAgents();

            while (finishedAgents < numAgents) {
                //TODO::possible better processor using here - sleep or interrupt
                AgentAnswer potential = decryptedStrings.poll();
                if (potential != null)
                    if (potential.getEncryptedString() == AGENT_FINISHED_TASKS)
                        finishedAgents++;
                    else {
                        if (decryptPotentials.contains(potential.getEncryptedString()) == false)
                            decryptPotentials.add(potential);
                    }


                if (interruptReason != EnigmaAgent.InterruptReason.FREE) {
                    if (interruptReason == EnigmaAgent.InterruptReason.SUSPEND)
                        stopProcessing();

                    if (interruptReason == EnigmaAgent.InterruptReason.INFOS)
                        createInfosHalfWay();
                }

            }

            if(wantedLevel == TaskLevels.levelEasy)
               System.out.println(decryptPotentials);
    }

    private void stopProcessing() {
        System.out.println("i was suspended");
        agentLock.lock();
        for (EnigmaAgent agent : Agents) {
            agent.interrupt();
            agent.setInterruptReason(EnigmaAgent.InterruptReason.SUSPEND);
        }
        mainMutex.lock();
        mainMutex.unlock();
        agentLock.unlock();
        for (EnigmaAgent agent : Agents) {
            agent.setInterruptReason(EnigmaAgent.InterruptReason.FREE);
        }
    }

    public void handleMediumTasks(){
        for (Reflector reflector : reflectors) {
            this.machine.setChosenReflector(RomanInterpeter.numToRoman(reflector.getId()));
            handleEasyTasks();
        }

        if(wantedLevel == TaskLevels.levelMedium)
            System.out.println(decryptPotentials);
    }

    public void setPermutationForRotorIdsHelper(){
        rotorIdsPermutations = new ArrayList<>();

        List<Integer> chosenRotorIds = new ArrayList<>();

        for (Pair<Integer, Integer> rotorAndLoc : machine.getCurrentRotorAndLocations()) {
            chosenRotorIds.add(rotorAndLoc.getKey());
        }
        setPermutationsForRotorIds(chosenRotorIds, 0);
    }


    //this method assumes ids in machine are allready known
    public void handleHardTasks(){
        setPermutationForRotorIdsHelper();
        Pair<Integer, Integer>[] rotorsAndLocations = new Pair[machine.getAppliedRotors()];
        for (List<Integer> rotorIdsPermutation : rotorIdsPermutations) {
            for (int i = 0; i < machine.getAppliedRotors() ; i++) {
                rotorsAndLocations[i] = new Pair<>(rotorIdsPermutation.get(i), 1);
            }
            machine.setChosenRotors(rotorsAndLocations);
            handleMediumTasks();
        }

        if(wantedLevel == TaskLevels.levelHard)
            System.out.println(decryptPotentials);
    }


    private void setNoverKForRotorIds(List<Integer> rotors, int len, int startPosition, List<Integer> result){
        if (len == 0){
            rotorIdsNoverK.add(new ArrayList<>(result));
            return;
        }
        for (int i = startPosition; i <= rotors.size()-len; i++){
            result.set(result.size() - len, rotors.get(i));
            setNoverKForRotorIds(rotors, len-1, i+1, result);
        }
    }

    private void setNoverKForRotorIdsHelper(){
        rotorIdsNoverK = new ArrayList<>();
        List<Integer> resultNOverK = new ArrayList();
        for (int i = 0; i < machine.getAppliedRotors() ; i++) {
            resultNOverK.add(0);
        }
        setNoverKForRotorIds(machine.getRotorIds(), machine.getAppliedRotors(),0,resultNOverK);
    }


    public void handleImpossibleTasks(){
        setNoverKForRotorIdsHelper();
        Pair<Integer, Integer>[] rotorsAndLocations = new Pair[machine.getAppliedRotors()];
        for (List<Integer> rotoridsNoverKOneOption : rotorIdsNoverK) {
            for (int i = 0; i < machine.getAppliedRotors(); i++) {
                rotorsAndLocations[i] = new Pair<>(rotoridsNoverKOneOption.get(i), 1);
            }
            machine.setChosenRotors(rotorsAndLocations);
            handleHardTasks();
        }

        if(wantedLevel == TaskLevels.levelImpossible)
            System.out.println(decryptPotentials);
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
                //last mission for Agent to stop
                tasksQueue.put(new EasyTask(null, null, 0));
            }

            if(deliveredTasks < numOfEasyTasks) {
                tasksQueues.get(0).take();
                while (deliveredTasks < numOfEasyTasks) {
                    tasksQueues.get(0).put(easyTasks.get(deliveredTasks));
                    deliveredTasks++;
                }
                tasksQueues.get(0).put(new EasyTask(null, null, 0));
            }
            this.totalTasksDelivered += deliveredTasks * taskSize;
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
        this.levels.get(wantedLevel - 1).run();
    }

    private DM createThreadAgents(){
        finishedAgents = 0;
        this.Agents = new ArrayList<>(numAgents);
        for (int i = 0; i < numAgents ; i++) {
            try {
                this.Agents.add(new EnigmaAgent(machine.clone(), decryptedStrings, tasksQueues.get(i), enigmaDictionary, i+1, agentLock));
            }catch (Exception cne)
            {
                cne.printStackTrace();
            }
        }
        return this;
    }

    private DM calcNumOfEasyTasks()
    {
        int numLetters = machine.getLanguage().length;
        int numRotors = machine.getAppliedRotors();
        //check this casting
        numOfEasyTasks =(int)Math.pow(numLetters, numRotors) / taskSize;
        numOfEasyTasks += (int)Math.pow(numLetters, numRotors) % taskSize;
        return this;
    }

    public DM createEasyTasks() {
        this.easyTasks = new ArrayList<>();
        rotorLocationCounter = new RotorLocationCounter(machine.getLanguageInterpeter(), getInitialSettingsOfMachine());
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

    //this function assumes the machine has allready set the chosen rotors ids
    public Pair<Integer,Integer>[] getInitialSettingsOfMachine() {
        Pair<Integer, Integer>[] initialRotorsAndLocations = new Pair[machine.getAppliedRotors()];
        for (int i = 0; i < machine.getAppliedRotors(); i++) {
            initialRotorsAndLocations[i] = new Pair(machine.getCurrentRotorAndLocations()[i].getKey(), machine.getLanguageInterpeter().getLanguageAsNumbers().get(0));
        }
        return initialRotorsAndLocations;
    }

    public void setPermutationsForRotorIds(List<Integer> chosenRotorIds, int currentCreatedSize){
        for(int i = currentCreatedSize; i < chosenRotorIds.size(); i++){
            java.util.Collections.swap(chosenRotorIds, i, currentCreatedSize);
            setPermutationsForRotorIds(chosenRotorIds, currentCreatedSize+1);
            java.util.Collections.swap(chosenRotorIds, currentCreatedSize, i);
        }
        if (currentCreatedSize == chosenRotorIds.size() -1){
            rotorIdsPermutations.add(new ArrayList<>(chosenRotorIds));
        }
    }

    public void setMainMutex(Mutex mutex) {
        this.mainMutex = mutex;
    }


    private void createInfosHalfWay() {
        List<String> missionsForAgent = new ArrayList<>();
        int id = 1;
        for (BlockingQueue<EasyTask> tasksQueue : tasksQueues) {
            for (EasyTask easyTask : tasksQueue) {
                if(easyTask.getRotorsAndNotches() != null) {
                    missionsForAgent.add("Agent id: " + id + " mission: " + machine.getLanguageInterpeter().numberToLetters(easyTask.getNumberLocations()) + "\n");
                }
            }
            id++;
        }
        List<AgentAnswer> tempAgentAnsewer = new ArrayList<>();

        int index = 0;
        for (AgentAnswer decryptPotential : decryptPotentials) {
            tempAgentAnsewer.add(decryptPotential);
            index++;
            if(index == 10)
                break;
        }

        ////calculate percentage!!!
        HalfWayInfo halfWayInfo = new HalfWayInfo(tempAgentAnsewer, 0, missionsForAgent);
        try {
            halfWayInfoQueue.put(halfWayInfo);
        }catch (InterruptedException ie){
            ;
        }
    }

    public void setHalfWayInfoQueue(BlockingQueue<HalfWayInfo> halfWayInfoQueue) {
        this.halfWayInfoQueue = halfWayInfoQueue;
    }
}
