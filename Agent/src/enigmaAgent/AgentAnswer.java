package enigmaAgent;

public class AgentAnswer {
    private String encryptedString;
    private long missionTime;
    private int agentId;
    private String decryptedString;

    public AgentAnswer(String encryptedString, String decryptedString, long missionTime, int agentId) {
        this.encryptedString = encryptedString;
        this.missionTime = missionTime;
        this.agentId = agentId;
        this.decryptedString = decryptedString;
    }

    public String getEncryptedString() {
        return encryptedString;
    }

    public long getMissionTime() {
        return missionTime;
    }

    public int getAgentId() {
        return agentId;
    }

    @Override
    public boolean equals(Object decryptedString) {
        return this.decryptedString.equals(decryptedString);
    }

    @Override
    public String toString(){
        String res = "Agent ID: " + agentId + ", Mission Time: " + missionTime + ", Encrypted String: " + encryptedString + "\n";
        return res;
    }
}
