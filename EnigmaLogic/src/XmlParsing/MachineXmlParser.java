package XmlParsing;

import Machine.MachineBuilder;
import Machine.MachineProxy;
import XmlParsing.JaxbClasses.*;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import javax.xml.bind.*;
import java.io.File;
import java.util.List;


public class MachineXmlParser {
    private static Enigma jaxbEnigma;

    public static Enigma parseXmltoJaxbMachine(String filePath) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = null;
        jaxbEnigma = null;
        File xmlFilePath = new File(filePath);
        JAXBContext jaxbContext = null;
        jaxbContext = JAXBContext.newInstance(Enigma.class);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        jaxbEnigma = (Enigma) jaxbUnmarshaller.unmarshal(xmlFilePath);
        return jaxbEnigma;
    }

    public static MachineProxy parseXmlToMachineProxy(String filePath) throws JAXBException {
        if(jaxbEnigma == null){
            parseXmltoJaxbMachine(filePath);
        }
        Machine jaxbMachine = jaxbEnigma.getMachine();
        MachineBuilder machineBuilder = new MachineBuilder();
        machineBuilder.initMachine(jaxbMachine.getABC().trim().toCharArray(), jaxbEnigma.getMachine().getRotorsCount());
        setMachineProxyReflectors(machineBuilder, jaxbMachine);
        setMachineProxyRotors(machineBuilder, jaxbMachine);
        return machineBuilder.create();
    }

    private static void setMachineProxyReflectors(MachineBuilder machineBuilder, Machine jaxbMachine) {
        Reflectors reflectors = jaxbMachine.getReflectors();
        List<Reflector> reflectorList = reflectors.getReflector();
        for(Reflector refl : reflectorList)
        {
            int[] from = getReflectedValues(refl.getReflect(), "from");
            int[] to = getReflectedValues(refl.getReflect(), "to");
            machineBuilder.setReflector(from, to, refl.getId());
        }

    }

    private static int[] getReflectedValues(List<Reflect> reflectedList, String reflectedOrigin) {
        int[] reflectedVal = new int[reflectedList.size()];
        int index = 0;
        for(Reflect refl : reflectedList)
        {
            if(reflectedOrigin.equals("from"))
                reflectedVal[index] = reflectedList.get(index).getInput();
            else if(reflectedOrigin.equals("to"))
                reflectedVal[index] = reflectedList.get(index).getOutput();
            index++;
        }
        return reflectedVal;
    }

    private static void setMachineProxyRotors(MachineBuilder machineBuilder, Machine jaxbMachine) {
        Rotors jaxbRotors = jaxbMachine.getRotors();
        List<Rotor> jaxbRotorsList = jaxbRotors.getRotor();
        for(Rotor rotor : jaxbRotorsList)
        {
          String rightEntries = getEntries(rotor.getMapping(), "right");
          String leftEntries = getEntries(rotor.getMapping(), "left");
          machineBuilder.setRotor(rightEntries,leftEntries,rotor.getNotch() - 1, rotor.getId()); // add id!
        }
    }

    private static String getEntries(List<Mapping> mapping, String entry) {
        String entries = new String();
        for(Mapping mapp : mapping)
        {
            if(entry.equals("right"))
                entries += mapp.getRight();
            else if(entry.equals("left"))
                entries += mapp.getLeft();

        }
        return entries;
    }
}
