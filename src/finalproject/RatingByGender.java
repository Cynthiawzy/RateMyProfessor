package finalproject;

import java.util.ArrayList;

public class RatingByGender extends DataAnalyzer {
    private MyHashTable<String, MyHashTable<String, MyHashTable<String, Integer>>> genderStats;

    public RatingByGender(Parser p) {
        super(p);
    }

    @Override
    public MyHashTable<String, Integer> getDistByKeyword(String keyword) {

        MyHashTable<String, Integer> result = new MyHashTable<>();
        String[] inputs = keyword.split(",");
        String gender = inputs[0].trim().toUpperCase();
        String ratingType = inputs[1].trim().toLowerCase();

        if (genderStats == null) {
            extractInformation();
        }

        MyHashTable<String, Integer> distribution = genderStats.get(gender).get(ratingType);
        for (String rating : distribution.getKeySet()) {
            result.put(rating, distribution.get(rating));
        }
        return result;
    }



    @Override
    public void extractInformation() {

        genderStats = new MyHashTable<>();
        int genderIndex = parser.fields.get("gender");
        int qualityIndex = parser.fields.get("student_star");
        int difficultyIndex = parser.fields.get("student_difficult");

        for (String[] entry : parser.data) {
            String gender = entry[genderIndex].trim().toUpperCase();
            if (!gender.equals("F") && !gender.equals("M")) {
                continue;
            }

            double qualityRating = Double.parseDouble(entry[qualityIndex].trim());
            double difficultyRating = Double.parseDouble(entry[difficultyIndex].trim());

            MyHashTable<String, MyHashTable<String, Integer>> ratingStats = genderStats.get(gender);
            if (ratingStats == null) {
                ratingStats = new MyHashTable<>();
                genderStats.put(gender, ratingStats);
            }
            updateDistribution(ratingStats, "quality", qualityRating);
            updateDistribution(ratingStats, "difficulty", difficultyRating);
        }
    }
    private void updateDistribution(MyHashTable<String, MyHashTable<String, Integer>> ratingStats, String ratingType, double ratingValue) {
        String rating = String.valueOf((int) ratingValue);
        MyHashTable<String, Integer> distribution = ratingStats.get(ratingType);
        if (distribution == null) {
            distribution = new MyHashTable<>();
            ratingStats.put(ratingType, distribution);
        }
        Integer count = distribution.get(rating);
        if (count == null) {
            distribution.put(rating, 1);
        } else {
            distribution.put(rating, count + 1);
        }
    }
}