package src.kol_2.zadnji_20;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student implements Comparable<Student> {
    String index;
    String name;
    int firstExamPoints;
    int secondExamPoints;
    int labPoints;

    public Student(String index, String name) {
        this.index = index;
        this.name = name;
        this.firstExamPoints = 0;
        this.secondExamPoints = 0;
        this.labPoints = 0;
    }

    public void setFirstExamPoints(int firstExamPoints) {
        this.firstExamPoints = firstExamPoints;
    }

    public void setSecondExamPoints(int secondExamPoints) {
        this.secondExamPoints = secondExamPoints;
    }

    public void setLabPoints(int labPoints) {
        this.labPoints = labPoints;
    }

    public double summaryPoints() {
        return firstExamPoints * 0.45 + secondExamPoints * 0.45 + labPoints;
    }

    public int getGrade() {
        if (summaryPoints() < 50) {
            return 5;
        } else if (summaryPoints() < 60) {
            return 6;
        } else if (summaryPoints() < 70) {
            return 7;
        } else if (summaryPoints() < 80) {
            return 8;
        } else if (summaryPoints() < 90) {
            return 9;
        }
        return 10;
    }

    public boolean hasPassed() {
        return getGrade() > 5;
    }
    public void updateActivity(String activity, int points) throws Exception {
        if (activity.equals("midterm1")) {
            setFirstExamPoints(points);
        } else if (activity.equals("midterm2")) {
            setSecondExamPoints(points);
        } else if (activity.equals("labs")) {
            setLabPoints(points);
        } else {
            throw new Exception();
        }
    }

    @Override
    public int compareTo(Student other) {
        return Double.compare(summaryPoints(), other.summaryPoints());
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d", index, name, firstExamPoints, secondExamPoints, labPoints, summaryPoints(), getGrade());
    }

}

class AdvancedProgrammingCourse {
    Map<String, Student> students;

    public AdvancedProgrammingCourse() {
        students = new HashMap<>();
    }

    public void addStudent(Student s) {
        students.putIfAbsent(s.index, s);
    }

    public void updateStudent(String idNumber, String activity, int points) {
        Student student = students.get(idNumber);

        try {
            student.updateActivity(activity, points);
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }


    }

    public List<Student> getFirstNStudents(int n) {
        return students.values().stream()
                      .sorted(Comparator.reverseOrder())
                      .limit(n)
                      .collect(Collectors.toList());
    }

    public Map<Integer, Integer> getGradeDistribution() {
        Map<Integer, Integer> studentGrades = students.values().stream()
                      .map(Student::getGrade)
                      .collect(Collectors.groupingBy(
                                    it -> it,
                                    TreeMap::new,
                                    Collectors.summingInt(it -> 1)
                      ));
        IntStream.range(5, 11).forEach(x -> studentGrades.putIfAbsent(x, 0));
        return studentGrades;
    }

    public void printStatistics() {
        DoubleSummaryStatistics dss = students.values().stream()
                      .filter(Student::hasPassed)
                      .mapToDouble(Student::summaryPoints).summaryStatistics();

        System.out.printf("Count: %d Min: %.2f Average: %.2f Max: %.2f",
                      dss.getCount(),
                      dss.getMin(),
                      dss.getAverage(),
                      dss.getMax());
    }
}

public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
