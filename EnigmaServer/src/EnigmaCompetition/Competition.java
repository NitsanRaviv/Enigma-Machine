package EnigmaCompetition;

import EnigmaCracking.Tasks.TaskLevels;
import LogicManager.Integrator;
import XmlParsing.JaxbClasses.Battlefield;
import agentUtilities.EnigmaDictionary;

import java.util.ArrayList;
import java.util.List;

public class Competition {
    private Ubout uboat;
    private List<Ally> allies;
    private Integrator integrator;
    private String decryptedString;
    private String encryptedString;
    private Battlefield battlefield;
    private EnigmaDictionary dictionary;
    private int taskLevel;

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
        if(ally == null)
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
}
