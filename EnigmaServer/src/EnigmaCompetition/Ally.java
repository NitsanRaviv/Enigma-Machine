package EnigmaCompetition;

import EnigmaCracking.DM;
import LogicManager.Integrator;

public class Ally {
    private String username;
    private Integrator integrator;
    private DM dm;
    private int port;

    public Ally(String username, DM dm, int port){
        this.username = username;
        this.dm = dm;
        this.port = port;
    }
}
