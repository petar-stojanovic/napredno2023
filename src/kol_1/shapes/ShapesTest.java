package src.kol_1.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}

enum Type {
    C, R
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

abstract class Form implements Scalable, Stackable {
    private String id;
    private Color color;

    public Form(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    abstract Type getType();
}

class CircleForm extends Form {
    private float radius;

    public CircleForm(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius = radius * scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    Type getType() {
        return Type.C;
    }
}

class RectangleForm extends Form {
    float width;
    float height;

    public RectangleForm(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        width = width * scaleFactor;
        height = height * scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    Type getType() {
        return Type.R;
    }
}

class Canvas {
    List<Form> forms;

    public Canvas() {
        forms = new ArrayList<>();
    }

    void add(String id, Color color, float radius) {
        addAtSpecificPlace(new CircleForm(id, color, radius));

    }

    void add(String id, Color color, float width, float height) {
        addAtSpecificPlace(new RectangleForm(id, color, width, height));
    }

    // 100 120 200 300 ---> 150
    private void addAtSpecificPlace(Form formToAdd) {

        int indexToAdd = 0;
        for (Form form : forms) {
            if (formToAdd.weight() > form.weight()) {
//                indexToAdd = forms.indexOf(form);
                break;
            }
            indexToAdd++;
        }
        forms.add(indexToAdd, formToAdd);
    }

    public void scale(String id, float scaleFactor) {
        Form form = forms.stream().filter(it -> it.getId().equals(id)).findFirst().get();
        forms.remove(form);
        form.scale(scaleFactor);
        addAtSpecificPlace(form);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        forms.forEach(it -> sb.append(String.format("%s: %-5s%-10s%10.2f\n", it.getType(), it.getId(), it.getColor(), it.weight())));
        return sb.toString();
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}
