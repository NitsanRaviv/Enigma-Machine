package EnigmaCompetition;

import LogicManager.Integrator;
import XmlParsing.JaxbClasses.Battlefield;
import agentUtilities.EnigmaDictionary;

import java.util.ArrayList;
import java.util.List;

public class Competition implements CompetitionObserver {
    private Ubout uboat;
    private List<Ally> allies = new ArrayList<>();
    private Integrator integrator;
    private String decryptedString;
    private String encryptedString;
    private Battlefield battlefield;
    private EnigmaDictionary dictionary;
    private int taskLevel;
    private boolean competitionReady = false;
    private boolean foundString;
    private String winner = "noWinner";
    private boolean finished = false;
    private boolean running = false;

    public boolean isRunning() {
        return running;
    }

    public boolean isFinished() {
        return finished;
    }


    public String getWinner() {
        return winner;
    }


    public EnigmaDictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(EnigmaDictionary dictionary) {
        this.dictionary = dictionary;
    }


    public int getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(int taskLevel) {
        this.taskLevel = taskLevel;
    }


    public String getEncryptedString() {
        return encryptedString;
    }

    public void addAlly(Ally ally) {
        if (this.allies == null)
            this.allies = new ArrayList<>();

        this.allies.add(ally);
    }

    public void setUboat(Ubout uboat) {
        this.uboat = uboat;
    }


    public void setIntegrator(Integrator integrator) {
        this.integrator = integrator;
    }

    public Integrator getIntegrator() {
        return integrator;
    }

    public void setDecryptedString(String decryptedString) {
        this.decryptedString = decryptedString;
    }

    public void setEncryptedString(String encryptedString) {
        this.encryptedString = encryptedString;
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public void setBattlefield(Battlefield battlefield) {
        this.battlefield = battlefield;
    }

    public Ubout getUboat() {
        return uboat;
    }

    public boolean checkReady() {

        if (battlefield.getAllies() > allies.size())
            return false;

        for (Ally ally : allies) {
            if (ally.checkReady() == false)
                return false;
        }
        return true;
    }

    public List<Ally> getAllies() {
        return this.allies;
    }

    public void runCompetition() {
        winner = "noWinner";
        this.running = true;
        runAllies();
        boolean alliesFinished = false;
        foundString = false;
        while (alliesFinished == false && foundString == false) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (alliesStillProccessing() == false && foundString == false) {
                alliesFinished = true;
                //update json that will be sent to allies - losers
                //make dm stop!
            }
        }
        if(this.finished == false){
            finished = true;
            stopCompetition();
        }
        System.out.println("competition finished");
    }

    private void runAllies() {
        for (Ally ally : allies) {
            synchronized (ally.getMutex()) {
                ally.setState(Ally.State.dmProcessing);
                ally.getMutex().notifyAll();
                System.out.println(ally.getUsername() + " started contest");
            }
        }
    }

    private boolean alliesStillProccessing() {
        for (Ally ally : allies) {
            //there is at least one ally that processes data
            if (ally.getState() == Ally.State.dmProcessing)
                return true;
        }
        return false;
    }

    @Override
    public void notifyFoundPotential(String potential, Ally ally) {
        //add here a check if competition is finished
        synchronized (this) {
            if (potential.equals(this.decryptedString)) {
                if(foundString == false) {
                    foundString = true;
                    stopCompetition();
                    System.out.println("found potential! winner is: " + ally.getUsername() + " encrypted string: " + potential);
                    winner = ally.getUsername();
                }
                else{
                    //maybe return a value here???? so ally and Dm will know to stop
                }
            } else if (foundString == false) {
                System.out.println("ally " + ally.getUsername() + " didnt found the correct string, found string: " + potential);
            }
        }
    }

    private void stopCompetition() {
        for (Ally ally : allies) {
            ally.stopDM();
        }
        this.finished = true;
        this.running = false;
    }

    public String getAlliesAndAgents() {
        StringBuilder sb = new StringBuilder();
        for (Ally ally : allies) {
            sb.append(ally.getUsername()+ ": " + ally.getNumAgents() + " ");
        }
        return sb.toString();
    }

    public String getAllAllyPotentials() {
        StringBuilder sb = new StringBuilder();
        for (Ally ally : allies) {
            sb.append(ally.getAgentPotentials());
        }
        return sb.toString();
    }
}
