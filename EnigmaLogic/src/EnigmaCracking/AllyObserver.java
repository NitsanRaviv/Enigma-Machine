package EnigmaCracking;

public interface AllyObserver {
    void notifyAlly(String potential, int agentId);
    void notifyFinished();
}
