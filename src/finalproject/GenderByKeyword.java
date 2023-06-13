package finalproject;

import java.util.ArrayList;
import java.util.LinkedList;

public class GenderByKeyword extends DataAnalyzer {
    private MyHashTable<String, MyHashTable<String, Integer>> genderByKeywordTable;

    public GenderByKeyword(Parser p) {
        super(p);
    }

    public MyHashTable<String, Integer> getDistByKeyword(String keyword) {
        return genderByKeywordTable.get(keyword.toLowerCase().trim());
    }

    public void extractInformation() {
        genderByKeywordTable = new MyHashTable<>();

        for (String[] cur : this.parser.data) {
            String[] comment = cur[this.parser.fields.get("comments")].toLowerCase().trim().replaceAll("[^a-z']", " ").split("\\s+");
            String gender = cur[this.parser.fields.get("gender")].trim();
            MyHashTable<String, Boolean> truthTable = new MyHashTable<>();

            for (String word : comment) {
                if (truthTable.get(word) == null) {
                    if (genderByKeywordTable.get(word) == null) {
                        MyHashTable<String, Integer> genderCount = new MyHashTable<>();

                        genderCount.put("M", 0);
                        genderCount.put("F", 0);
                        genderCount.put("X", 0);

                        genderByKeywordTable.put(word, genderCount);
                    }
                    truthTable.put(word, true);
                    genderByKeywordTable.get(word).put(gender, genderByKeywordTable.get(word).get(gender) +1);
                }
            }

        }
    }
}

