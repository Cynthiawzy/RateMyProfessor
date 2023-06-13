package finalproject;

public class RatingDistributionByProf extends DataAnalyzer {
    private MyHashTable<String, MyHashTable<String, Integer>> profStats;

    public RatingDistributionByProf(Parser p) {
        super(p);
    }

    @Override
    public MyHashTable<String, Integer> getDistByKeyword(String keyword) {
        MyHashTable<String, Integer> result = new MyHashTable<String, Integer>();
        MyHashTable<String, Integer> stats = profStats.get(keyword.trim().toLowerCase());
        if (stats == null) {
            return result;
        }
        for (String rating : stats.getKeySet()) {
            Integer count = stats.get(rating);
            if (count != null) {
                result.put(rating, count);
            }
        }
        return result;
    }

    @Override
    public void extractInformation() {
        profStats = new MyHashTable<String, MyHashTable<String, Integer>>();
        int nameIndex = parser.fields.get("professor_name");
        int qualityIndex = parser.fields.get("student_star");
        for (String[] entry : parser.data) {
            String prof = entry[nameIndex].trim().toLowerCase();
            MyHashTable<String, Integer> ratingTable = profStats.get(prof);
            if (ratingTable == null) {
                ratingTable = new MyHashTable<String, Integer>();
                ratingTable.put("1", 0);
                ratingTable.put("2", 0);
                ratingTable.put("3", 0);
                ratingTable.put("4", 0);
                ratingTable.put("5", 0);
                profStats.put(prof, ratingTable);
            }
            double rating = Double.parseDouble(entry[qualityIndex].trim());
            String ratingKey = String.valueOf((int) rating);
            Integer count = ratingTable.get(ratingKey);
            if (count == null) {
                count = 0;
            }
            ratingTable.put(ratingKey, count + 1);
        }
    }
}
