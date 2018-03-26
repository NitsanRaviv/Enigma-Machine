package Parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import Constants.*;

public class Rotor {
    private List<Integer> entries = new ArrayList<>();
    private int zeezIndex =  MachineConstants.DEFAULT_ZEEZ_INDEX;

    public Rotor(List<Integer> entries)
    {
        this.entries = entries;
    }

    public Rotor(List<Integer> entries, int zeezIndex)
    {
        this.entries = entries;
        this.zeezIndex = zeezIndex;
    }

    public void rotateRotorOneRound()
    {
        if(this.entries != null) {
            Collections.rotate(entries, entries.size() - 1);
        }

        if(zeezIndex == 0)
        {
            //rotateAdjacentRotorIfExists
        }
    }

    public List<Integer> getEntries() {
        return entries;
    }

    public void setZeezIndex(int zeezIndex) {
        this.zeezIndex = zeezIndex;
    }
}
