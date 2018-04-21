package LogicManager;

import java.util.List;

public interface LogicApi {
     List<String> getMachineSpecification();
     boolean loadMachineFromXml(String path, String msg);
     String processInput(String input);
     String resetCode();
     boolean setInitialCode(String[] rotors,String[] rotorMap,String chosenReflector);
}
