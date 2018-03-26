package Parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rotor {
    private List<Integer> entries = new ArrayList<>();

    public Rotor(List<Integer> entries)
    {
        this.entries = entries;
    }

    public void rotateRotorOneRound()
    {
        if(this.entries != null) {
            Collections.rotate(entries, entries.size() - 1);
        }
    }

    public List<Integer> getEntries() {
        return entries;
    }
}
