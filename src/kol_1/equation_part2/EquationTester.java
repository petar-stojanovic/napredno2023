package src.kol_1.equation_part2;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

class Equation<T, U> {
    Supplier<T> supplier;
    Function<T, U> function;

    public Equation(Supplier<T> supplier, Function<T, U> function) {
        this.supplier = supplier;
        this.function = function;
    }

    public Optional<U> calculate() {
        return Optional.of(function.apply(supplier.get()));
    }
}

class EquationProcessor {
    public static <T, U> void process(List<T> input, List<Equation<T, U>> equations) {
        input.forEach(it -> {
            System.out.printf("Input: %s\n", it);
        });
        equations.forEach(it -> {
            System.out.println("Result: " + it.calculate().get());
        });
    }

}

class Line {
    Double coeficient;
    Double x;
    Double intercept;

    public Line(Double coeficient, Double x, Double intercept) {
        this.coeficient = coeficient;
        this.x = x;
        this.intercept = intercept;
    }

    public static Line createLine(String line) {
        String[] parts = line.split("\\s+");
        return new Line(
                      Double.parseDouble(parts[0]),
                      Double.parseDouble(parts[1]),
                      Double.parseDouble(parts[2])
        );
    }

    public double calculateLine() {
        return coeficient * x + intercept;
    }

    @Override
    public String toString() {
        return String.format("%.2f * %.2f + %.2f", coeficient, x, intercept);
    }
}

public class EquationTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { // Testing with Integer, Integer
            List<Equation<Integer, Integer>> equations1 = new ArrayList<>();
            List<Integer> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(Integer.parseInt(sc.nextLine()));
            }

            // TODO: Add an equation where you get the 3rd integer from the inputs list, and the result is the sum of that number and the number 1000.
            Equation<Integer, Integer> first = new Equation<>(() -> inputs.get(2), it -> it + 1000);

            // TODO: Add an equation where you get the 4th integer from the inputs list, and the result is the maximum of that number and the number 100.
            Equation<Integer, Integer> second = new Equation<>(() -> inputs.get(3), it -> Math.max(it, 100));

            equations1.add(first);
            equations1.add(second);

            EquationProcessor.process(inputs, equations1);

        } else { // Testing with Line, Integer
            List<Equation<Line, Double>> equations2 = new ArrayList<>();
            List<Line> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(Line.createLine(sc.nextLine()));
            }

            //TODO Add an equation where you get the 2nd line, and the result is the value of y in the line equation.
            Equation<Line, Double> first = new Equation<>(() -> inputs.get(1), it -> it.calculateLine());

            //TODO Add an equation where you get the 1st line, and the result is the sum of all y values for all lines that have a greater y value than that equation.
            Line line = inputs.get(0);
            Equation<Line, Double> second = new Equation<>(() -> line, it ->
                          inputs.stream().filter(input -> input.calculateLine() > line.calculateLine())
                                        .mapToDouble(input -> input.calculateLine()).sum());

            equations2.add(first);
            equations2.add(second);

            EquationProcessor.process(inputs, equations2);
        }
    }
}
