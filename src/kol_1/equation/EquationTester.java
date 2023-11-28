package src.kol_1.equation;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

class Equation<IN, OUT> {
    Supplier<IN> supplier;
    Function<IN, OUT> function;

    public Equation(Supplier<IN> supplier, Function<IN, OUT> function) {
        this.supplier = supplier;
        this.function = function;
    }

    public Optional<OUT> calculate() {
        return Optional.of(function.apply(supplier.get()));
    }
}

class EquationProcessor {
    public static <IN, OUT> void process(List<IN> input, List<Equation<IN, OUT>> equations) {
        input.forEach(it -> {
            System.out.printf("Input: %s\n", it);
        });

        equations.forEach(it -> {
            System.out.printf("Result: %s\n", it.calculate().get());
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
            equations1.add(new Equation<>(() -> inputs.get(2), integer -> integer + 1000));

            // TODO: Add an equation where you get the 4th integer from the inputs list, and the result is the maximum of that number and the number 100.
            equations1.add(new Equation<>(() -> inputs.get(3), integer -> Math.max(integer, 100)));

            EquationProcessor.process(inputs, equations1);

        } else { // Testing with Line, Integer
            List<Equation<Line, Double>> equations2 = new ArrayList<>();
            List<Line> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(Line.createLine(sc.nextLine()));
            }

            //TODO Add an equation where you get the 2nd line, and the result is the value of y in the line equation.
            equations2.add(new Equation<>(() -> inputs.get(1), line -> line.calculateLine()));

            //TODO Add an equation where you get the 1st line, and the result is the sum of all y values for all lines that have a greater y value than that equation.

            equations2.add(new Equation<>(() -> inputs.get(0), line -> {
                return inputs.stream().filter(it -> it.calculateLine() > line.calculateLine()).mapToDouble(it -> it.calculateLine()).sum();
            }));

            EquationProcessor.process(inputs, equations2);
        }
    }
}
