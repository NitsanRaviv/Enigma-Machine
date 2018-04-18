package LogicManager;

public interface LogicApi {
     String getMachineSpecification();
     void loadMachineFromXml(String path);
     String processInput(String input);
     void resetCode();
}
