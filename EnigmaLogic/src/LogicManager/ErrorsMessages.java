package LogicManager;

public interface ErrorsMessages {

     String noErrors = "";
     String errNoMachine = "Cant send information about the machine without loading it first from file";
     String errXML = "This file is not xml type or invalid xml";
     String errGetMachine = "Error reading machine";
     String errABCSize = "ABC size is not even";
     String errAmountOfRotor = "There is not enough rotors in the file";
     String errLessThenTwoRotor = "There is less then two rotors in the file";
     String errIDsRotors = "Error in rotor ids";
     String errDuplicateMappings = "Error duplicate mappings";
     String errNotch = "Notch error";
     String errIDsReflector = "Error in reflector ids";
     String errReflect = "Reflector error";
}
