package src.labs.lab7;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TermFrequencyTest {
    public static void main(String[] args) throws IOException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                      "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                      "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in,
                      stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde

class TermFrequency {

    private Map<String, Integer> wordCount = new HashMap<>();

    public TermFrequency(InputStream in, String[] stopWordss) throws IOException {
        Set<String> stopWords = new HashSet<>(Arrays.asList(stopWordss));
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = bf.readLine()) != null) {
            if (!line.isEmpty()) {
                String[] parts = line.split("\\s+");
                for (String part : parts) {
                    String normalizedWord = normalize(part);

                    if (stopWords.contains(normalizedWord) || normalizedWord.isEmpty()) {
                        continue;
                    }
                    wordCount.putIfAbsent(normalizedWord, 0);
                    wordCount.computeIfPresent(normalizedWord, (k, v) -> v + 1);

                }
            }
        }
    }

    private static String normalize(String word) {
        return word.toLowerCase().replace(",", "").replace(".", "").trim();
    }

    public int countTotal() {
        return wordCount.values().stream().mapToInt(i -> i).sum();
    }

    public int countDistinct() {
        return wordCount.keySet().size();
    }

    public List<String> mostOften(int i) {
        Comparator<Map.Entry<String,Integer>> comparator =
                      Comparator.comparing(Map.Entry<String,Integer>::getValue, Comparator.reverseOrder())
                                    .thenComparing(Map.Entry::getKey);


        return wordCount.entrySet().stream()
                      .sorted(comparator)
//                      .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
//                                    .thenComparing(Map.Entry::getKey))
                      .limit(i)
                      .map(Map.Entry::getKey)
                      .collect(Collectors.toList());
    }

}