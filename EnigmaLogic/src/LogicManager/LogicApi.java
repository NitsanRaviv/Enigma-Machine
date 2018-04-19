package LogicManager;

public interface LogicApi {
     String getMachineSpecification();
     boolean loadMachineFromXml(String path, String msg);
     String processInput(String input);
     void resetCode();
}
