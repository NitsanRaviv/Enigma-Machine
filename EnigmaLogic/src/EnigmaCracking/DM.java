package EnigmaCracking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import Parts.*;
import Utilities.RomanInterpeter;
import agentUtilities.EnigmaDictionary;
import enigmaAgent.EnigmaAgent;
import enigmaAgent.RotorLocationCounter;
import Machine.MachineProxy;
import Tasks.*;
import javafx.util.Pair;

import static enigmaAgent.AgentConstants.AGENT_FINISHED_TASKS;

public class DM extends Thread {
    private int numAgents;
    private List<BlockingQueue<EasyTask>> tasksQueues;
    private BlockingQueue<String> decryptedStrings;
    private Set<String> decryptPotentials;
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
    private List<Reflector> reflectors;
    private List<Runnable> levels;
    private List<List<Integer>> rotorIdsPermutations;
    private List<List<Integer>> rotorIdsNoverK;
    private int wantedLevel;
    private Semaphore mainSemaphore;

    public InterruptReason getInterruptReason() {
        return interruptReason;
    }

    private InterruptReason interruptReason;

    public void setInterruptReason(InterruptReason interruptReason) {
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
        this.init();
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
                String potential = decryptedStrings.poll();
                if (potential != null)
                    if (potential == AGENT_FINISHED_TASKS)
                        finishedAgents++;
                    else {
                        if (decryptPotentials.contains(potential) == false)
                            decryptPotentials.add(potential);
                    }
                    if(isInterrupted()){
                        try {
                           interruptReason.wait();
                        }catch (InterruptedException ie){
                            ie.printStackTrace();
                        }
                    }
            }


            System.out.println(decryptPotentials);
    }

    public void handleMediumTasks(){
        for (Reflector reflector : reflectors) {
            this.machine.setChosenReflector(RomanInterpeter.numToRoman(reflector.getId()));
            handleEasyTasks();
        }
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
                this.Agents.add(new EnigmaAgent(machine.clone(), decryptedStrings, tasksQueues.get(i), enigmaDictionary, i+1));
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

    public void setMainSemaphore(Semaphore sem) {
        this.mainSemaphore = sem;
    }


    public enum InterruptReason{
        SUSPEND, INFOS;
    }

}
