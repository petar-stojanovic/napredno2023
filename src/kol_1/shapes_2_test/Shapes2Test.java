package src.kol_1.shapes_2_test;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String id, double maxArea) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", id, maxArea));
    }
}

enum Type {
    CIRCLE, SQUARE
}

abstract class Shape {
    double size;

    public Shape(double size) {
        this.size = size;
    }

    abstract double getSize();

    abstract Type getType();

}

class Circle extends Shape {

    public Circle(double size) {
        super(size);
    }

    @Override
    double getSize() {
        return size * size * Math.PI;
    }

    @Override
    Type getType() {
        return Type.CIRCLE;
    }
}

class Square extends Shape {

    public Square(double size) {
        super(size);
    }

    @Override
    double getSize() {
        return size * size;
    }

    @Override
    Type getType() {
        return Type.SQUARE;
    }
}

class Canvas implements Comparable<Canvas> {
    String id;
    List<Shape> shapes;

    public Canvas(String id, List<Shape> shapes) {
        this.id = id;
        this.shapes = shapes;
    }

    public static Canvas create(String line, double maxArea) throws IrregularCanvasException {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Shape> shapeList = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String type = parts[i++];
            double size = Double.parseDouble(parts[i]);
            Shape shape;
            if (type.charAt(0) == 'C') {
                shape = new Circle(size);
            } else {
                shape = new Square(size);
            }
            if (shape.getSize() > maxArea) {
                throw new IrregularCanvasException(id, maxArea);
            }
            shapeList.add(shape);
        }

        return new Canvas(id, shapeList);
    }

    public double getSize() {
        return shapes.stream().mapToDouble(Shape::getSize).sum();
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics dss = shapes.stream().mapToDouble(Shape::getSize).summaryStatistics();
        int totalCircles = (int) shapes.stream().filter(it -> it.getType().equals(Type.CIRCLE)).count();
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                      id,
                      shapes.size(),
                      totalCircles,
                      shapes.size() - totalCircles,
                      dss.getMin(),
                      dss.getMax(),
                      dss.getAverage());
    }

    @Override
    public int compareTo(Canvas o) {
//        Comparator<Canvas> com = Comparator.comparing(Canvas::getSize).thenComparing(Canvas::getSize);

        return Comparator.comparing(Canvas::getSize).thenComparing(Canvas::getSize).compare(this, o);
    }
}

class ShapesApplication {
    double maxArea;
    List<Canvas> canvas;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        this.canvas = new ArrayList<>();
    }

    public void readCanvases(InputStream in) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        canvas = bf.lines().map(it -> {
                          try {
                              return Canvas.create(it, maxArea);
                          } catch (IrregularCanvasException e) {
                              System.out.println(e.getMessage());
                              return null;
                          }
                      })
                      .filter(Objects::nonNull)
                      .collect(Collectors.toList());
        bf.close();
    }

    public void printCanvases(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
//        Comparator byRanking =
//                      (Player player1, Player player2) -> Integer.compare(player1.getRanking(), player2.getRanking());

        canvas.stream().sorted((l, r) -> Double.compare(l.getSize(), r.getSize())).forEach(pw::println);
        pw.flush();
    }
}

public class Shapes2Test {

    public static void main(String[] args) throws IOException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);

    }
}