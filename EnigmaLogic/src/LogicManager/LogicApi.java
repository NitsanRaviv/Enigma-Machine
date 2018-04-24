package LogicManager;

import java.util.List;

public interface LogicApi {
     List<String> getMachineSpecification();

     String loadMachineFromXml(String path);

     String processInput(String input);

     String resetCode();

     void setInitialCode(String[] rotors, String[] rotorMap, String chosenReflector);

     String setRandomMachineCode();

}


