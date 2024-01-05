//package src.kol_1.component_trial;
//
//import java.util.*;
//
//class InvalidPositionException extends Exception {
//    public InvalidPositionException(int position) {
//        super(String.format("Invalid position %d, alredy taken!", position));
//    }
//}
//
//class Component implements Comparable<Component> {
//    private String color;
//    private int weight;
//    private Set<Component> components;
//
//    public Component(String color, int weight) {
//        this.color = color;
//        this.weight = weight;
//        components = new TreeSet<>();
//    }
//
//    public String getColor() {
//        return color;
//    }
//
//    public int getWeight() {
//        return weight;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//    public Set<Component> getComponents() {
//        return components;
//    }
//
//    void addComponent(Component component) {
//        this.components.add(component);
//    }
//
//    @Override
//    public int compareTo(Component o) {
//        Comparator<Component> comparator =
//                      Comparator.comparing(Component::getWeight).thenComparing(Component::getColor);
//        return comparator.compare(this, o);
//    }
//
//    public static void addTextToPrint(StringBuilder sb, Component c, int numOfLines) {
//        sb.append("---".repeat(numOfLines));
//        sb.append(String.format("%d:%s\n", c.getWeight(), c.getColor()));
//
//        for (Component component : c.components) {
//            Component.addTextToPrint(sb, component, numOfLines + 1);
//        }
//    }
//
//    @Override
//    public String toString() {
//        return String.format("---%d:%s", weight, color);
//    }
//
//    public void changeColor(int weight, String color) {
//        if (this.weight < weight) {
//            setColor(color);
//        }
//        for (Component c : components) {
//            c.changeColor(weight, color);
//        }
//    }
//}
//
//class Window {
//    String name;
//    Map<Integer, Component> components;
//
//    public Window(String name) {
//        this.name = name;
//        this.components = new TreeMap<>();
//    }
//
//    void addComponent(int position, Component component) throws InvalidPositionException {
//        if (components.containsKey(position)) {
//            throw new InvalidPositionException(position);
//        }
//
//        components.put(position, component);
//    }
//
//    void changeColor(int weight, String color) {
//
//        for (Component c : components.values()) {
//            c.changeColor(weight, color);
//        }
//    }
//
//    void swichComponents(int pos1, int pos2) {
//        Component temp = components.get(pos1);
//        components.put(pos1, components.get(pos2));
//        components.put(pos2, temp);
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append(String.format("WINDOW %s\n", name));
//
//        for (Map.Entry<Integer, Component> x : components.entrySet()) {
//            sb.append(String.format("%d:", x.getKey()));
//            Component.addTextToPrint(sb, x.getValue(), 0);
//        }
//
//        return sb.toString();
//    }
//}
//
//public class ComponentTest {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String name = scanner.nextLine();
//        Window window = new Window(name);
//        Component prev = null;
//        while (true) {
//            try {
//                int what = scanner.nextInt();
//                scanner.nextLine();
//                if (what == 0) {
//                    int position = scanner.nextInt();
//                    window.addComponent(position, prev);
//                } else if (what == 1) {
//                    String color = scanner.nextLine();
//                    int weight = scanner.nextInt();
//                    Component component = new Component(color, weight);
//                    prev = component;
//                } else if (what == 2) {
//                    String color = scanner.nextLine();
//                    int weight = scanner.nextInt();
//                    Component component = new Component(color, weight);
//                    prev.addComponent(component);
//                    prev = component;
//                } else if (what == 3) {
//                    String color = scanner.nextLine();
//                    int weight = scanner.nextInt();
//                    Component component = new Component(color, weight);
//                    prev.addComponent(component);
//                } else if (what == 4) {
//                    break;
//                }
//
//            } catch (InvalidPositionException e) {
//                System.out.println(e.getMessage());
//            }
//            scanner.nextLine();
//        }
//
//        System.out.println("=== ORIGINAL WINDOW ===");
//        System.out.println(window);
//        int weight = scanner.nextInt();
//        scanner.nextLine();
//        String color = scanner.nextLine();
//        window.changeColor(weight, color);
//        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
//        System.out.println(window);
//        int pos1 = scanner.nextInt();
//        int pos2 = scanner.nextInt();
//        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
//        window.swichComponents(pos1, pos2);
//        System.out.println(window);
//    }
//}
//
//// вашиот код овде