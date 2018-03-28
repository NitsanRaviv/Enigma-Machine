package Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageInterpeter {
    private char[] language;
    private Map<Character, Integer> dictionary;


    public LanguageInterpeter(char[] language) {
        int number = 1;
        this.language = language;
        dictionary = new HashMap<>(language.length);
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
            res.add(index, language[i-1]);
            index++;
         }
         return res;
    }

    public List<Integer> lettersToNumbers(char[] letters)
    {
        List<Integer> res = new ArrayList<>(letters.length);
        int index = 0;
        for(Character letter : letters) {
            res.add(index, dictionary.get(letter));
            index++;
        }
        return res;
    }

    public List<Integer> getLanguageAsNumbers()
    {
        List<Integer> res = new ArrayList<>(dictionary.size());
        int index = 0;
        for(Character letter : dictionary.keySet()) {
            res.add(index, dictionary.get(letter));
            index++;
        }
        return res;
    }
}
