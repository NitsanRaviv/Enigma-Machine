package EnigmaCracking;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import EnigmaCracking.Tasks.TaskLevels;
import Parts.*;
import Utilities.RomanInterpeter;
import agentUtilities.EnigmaDictionary;
import enigmaAgent.AgentAnswer;
import enigmaAgent.AgentConstants;
import enigmaAgent.EnigmaAgent;
import enigmaAgent.RotorLocationCounter;
import Machine.MachineProxy;
import Tasks.*;
import javafx.util.Pair;
import sun.awt.Mutex;

import static enigmaAgent.AgentConstants.AGENT_FINISHED_TASKS;
import static enigmaAgent.AgentConstants.MORE_TASKS;

public class DM extends Thread {
    private int numQueues;
    private List<List<EasyTask>> tasksQueues;
    private List<AgentAnswer> decryptedStrings;
    private Set<AgentAnswer> decryptPotentials;
    private MachineProxy machine;
    private int numOfEasyTasks;
    private int taskSize;
    private String encryptedString;
    private RotorLocationCounter rotorLocationCounter;
    private EnigmaDictionary enigmaDictionary;
    private List<EasyTask> easyTasks;
    private final int taksQueueSize = 10000;
    private int processedTaskQueues = 0;
    private List<Reflector> reflectors;
    private List<Runnable> levels;
    private List<List<Integer>> rotorIdsPermutations;
    private List<List<Integer>> rotorIdsNoverK;
    private int wantedLevel;
    private Mutex mainMutex;
    private EnigmaAgent.InterruptReason interruptReason = EnigmaAgent.InterruptReason.FREE;
    private Mutex agentLock;
    private int totalTasksDelivered = 0;
    private int totalTasksOptions = 0;
    private long startTime;
    private ServerSocket serverSocket;
    private List<Socket> agentSockets;
    private List<ObjectInputStream> agentInputStreams;
    private List<ObjectOutputStream> agentOutputStreams;
    private int numAgents;
    private int agentToAskAns = 0;



    public EnigmaAgent.InterruptReason getInterruptReason() {
        return interruptReason;
    }

    public void setInterruptReason(EnigmaAgent.InterruptReason interruptReason) {
        this.interruptReason = interruptReason;
    }

    public EnigmaAgent.InterruptReason getInterruptReason(EnigmaAgent.InterruptReason interruptReason) {
        return this.interruptReason;
    }


    //TODO:: add mission-level
    public DM(MachineProxy machine, EnigmaDictionary dictionary, String encrtyptedString, int numAgents, int taskSize, int level){
        this.numAgents = numAgents;
        this.machine = machine;
        this.taskSize = taskSize;
        this.decryptPotentials = new HashSet<>();
        this.decryptedStrings = new ArrayList<>();
        this.encryptedString = encrtyptedString;
        this.enigmaDictionary = dictionary;
        this.reflectors = new ArrayList<>(machine.getReflectors());
        this.levels = new ArrayList<>();
        this.wantedLevel = level;
        this.agentLock = new Mutex();
        //make smarter!
        this.numQueues = numAgents;

    }

    public DM(MachineProxy machine, EnigmaDictionary dictionary, String encrtyptedString, int numAgents, int taskSize, int level, Mutex mutex){
        this(machine, dictionary, encrtyptedString, numAgents,taskSize, level);
        this.mainMutex = mutex;
    }

    public DM(MachineProxy machine, EnigmaDictionary dictionary, String encrtyptedString, int numAgents, int taskSize, int level, Mutex mutex, int port){
        this(machine, dictionary, encrtyptedString, numAgents,taskSize, level);
        this.mainMutex = mutex;
        try {
            this.serverSocket = new ServerSocket(port, 0, InetAddress.getLoopbackAddress());
        }catch (Exception e){
            e.printStackTrace();
        }
        agentSockets = new ArrayList<>();
    }


    public DM init(){
        this.calcNumOfEasyTasks()
                .createTasksQueues()
                .connectAgents();

        this.levels.add(() ->this.handleEasyTasks());
        this.levels.add(() ->this.handleMediumTasks());
        this.levels.add(() -> this.handleHardTasks());
        this.levels.add(() ->this.handleImpossibleTasks());
        return this;
    }

    private void connectAgents() {
        agentOutputStreams = new ArrayList<>();
        for (int i = 0; i < numAgents ; i++) {

            Socket socket = null;
            try {
                socket = serverSocket.accept();
                this.agentSockets.add(socket);
                agentOutputStreams.add(new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean handleEasyTasks(){
        this.createEasyTasks()
                .deliverTasksToQueues()
                .workProtocolAgents();


            while (processedTaskQueues < numQueues) {
                AgentAnswer potential = getAnswerFromSockets();
                if (potential != null)
                    if (potential.getEncryptedString().equals(AGENT_FINISHED_TASKS)) {
                        processedTaskQueues++;
                        if(processedTaskQueues == numQueues)
                            break;

                        //add this logic when making numqueues and numagents different

//                        if(tasksQueues.size() > 0)
//                        {
//                            workProtocolSingleAgent(potential.getAgentId());
//                        }
                    }
                    else {
                        if (decryptPotentials.contains(potential.getEncryptedString()) == false)
                            decryptPotentials.add(potential);
                    }

                //returns boolean
                handleInterrupts();
                //initiate new Agent connections
            }

        processedTaskQueues = 0;

            if(wantedLevel == TaskLevels.levelEasy) {
                System.out.println(createEndOfDecryptionInfo());
                this.interruptReason = EnigmaAgent.InterruptReason.STOP;
                makeWebAgentsToStop();
            }

            else{
                notifyAgentsThereAreMoreTasks();
            }

            return true;
    }

    private void workProtocolSingleAgent(int agentId) {
        ObjectOutputStream socketOutputStream = null;
        int index = 0;
        for (Socket agentSocket : agentSockets) {
            if (agentSocket.getPort() == agentId)
            {
                socketOutputStream = agentOutputStreams.get(index);
            }
            index++;
        }
        try {
            socketOutputStream.writeObject(MORE_TASKS);
            socketOutputStream.writeObject(machine);
            socketOutputStream.writeObject(enigmaDictionary);
            socketOutputStream.writeObject(tasksQueues.get(processedTaskQueues));
            socketOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeWebAgentsToStop() {
        try {
            for (ObjectOutputStream socketOutputStream : agentOutputStreams) {
                socketOutputStream.writeObject(AgentConstants.FINISH_WORK);
                socketOutputStream.flush();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void notifyAgentsThereAreMoreTasks() {
        try {
            for (ObjectOutputStream socketOutputStream : agentOutputStreams) {
                socketOutputStream.writeObject(AgentConstants.MORE_TASKS);
                socketOutputStream.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private AgentAnswer getAnswerFromSockets() {
        AgentAnswer agentAnswer = null;
        try {
            if (agentInputStreams == null)
                initAgentInputStreams();

            agentAnswer = (AgentAnswer)agentInputStreams.get(agentToAskAns).readObject();
            agentToAskAnsHandle();

//            for (ObjectInputStream agentInputStream : agentInputStreams) {
//                agentAnswer = (AgentAnswer) agentInputStream.readObject();
//            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //make is as a list!//
        return agentAnswer;
    }

    private void agentToAskAnsHandle() {
        agentToAskAns++;
        if(agentToAskAns == agentInputStreams.size())
            agentToAskAns = 0;
    }


    private void initAgentInputStreams() {
        agentInputStreams = new ArrayList<>();
        for (int socketIndex = 0; socketIndex < agentSockets.size(); socketIndex++) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(agentSockets.get(socketIndex).getInputStream()));
                agentInputStreams.add(objectInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void workProtocolAgents(){
        try {
            for (ObjectOutputStream socketOutputStream : agentOutputStreams) {
                socketOutputStream.writeObject(machine);
                socketOutputStream.writeObject(enigmaDictionary);
                socketOutputStream.writeObject(tasksQueues.get(processedTaskQueues));
                socketOutputStream.flush();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }




    private boolean handleInterrupts() {
        if (interruptReason != EnigmaAgent.InterruptReason.FREE) {
            if (interruptReason == EnigmaAgent.InterruptReason.SUSPEND)
                stopProcessing();

            if (interruptReason == EnigmaAgent.InterruptReason.INFOS)
                createInfosHalfWay();

            if(interruptReason == EnigmaAgent.InterruptReason.STOP){
                finishDmProcess();
                return false;
            }
        }
        return true;
    }

    private void finishDmProcess() {
        System.out.println(createEndOfDecryptionInfo());
        wantedLevel = -1; //so printing wont happen again
    }

    public List<String> createEndOfDecryptionInfo(){
        List<String> endOfDecryptionInfo = new ArrayList<>();
        endOfDecryptionInfo.add("total processing time = " + calcTotalTime()  + "\n");
        endOfDecryptionInfo.add("total number of processed strings = " + totalTasksDelivered + "\n");
        int tasksPerAgent = totalTasksDelivered /taskSize / numQueues;
        endOfDecryptionInfo.add("Encrypted Strings: \n " + decryptPotentials.toString());

        return endOfDecryptionInfo;

    }

    private String calcTotalTime() {
        long totalMili = System.currentTimeMillis() - startTime;
        long seconds = totalMili / 1000;
        long leftMili = totalMili % 1000;
        String res = seconds + ":" + leftMili;
        return res;
    }

    private void stopProcessing() {

        ;
    }

    public boolean handleMediumTasks(){
        for (Reflector reflector : reflectors) {
            this.machine.setChosenReflector(RomanInterpeter.numToRoman(reflector.getId()));
            if(handleEasyTasks() == false){
                return false;
            }
        }
        if(wantedLevel == TaskLevels.levelMedium) {
            System.out.println(createEndOfDecryptionInfo());
            this.interruptReason = EnigmaAgent.InterruptReason.STOP;
            makeWebAgentsToStop();
        }

        else{
            notifyAgentsThereAreMoreTasks();
        }

            return true;

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
    public boolean handleHardTasks(){
        setPermutationForRotorIdsHelper();
        Pair<Integer, Integer>[] rotorsAndLocations = new Pair[machine.getAppliedRotors()];
        for (List<Integer> rotorIdsPermutation : rotorIdsPermutations) {
            for (int i = 0; i < machine.getAppliedRotors() ; i++) {
                rotorsAndLocations[i] = new Pair<>(rotorIdsPermutation.get(i), 1);
            }
            machine.setChosenRotors(rotorsAndLocations);
            if(handleMediumTasks() == false){
                return false;
            }
        }

        if(wantedLevel == TaskLevels.levelHard) {
            System.out.println(createEndOfDecryptionInfo());
            this.interruptReason = EnigmaAgent.InterruptReason.STOP;
            makeWebAgentsToStop();
        }

        else{
            notifyAgentsThereAreMoreTasks();
        }

            return true;

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


    public boolean handleImpossibleTasks(){
        setNoverKForRotorIdsHelper();
        Pair<Integer, Integer>[] rotorsAndLocations = new Pair[machine.getAppliedRotors()];
        for (List<Integer> rotoridsNoverKOneOption : rotorIdsNoverK) {
            for (int i = 0; i < machine.getAppliedRotors(); i++) {
                rotorsAndLocations[i] = new Pair<>(rotoridsNoverKOneOption.get(i), 1);
            }
            machine.setChosenRotors(rotorsAndLocations);
            if(handleHardTasks() == false){
                return false;
            }
        }

        if(wantedLevel == TaskLevels.levelImpossible) {
            System.out.println(createEndOfDecryptionInfo());
            this.interruptReason = EnigmaAgent.InterruptReason.STOP;
            makeWebAgentsToStop();
        }

        else{
            notifyAgentsThereAreMoreTasks();
        }


            return true;
    }

    private DM deliverTasksToQueues() {
        int deliveredTasks = 0;
            for (List<EasyTask> tasksQueue : tasksQueues) {
                int numOfTasksPerList = (numOfEasyTasks / numQueues);
                for (int i = 0; i < numOfTasksPerList; i++) {
                    tasksQueue.add(easyTasks.get(deliveredTasks));
                    deliveredTasks++;
                }
                //last mission for Agent to stop
                tasksQueue.add(new EasyTask(null, null, 0));
            }

            if(deliveredTasks < numOfEasyTasks) {
                tasksQueues.get(0).remove(tasksQueues.get(0).size() -1);
                while (deliveredTasks < numOfEasyTasks) {
                    tasksQueues.get(0).add(easyTasks.get(deliveredTasks));
                    deliveredTasks++;
                }
                tasksQueues.get(0).add(new EasyTask(null, null, 0
                ));
            }
            this.totalTasksDelivered += deliveredTasks * taskSize;
            ;//TODO::handle all interrupts in DM
        return this;
    }




    @Override
    public void run() {
        this.init();
        startTime = System.currentTimeMillis();
        this.levels.get(wantedLevel - 1).run();
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
        for (int i = 0; i < numQueues; i++) {
            this.tasksQueues.add(new ArrayList<>());
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
        for (List<EasyTask> tasksQueue : tasksQueues) {
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


        int percentageLeft = ((totalTasksDelivered * 100) / totalTasksOptions);
        while(percentageLeft > 99)
                percentageLeft = percentageLeft / 10;

        HalfWayInfo halfWayInfo = new HalfWayInfo(tempAgentAnsewer, percentageLeft, missionsForAgent);
    }

    public void setHalfWayInfoQueue(BlockingQueue<HalfWayInfo> halfWayInfoQueue) {
        ;
    }

    public void setTotalTasksOptions(int totalTasksOptions) {
        this.totalTasksOptions = totalTasksOptions;
    }
}
