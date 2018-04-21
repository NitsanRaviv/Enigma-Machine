package Parts;

import java.util.HashMap;
import java.util.Map;

public class Reflector {
    private Map<Integer, Integer> relectedValues;
    private int id;

    public Reflector(Map<Integer, Integer> oneWayMapping, int id) {
        this.relectedValues = new HashMap();
        this.id = id;
        for(Integer i : oneWayMapping.keySet())
        {
            this.relectedValues.put(i, oneWayMapping.get(i));
        }
        for(Integer i : oneWayMapping.keySet())
        {
            this.relectedValues.put(oneWayMapping.get(i),i);
        }
    }

    public Integer getReflectedValue(int entrance)
    {
        return this.relectedValues.get(entrance);
    }

    public int getId() {
        return id;
    }
}
