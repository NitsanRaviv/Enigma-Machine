package enigmaAgent;

import Machine.MachineProxy;
import Tasks.EasyTask;
import agentUtilities.EnigmaDictionary;
import sun.awt.Mutex;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static enigmaAgent.AgentConstants.*;

public class EnigmaWebAgent {
    private EnigmaAgent agent;
    private MachineProxy machineProxy;
    private BlockingQueue<EasyTask> easyTasks;
    private BlockingQueue<AgentAnswer> agentAnswers;
    private List<EasyTask> easyTasksList;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private int port;
    private Mutex lock;
    private EnigmaDictionary dictionary;
    private List<AgentAnswer> answerList;
    private boolean continueWorking = true;

    public EnigmaWebAgent(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        this.connect();
        while(continueWorking) {
            runningAgent();
            sendAnswerToServer();
            waitForFurtherInstractions();
        }
    }

    private void runningAgent() {
        boolean agentFinished = false;
        initiateAgent();
        agent.start();
        while (agentFinished == false) {
            AgentAnswer potential = agentAnswers.poll();

            if (potential != null)
                if (potential.getEncryptedString().equals(AGENT_FINISHED_TASKS)) {
                    agentFinished = true;
                    answerList.add(potential);
                    break;

                } else {
                    if (answerList.contains(potential.getEncryptedString()) == false) {
                        answerList.add(potential);
                        System.out.println("found a potential string " + potential.toString());
                    }

                }
        }

        System.out.println("WebAgent: finished running agent-thread, waiting DM will give me further instructions");
    }


    private void waitForFurtherInstractions() {
        try {
            String instruction = (String) inputStream.readObject();
            switch (instruction) {
                case (MORE_TASKS): {
                    System.out.println("DM has given me further tasks.");
                    break;
                }

                case (FINISH_WORK): {
                    System.out.println("Web Agent finished, asta la vista");
                    continueWorking = false;
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendAnswerToServer() {
        try {
            for (AgentAnswer agentAnswer : agentAnswers) {
                answerList.add(agentAnswer);
            }

            for (AgentAnswer agentAnswer : answerList) {
                outputStream.writeObject(agentAnswer);
            }

            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initiateAgent() {
        try {
            answerList = new ArrayList<>();
            getMachineFromSocket()
                    .getDictionaryFromSocket()
                    .getTasksFromSocket()
                    .buildAgent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EnigmaWebAgent connect() throws IOException {
        this.socket = new Socket(InetAddress.getLoopbackAddress(), port);
        System.out.println("WebAgent: connected to remote DM");
        this.inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        System.out.println("WebAgent: got socket input stream - I have data awaits for me");
        this.outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        return this;
    }

    private EnigmaWebAgent getTasksFromSocket() throws Exception {
        easyTasksList = (List<EasyTask>) inputStream.readObject();
        this.easyTasks = new ArrayBlockingQueue<EasyTask>(10000);
        for (EasyTask easyTask : easyTasksList) {
            this.easyTasks.add(easyTask);
        }
        return this;
    }

    private EnigmaWebAgent getMachineFromSocket() throws Exception {
        machineProxy = (MachineProxy) inputStream.readObject();
        return this;
    }

    private EnigmaWebAgent getDictionaryFromSocket() throws Exception {
        dictionary = (EnigmaDictionary) inputStream.readObject();
        return this;
    }

    private EnigmaWebAgent buildAgent() {
        this.agentAnswers = new ArrayBlockingQueue<AgentAnswer>(10000);
        this.agent = new EnigmaAgent(machineProxy, agentAnswers, easyTasks, dictionary, port, lock);
        return this;
    }
}
