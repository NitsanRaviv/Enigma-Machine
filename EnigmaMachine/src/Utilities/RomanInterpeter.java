package Utilities;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class RomanInterpeter {
    private static Set<Pair<String,Integer>> romanDict;

    private static void setRomanInterpeter() {
        romanDict = new HashSet<>();
        romanDict.add(new Pair<>("I", 1));
        romanDict.add(new Pair<>("II", 2));
        romanDict.add(new Pair<>("III", 3));
        romanDict.add(new Pair<>("IV", 4));
        romanDict.add(new Pair<>("V", 5));
    }

    public static int romanToNum(String roman){
        if(romanDict == null)
            setRomanInterpeter();
        for(Pair<String, Integer> p : romanDict){
            if(p.getKey().toUpperCase().equals(roman)){
                return p.getValue();
            }
        }
        return -1;
    }

    public static String numToRoman(int fig){
        if(romanDict == null)
            setRomanInterpeter();
        for(Pair<String, Integer> p : romanDict){
            if(p.getValue().equals(fig)){
                return p.getKey();
            }
        }
        return null;
    }
}
