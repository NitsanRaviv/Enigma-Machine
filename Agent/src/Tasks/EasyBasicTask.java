//package Tasks;
//
//import Parts.Reflector;
//import Parts.Rotor;
//import java.util.HashMap;
//import java.util.Map;
//
//public class EasyBasicTask extends BasicTask {
//
//    protected Map<Integer,Rotor> rotorsAndPositions; // pos -> key , rotor -> value
//    private Reflector reflector;
//
//    public EasyBasicTask(String initialCode, int taskSize, Reflector reflector, Map<Integer,Rotor> rotorsAndPositions) {
//        super(initialCode, taskSize);
//        this.reflector = reflector;
//        this.rotorsAndPositions = new HashMap<>();
//        this.rotorsAndPositions = rotorsAndPositions;
//    }
//
//    // ctr without reflector
//    public EasyBasicTask(String initialCode, int taskSize, Map<Integer,Rotor> rotorsAndPositions) {
//        super(initialCode, taskSize);
//        this.rotorsAndPositions = new HashMap<>();
//        this.rotorsAndPositions = rotorsAndPositions;
//    }
//
//    public Rotor getRotorByPosition(Integer position){
//        return rotorsAndPositions.get(position);
//    }
//
//    public Reflector getReflector() {
//        return reflector;
//    }
//
//    public Map<Integer, Rotor> getRotorsAndPositions() {
//        return rotorsAndPositions;
//    }
//
//    @Override
//    public String getInitialCode() {
//        return super.getInitialCode();
//    }
//
//    @Override
//    public int getTaskSize() {
//        return super.getTaskSize();
//    }
//}
