package Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageInterpeter {
    private List<Character> language;
    private List<Integer> numbers;
    private Map<Character, Integer> dictionary;


    public LanguageInterpeter(List<Character> language) {
        int number = 0;
        this.language = language;
        dictionary = new HashMap<>(language.size());
        for (Character c : language) {
            dictionary.put(c, number);
            number++;
        }

    }

}
//
//    public List<Character> numberToLetters(List<Integer> numbers)
//    {
//        private List<Character> res = new ArrayList<>(numbers.size());
//        for (Integer i: numbers )
//        {
//           res[index] = dictionary[]
//        }
//
//    }
//}
