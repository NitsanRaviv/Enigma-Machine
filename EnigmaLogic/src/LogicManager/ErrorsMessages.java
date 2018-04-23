package LogicManager;

public interface ErrorsMessages {

    public String noErrors = "";
    public String errNoMachine = "Tried get info about the machine without load one";
    public String errXML = "This file is not xml type or invalid";
    public String errGetMachine = "Error reading machine";
    public String errABCSize = "ABC size is not even";
    public String errAmountOfRotor = "There is not enough rotors in the file";
    public String errLessThenTwoRotor = "There is less then two rotors in the file";
    public String errIDsRotors = "Error in rotors ids";
    public String errDuplicateMappings = "Error duplicate mappings";
    public String errNotch = "Error notch";
    public String errIDsReflector = "Error in reflector ids";
    public String errReflect = "Reflect error";
}
