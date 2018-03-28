package Parts;

import java.util.HashMap;
import java.util.Map;

public class Reflector {
    private Map<Integer, Integer> relectedValues;

    public Reflector(Map<Integer, Integer> oneWayMapping) {
        this.relectedValues = new HashMap();
        for(Integer i : oneWayMapping.keySet())
        {
            this.relectedValues.put(i, oneWayMapping.get(i));
        }
        for(Integer i : oneWayMapping.keySet())
        {
            this.relectedValues.put(oneWayMapping.get(i),i );
        }
    }

    public Integer getReflectedValue(int entrance)
    {
        return this.relectedValues.get(entrance);
    }
}
