package EnigmaCompetition;

import EnigmaCracking.DM;
import EnigmaCracking.AllyObserver;
import LogicManager.Integrator;
import sun.awt.Mutex;

public class Ally implements AllyObserver {
    private String username;
    private Integrator integrator;
    private DM dm;
    private int port;
    private Competition competition;
    private Mutex mutex;
    private State state;
    private CompetitionObserver competitionObserver;

    public void setCompetitionObserver(CompetitionObserver competitionObserver) {
        this.competitionObserver = competitionObserver;
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
    public void notifyAlly(String potential) {
        //verify not to much threads are opening
        Thread thread = new Thread(()->
        this.competitionObserver.notifyFoundPotential(potential, this));
        thread.start();
    }

    public enum State {
        inActive, waitingForAgents,waitingToStart, dmProcessing,
    }

}
