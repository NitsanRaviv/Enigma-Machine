package LogicManager.InitialCode;

import Utilities.LanguageInterpeter;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class InitialCodeParser {
    public static List<Pair<Integer,Integer>> parseRotors(String[] rotorIds, String[] rotorMap, LanguageInterpeter languageInterpeter) {
        List<Pair<Integer,Integer>> parsedRotors = new ArrayList<>();
        for(int index = 0; index < rotorIds.length; index++){
            parsedRotors.add(new Pair<>(Integer.parseInt(rotorIds[index]),languageInterpeter.characterToNum(rotorMap[index])));
        }
        return parsedRotors;
    }

    public static String[] convertStringtoArrayByRegex(String string, String regex){
        return string.split(regex);
    }
}
