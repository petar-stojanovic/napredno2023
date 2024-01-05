package src.labs.lab7;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        // Vasiod kod ovde
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));

        Map<String, Set<String>> anagrams = new LinkedHashMap<>();
        bf.lines().forEach(it -> {
            String sortedWord = sortWord(it);
            anagrams.putIfAbsent(sortedWord, new TreeSet<>());
            anagrams.get(sortedWord).add(it);
        });

        StringBuilder sb = new StringBuilder();

        anagrams.values().forEach(it -> {
            it.forEach(val -> sb.append(val).append(" "));
            sb.append("\n");
        });

        System.out.println(sb.toString());
    }


    private static String sortWord(String word) {
        char[] x = word.toCharArray();
        Arrays.sort(x);
        return new String(x);
    }
}
