package enigmaAgent;

import java.io.Serializable;

public class AgentAnswer implements Serializable {
    private String encryptedString;
    private long missionTime;
    private int agentId;
    private String decryptedString;
    private String machineCode;

    public AgentAnswer(String encryptedString, String decryptedString, long missionTime, int agentId, String machineCode) {
        this.encryptedString = encryptedString;
        this.missionTime = missionTime;
        this.agentId = agentId;
        this.machineCode = machineCode;
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
        String res =  "Encrypted String: " + encryptedString + ", Agent ID: " + agentId + ", Mission Time: " + missionTime  +
                ", Machine Code: " + machineCode + "\n";
        return res;
    }
}
