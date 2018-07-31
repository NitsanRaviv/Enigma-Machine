package EnigmaCompetition;

import EnigmaCracking.DM;
import EnigmaCracking.AllyObserver;
import LogicManager.Integrator;
import enigmaAgent.EnigmaAgent;
import sun.awt.Mutex;

import java.util.*;

public class Ally implements AllyObserver {
    private String username;
    private Integrator integrator;
    private DM dm;
    private int port;
    private Competition competition;
    private Mutex mutex;
    private State state;
    private CompetitionObserver competitionObserver;
    private Map<Integer, Set<String>> idToPotentials = new HashMap<>();

    public Ally(String username, int port, Mutex allyMutex) {
        this.username = username;
        this.port = port;
        this.mutex = allyMutex;
    }

    public void setCompetitionObserver(CompetitionObserver competitionObserver) {
        this.competitionObserver = competitionObserver;
    }


    public void stopDM(){
        dm.setInterruptReason(EnigmaAgent.InterruptReason.STOP);
    }


    public String getUsername() {
        return username;
    }

    public Integrator getIntegrator() {
        return integrator;
    }

    public DM getDm() {
        return dm;
    }

    public int getPort() {
        return port;
    }

    public Ally(String username, DM dm, int port, Competition competition, Mutex allyMutex){
        this.username = username;
        this.dm = dm;
        this.port = port;
        this.competition = competition;
        this.mutex = allyMutex;
        this.state = State.inActive;
    }


    public Ally(String username,  int port, Competition competition, Mutex allyMutex){
        this.username = username;
        this.port = port;
        this.competition = competition;
        this.mutex = allyMutex;
        this.state = State.inActive;
    }
    
    public boolean checkReady() {
       return this.state.equals(State.waitingToStart);
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Mutex getMutex() {
        return mutex;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void notifyAlly(String potential, int agentId) {
        //verify not to much threads are opening
        Thread thread = new Thread(()->
        this.competitionObserver.notifyFoundPotential(potential, this));
        thread.start();

        Set<String> potentials = idToPotentials.get(agentId);
        if(potentials == null) {
            potentials = new HashSet<>();
            idToPotentials.put(agentId, potentials);
        }
        potentials.add(potential);
    }

    @Override
    public void notifyFinished() {
        this.state = State.finished;
    }

    public Competition getCompetition() {
        return competition;
    }

    public String getNumAgents() {
        return String.valueOf(dm.getNumAgents());
    }

    public String getAgentPotentials() {
        return idToPotentials.toString();
    }

    public enum State {
        inActive, waitingForAgents,waitingToStart, dmProcessing, finished
    }

    public void setDm(DM dm) {
        this.dm = dm;
    }


}
