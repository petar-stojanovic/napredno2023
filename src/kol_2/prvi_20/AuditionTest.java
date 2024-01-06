package src.kol_2.prvi_20;

import java.util.*;

class Participant {
    String code;
    String name;
    int age;

    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }
}

class Audition {

    Map<String, Set<Participant>> participantsByCity;

    public Audition() {
        this.participantsByCity = new HashMap<>();
    }

    public void addParticipant(String city, String code, String name, int age) {
        Participant participant = new Participant(code, name, age);

        participantsByCity.putIfAbsent(city, new TreeSet<>(Comparator.comparing(Participant::getCode)));

        participantsByCity.get(city).add(participant);
    }

    public void listByCity(String city) {
        participantsByCity.get(city).stream().sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge).thenComparing(Participant::getCode))
                      .forEach(System.out::println);
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                              Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}