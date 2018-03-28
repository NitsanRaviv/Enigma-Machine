package Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageInterpeter {
    private List<Character> language;
    private Map<Character, Integer> dictionary;


    public LanguageInterpeter(List<Character> language) {
        int number = 1;
        this.language = language;
        dictionary = new HashMap<>(language.size());
        for (Character c : language) {
            dictionary.put(c, number);
            number++;
        }


    }



    public List<Character> numberToLetters(List<Integer> numbers)
    {
         List<Character> res = new ArrayList<>(numbers.size());
         int index = 0;
         for(Integer i : numbers) {
            res.add(index, language.get(i-1));
            index++;
         }
         return res;
    }
}
