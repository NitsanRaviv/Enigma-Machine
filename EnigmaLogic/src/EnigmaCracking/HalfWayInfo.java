package EnigmaCracking;

import enigmaAgent.AgentAnswer;

import java.util.List;

public class HalfWayInfo {
    private List<AgentAnswer> agentAnswers;
    private int percentageLeft;
    private List<String> leftMissionPerAgent;

    public HalfWayInfo(List<AgentAnswer> agentAnswers, int percentageLeft, List<String> leftMissionPerAgent) {
        this.agentAnswers = agentAnswers;
        this.percentageLeft = percentageLeft;
        this.leftMissionPerAgent = leftMissionPerAgent;
    }


    @Override
    public String toString() {
        return "Half Way Info{" + "\n" +
                "Agent Answers = " + agentAnswers + "\n" +
                ", Percentage Completion=" + percentageLeft + "%" + "\n" +
                ", Left Mission Per Agent=" + leftMissionPerAgent +
                '}';
    }
}
