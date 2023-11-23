package src.kol_1;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class Shape implements Comparable<Shape> {
    private String name;
    List<Integer> numbers;

    public Shape(String line) {
        this.name = "";
        this.numbers = new ArrayList<>();
    }

    public Shape(String name, List<Integer> numbers) {
        this.name = name;
        this.numbers = List.copyOf(numbers);
    }

    public static Shape create(String line) {
        String[] parts = line.split("\\s+");
        String name = parts[0];
        List<Integer> numbersList = new ArrayList<>();

        for ( int i = 1; i < parts.length; i++) {
            numbersList.add(Integer.parseInt(parts[i]));
        }

        return new Shape(name, numbersList);
    }

    public String getName() {
        return name;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public int getSumOfNumbers() {
        return numbers.stream().reduce(0, (left, right) -> left + right) * 4;
    }

    @Override
    public int compareTo(Shape other) {
        return Integer.compare(getSumOfNumbers(), other.getSumOfNumbers());
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", name, getNumbers().size(), getSumOfNumbers());
    }
}

class ShapesApplication {

    List<Shape> shapeList;

    public ShapesApplication() {
        shapeList = new ArrayList<>();
    }

    public int readCanvases(InputStream inputStream) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));

        shapeList = bf.lines()
                .map(Shape::create)
                .collect(Collectors.toList());

        bf.close();

        return shapeList.stream()
                .mapToInt(x -> x.getNumbers().size())
                .sum();
    }

    public void printLargestCanvasTo(OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(outputStream);

        shapeList.stream()
                .max(Comparator.naturalOrder())
                .ifPresent(printWriter::println);

        printWriter.flush();
    }
}

public class Shapes1Test {

    public static void main(String[] args) throws IOException {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}