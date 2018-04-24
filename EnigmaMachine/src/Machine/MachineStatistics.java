package Machine;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineStatistics {
    private Map<String,List<Pair<String,Pair<String,Long>>>> initialCodeToStat;
    public MachineStatistics(String initialCode){
        this.initialCodeToStat = new HashMap<>();
        this.initialCodeToStat.put(initialCode, new ArrayList<>());
    }

    public void setNewInitialCode(String initialCode){
        if(initialCodeToStat.get(initialCode) == null){
            this.initialCodeToStat.put(initialCode, new ArrayList<>());
        }
    }

    public void addOrigStringDestStringAndTime(String initialCode, String orig, String dest, long time){
        this.initialCodeToStat.get(initialCode).add(new Pair(orig, new Pair(dest, time)));
    }

    public String getStatistics(){
        return this.initialCodeToStat.toString() + "\n" + "Average time: " + getAvergeTimes();
    }

    public String getAvergeTimes(){
        double avgTime = 0;
        int div = 0;
        for(Map.Entry<String, List<Pair<String,Pair<String,Long>>>> entry : this.initialCodeToStat.entrySet()) {
            for (Pair<String,Pair<String,Long>> p : entry.getValue()) {
                avgTime += p.getValue().getValue().doubleValue();
                div ++;
            }
        }
        if(div != 0){
            avgTime /= div;
        }
        return Double.toString(avgTime);
    }
}
