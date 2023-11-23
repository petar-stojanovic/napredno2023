package src.labs.lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException() {
        super("InvalidExtraTypeException");
    }
}

class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException() {
        super("InvalidPizzaTypeException");
    }
}

class EmptyOrder extends Exception {
    public EmptyOrder() {
        super("EmptyOrder");
    }
}

class ItemOutOfStockException extends Exception {
    public ItemOutOfStockException(Item item) {

    }
}

enum ExtraType {
    Coke(5), Ketchup(3);

    private int price;

    ExtraType(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

enum PizzaType {
    Standard(5), Pepperoni(12), Vegetarian(8);

    private int price;

    PizzaType(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

interface Item {
    int getPrice();

}

class ExtraItem implements Item {
    private ExtraType type;

    ExtraItem(String type) throws InvalidExtraTypeException {
        try {
            this.type = ExtraType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidExtraTypeException();
        }
    }

    @Override
    public int getPrice() {
        return type.getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtraItem)) return false;
        return type == ((ExtraItem) o).type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

class PizzaItem implements Item {

    private PizzaType type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        try {
            this.type = PizzaType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidPizzaTypeException();
        }
    }

    @Override
    public int getPrice() {
        return type.getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PizzaItem)) return false;
        return type == ((PizzaItem) o).type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

class OrderLockedException extends Exception {
    OrderLockedException() {
        super("OrderLockedException");
    }
}

class Order {
    private List<Item> itemList;
    private boolean isLocked;

    public Order() {
        itemList = new ArrayList<>();
        this.isLocked = false;
    }

    void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (this.isLocked) {
            throw new OrderLockedException();
        }
        if (count > 10) {
            throw new ItemOutOfStockException(item);
        }
        if (itemList.contains(item)) {
            List<Item> itemsToRemove = new ArrayList<>();
            itemsToRemove = itemList.stream().filter(i -> i.equals(item)).collect(Collectors.toList());
            itemList.removeAll(itemsToRemove);
        }

        for (int i = 0; i < count; i++) {
            itemList.add(item);
        }
    }

    int getPrice() {
        return itemList.stream().mapToInt(it -> it.getPrice()).sum();
    }

    public void displayOrder() {

        System.out.printf("Total:%20s %n", getPrice());
    }

    public void removeItem(int idx) throws OrderLockedException {
        if (this.isLocked) {
            throw new OrderLockedException();
        }
        Item itemToRemove = itemList.get(idx);
        if (itemToRemove == null) {
            throw new ArrayIndexOutOfBoundsException(idx);
        }

        itemList.remove(idx);
    }

    public void lock() throws EmptyOrder {
        if (itemList.isEmpty()) {
            throw new EmptyOrder();
        }
        this.isLocked = true;
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}