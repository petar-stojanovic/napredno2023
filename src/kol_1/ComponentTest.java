package src.kol_1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Component implements Comparable<Component> {
    String color;
    int weight;
    List<Component> components;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        components = new ArrayList<>();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    void addComponent(Component component) {
        this.components.add(component);
        Collections.sort(components);
    }

    @Override
    public int compareTo(Component other) {
        return Comparator.comparing(Component::getWeight).thenComparing(Component::getColor).compare(this, other);
    }
}

class Window {
    private String name;
    private List<Component> components;

    public Window(String name) {
        this.name = name;
        this.components = new ArrayList<>();
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        Component x = null;
        try {
            x = components.get(position);
        } catch (IndexOutOfBoundsException e) {
            components.add(position, component);
        }
        if (x != null) {
            throw new InvalidPositionException(position);
        }

    }

    void changeColor(int weight, String color) {
        components.stream()
                .filter(it -> it.getWeight() < weight)
                .forEach(it -> it.setColor(color));
    }

    void swichComponents(int pos1, int pos2) {
        Component comp1 = components.get(pos1);
        Component comp2 = components.get(pos2);
        components.remove(comp1);
        components.remove(comp2);
        components.add(pos1, comp2);
        components.add(pos2, comp1);
    }

    @Override
    public String toString() {
        return "Window{" +
                "name='" + name + '\'' +
                ", components=" + components +
                '}';
    }
}

class InvalidPositionException extends Exception {

    public InvalidPositionException(int pos) {
        super(String.format("Invalid position %d, already taken!", pos));
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if (what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде