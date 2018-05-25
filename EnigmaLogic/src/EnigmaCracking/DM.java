package EnigmaCracking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import EnigmaCracking.Tasks.TaskLevels;
import LogicManager.Integrator;
import Parts.Reflector;
import Utilities.RomanInterpeter;
import XmlParsing.MachineXmlParser;
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
    private int wantedLevel;

    //TODO:: add mission-level
    public DM(MachineProxy machine, EnigmaDictionary dictionary, String encrtyptedString, int numAgents, int taskSize, int level){
        this.numAgents = numAgents;
        this.machine = machine;
        this.taskSize = taskSize;
        this.decryptPotentials = new HashSet<>();
        this.easyTasks = new ArrayList<>();
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
                .createEasyTasks()
                .createTasksQueues();
        return this;
    }

    public void handleEasyTasks(){
               this.createThreadAgents()
                .deliverTasksToQueues()
                .runAgents();
        this.levels.add(() ->this.handleEasyTasks());
        this.levels.add(() ->this.handleMediumTasks());

        while (finishedAgents < numAgents) {
            //TODO::possible better processor using here - sleep or interrupt
            String potential = decryptedStrings.poll();
            if (potential != null)
                if(potential == AGENT_FINISHED_TASKS)
                    finishedAgents++;
            else {
                    decryptPotentials.add(potential);
                    System.out.println(potential);
                }
        }
    }

    public void handleMediumTasks(){
        for (Reflector reflector : reflectors) {
            this.machine.setChosenReflector(RomanInterpeter.numToRoman(reflector.getId()));
            handleEasyTasks();
        }
    }


    public void handleHardTasks(){
      ///foreach permutation of the arrangement of rotors (ids are known!)
        //set machine rotors, position doesnt matter!
        //than call handelMediumTasks
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
}
