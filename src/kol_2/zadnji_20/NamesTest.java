package src.kol_2.zadnji_20;

import java.util.*;
import java.util.stream.Collectors;

class Names {

    Map<String, Integer> namesCount;

    public Names() {
        this.namesCount = new TreeMap<>();
    }

    public void addName(String name) {
        namesCount.putIfAbsent(name, 0);
        namesCount.computeIfPresent(name, (k, v) -> v + 1);
    }

    public void printN(int n) {
        namesCount.entrySet()
                      .stream()
                      .filter(it -> it.getValue() >= n)
                      .forEach((it) -> System.out.printf("%s (%d) %d\n", it.getKey(), it.getValue(),
                                    numOfUniqueLetters(it.getKey())));
    }

    public String findName(int len, int x) {
        List<String> deletedNames = namesCount.entrySet()
                      .stream().filter(it -> it.getKey().length() < len)
                      .map(it -> it.getKey())
                      .collect(Collectors.toList());
        return deletedNames.get(x%deletedNames.size());
    }

    private int numOfUniqueLetters(String name) {
        Set<Character> characters = new HashSet<>();

        char[] nameCharacters = name.toLowerCase().toCharArray();

        for (char nameCharacter : nameCharacters) {
            characters.add(nameCharacter);
        }

        return characters.size();
    }
}

// 27
public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}