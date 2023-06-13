package finalproject;

import java.util.ArrayList;


public class RatingByKeyword extends DataAnalyzer {
    private MyHashTable<String, int[]> keywordStats;

    public RatingByKeyword(Parser p) {
        super(p);
    }
    @Override
    public MyHashTable<String, Integer> getDistByKeyword(String keyword) {

        MyHashTable<String, Integer> result = new MyHashTable<>();
        String lowercaseKeyword = keyword.trim().toLowerCase();

        int[] ratingDistribution = null;
        if (keywordStats != null) {
            ratingDistribution = keywordStats.get(lowercaseKeyword);
        }
        if (ratingDistribution == null) {
            return result;
        }

        for (int i = 0; i < ratingDistribution.length; i++) {
            result.put(Integer.toString(i + 1), ratingDistribution[i]);
        }
        return result;
    }

    @Override
    public void extractInformation() {

        keywordStats = new MyHashTable<>();

        int commentIndex = parser.fields.get("comments");
        int qualityIndex = parser.fields.get("student_star");
        for (String[] entry : parser.data) {
            String comment = entry[commentIndex].trim().toLowerCase().replaceAll("[^a-zA-Z']", " ");
            String[] words = comment.split("\\s+");

            // create an array to keep track of unique words
            String[] uniqueWords = new String[words.length];
            int numUniqueWords = 0;

            // iterate over each word and check if it's already in the uniqueWords array
            for (String word : words) {
                if (!word.isEmpty()) {
                    boolean alreadyAdded = false;
                    for (int i = 0; i < numUniqueWords; i++) {
                        if (uniqueWords[i].equals(word)) {
                            alreadyAdded = true;
                            break;
                        }
                    }
                    if (!alreadyAdded) {
                        uniqueWords[numUniqueWords] = word;
                        numUniqueWords++;
                    }
                }
            }

            double rating = Double.parseDouble(entry[qualityIndex].trim());
            for (int i = 0; i < numUniqueWords; i++) {
                String word = uniqueWords[i];
                int[] ratingDistribution = keywordStats.get(word);
                if (ratingDistribution == null) {
                    ratingDistribution = new int[5];
                    keywordStats.put(word, ratingDistribution);
                }

                int index = (int) rating - 1;
                if (index >= 0 && index < 5) {
                    ratingDistribution[index]++;
                }
            }
        }
    }
}
