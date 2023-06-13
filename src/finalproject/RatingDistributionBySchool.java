package finalproject;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class RatingDistributionBySchool extends DataAnalyzer {

    private MyHashTable<String, MyPair<Integer, Double>> profSchoolStats;

    public RatingDistributionBySchool(Parser p) {
        super(p);
    }

    @Override
    public MyHashTable<String, Integer> getDistByKeyword(String keyword) {
        MyHashTable<String, Integer> result = new MyHashTable<>();
        String keywordLower = keyword.trim().toLowerCase();
        for (String profSchool : profSchoolStats.getKeySet()) {
            String[] tokens = profSchool.split(",");
            String prof = tokens[0];
            String school = tokens[1];
            if (school.equals(keywordLower)) {
                MyPair<Integer, Double> stats = profSchoolStats.get(profSchool);
                String avgRatingStr;
                if (stats.getValue() * 100 % 10 == 0) {
                    avgRatingStr = String.format("%.1f", stats.getValue());
                } else {
                    avgRatingStr = String.format("%.2f", stats.getValue());
                }
                result.put(prof + "\n" + avgRatingStr, stats.getKey());
            }
        }
        return result;
    }


    @Override
    public void extractInformation() {
        profSchoolStats = new MyHashTable<>();
        int nameIndex = parser.fields.get("professor_name");
        int qualityIndex = parser.fields.get("student_star");
        int schoolIndex = parser.fields.get("school_name");

        for (String[] entry : parser.data) {
            String prof = entry[nameIndex].trim().toLowerCase();
            String school = entry[schoolIndex].trim().toLowerCase();

            // Update professor-school stats
            String profSchool = prof + "," + school;
            MyPair<Integer, Double> profSchoolStatsPair = profSchoolStats.get(profSchool);
            if (profSchoolStatsPair == null) {
                profSchoolStatsPair = new MyPair<>(0, 0.0);
            }
            double rating = Double.parseDouble(entry[qualityIndex].trim());
            int numReviews = profSchoolStatsPair.getKey() + 1;
            double avgRating = ((numReviews - 1) * profSchoolStatsPair.getValue() + rating) / numReviews;
            profSchoolStats.put(profSchool, new MyPair<>(numReviews, avgRating));
        }
    }
}