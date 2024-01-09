package src.kol_2.zadnji_20;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    String index;
    List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public String getIndex() {
        return index;
    }

    public double summaryPoints() {
        return (double) points.stream().mapToInt(it -> it).sum() / 10;
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, hasFailed() ? "NO" : "YES", summaryPoints());
    }

    public Boolean hasFailed() {
        return points.size() < 8;
    }

    public int getYearOfStudy() {
        return 20 - Integer.parseInt(index.substring(0, 2));
    }

}

class LabExercises {
    List<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {

        Comparator<Student> comparator = Comparator.comparing(Student::summaryPoints).thenComparing(Student::getIndex);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        students.stream().sorted(comparator).limit(n).forEach(System.out::println);
    }

    public List<Student> failedStudents() {
        return students.stream()
                      .filter(Student::hasFailed)
                      .sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::summaryPoints))
                      .collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        return students.stream()
                      .filter(it -> !it.hasFailed())
                      .collect(Collectors.groupingBy(
                                    Student::getYearOfStudy,
                                    Collectors.averagingDouble(Student::summaryPoints)
                      ));
    }
}

//23
public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                          .mapToInt(Integer::parseInt)
                          .boxed()
                          .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                      .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                      .forEach(System.out::println);

    }
}