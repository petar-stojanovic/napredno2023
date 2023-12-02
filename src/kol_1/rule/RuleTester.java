package src.kol_1.rule;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Student {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public static Student create(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        return new Student(id, grades);
    }

    @Override
    public String toString() {
        return "Student{" +
                      "id='" + id + '\'' +
                      ", grades=" + grades +
                      '}';
    }
}

public class RuleTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Test for String,Integer
            List<Rule<String, Integer>> rules = new ArrayList<>();

            /*
            TODO: Add a rule where if the string contains the string "NP", the result would be index of the first occurrence of the string "NP"
            * */

            Rule<String, Integer> rule1 = new Rule<>(it -> it.contains("NP"), it -> it.indexOf("NP"));

            /*
            TODO: Add a rule where if the string starts with the string "NP", the result would be length of the string
            * */

            Rule<String, Integer> rule2 = new Rule<>(it -> it.startsWith("NP"), it -> it.length());

            rules.add(rule1);
            rules.add(rule2);

            List<String> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(sc.nextLine());
            }

            RuleProcessor.process(inputs, rules);

        } else { //Test for Student, Double
            List<Rule<Student, Double>> rules = new ArrayList<>();

            //TODO Add a rule where if the student has at least 3 grades, the result would be the max grade of the student

            Rule<Student, Double> rule1 = new Rule<>(
                          (it -> it.grades.size() >= 3),
                          it -> it.grades.stream().max((l, r) -> Integer.compare(l.intValue(), r.intValue())).get().doubleValue());

            //TODO Add a rule where if the student has an ID that starts with 20, the result would be the average grade of the student
            //If the student doesn't have any grades, the average is 5.0

            Rule<Student, Double> rule2 = new Rule<>(
                          (it -> it.id.startsWith("20")),
                          it -> it.grades.stream().mapToDouble(grade -> grade).average().orElse(5.0));

            rules.add(rule1);
            rules.add(rule2);

            List<Student> students = new ArrayList<>();
            while (sc.hasNext()) {
                students.add(Student.create(sc.nextLine()));
            }

            RuleProcessor.process(students, rules);
        }
    }
}

class RuleProcessor {
    static <T, U> void process(List<T> input, List<Rule<T, U>> rules) {
        input.forEach(it -> {
            System.out.println(String.format("Input: %s", it));

            rules.forEach(rule -> {
                Optional<U> optionalRule = rule.apply(it);

                if(optionalRule.isPresent()){
                    System.out.println(String.format("Result: %s", optionalRule.get()));
                }else {
                    System.out.println("Condition not met");
                }

            });

        });
    }
}

class Rule<T, U> {
    Predicate<T> predicate;
    Function<T, U> function;

    public Rule(Predicate<T> predicate, Function<T, U> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public Optional<U> apply(T input) {
        if (predicate.test(input)) {
            return Optional.of(function.apply(input));
        }

        return Optional.empty();
    }
}