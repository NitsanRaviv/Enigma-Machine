package EnigmaCracking;

import enigmaAgent.AgentAnswer;

import java.util.List;

public class HalfWayInfo {
    private List<AgentAnswer> agentAnswers;
    private double percentageLeft;
    private List<String> leftMissionPerAgent;

    public HalfWayInfo(List<AgentAnswer> agentAnswers, double percentageLeft, List<String> leftMissionPerAgent) {
        this.agentAnswers = agentAnswers;
        this.percentageLeft = percentageLeft;
        this.leftMissionPerAgent = leftMissionPerAgent;
    }


    @Override
    public String toString() {
        return "Half Way Info{" + "\n" +
                "Agent Answers = " + agentAnswers + "\n" +
                ", Percentage Left=" + percentageLeft + "\n" +
                ", Left Mission Per Agent=" + leftMissionPerAgent +
                '}';
    }
}
