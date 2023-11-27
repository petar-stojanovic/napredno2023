package src.kol_1.shopping;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class InvalidOperationException extends Exception {

    public InvalidOperationException(String message) {
        super(message);
    }
}

abstract class Product implements Comparable<Product> {

    private Integer id;
    private String name;

    private double price;

    public Product(Integer id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    abstract double getFullPrice();

    //WS;productID;productName;productPrice;quantity
    public static Product create(String line) throws InvalidOperationException {
        String[] parts = line.split(";");

        Integer productId = Integer.parseInt(parts[1]);
        String productName = parts[2];
        int productPrice = Integer.parseInt(parts[3]);
        double quantity = Double.parseDouble(parts[4]);
        if (quantity == 0) {
            throw new InvalidOperationException(String.format("The quantity of the product with id %s can not be 0.",
                              productId));
        }
        if (parts[0].equals("WS")) {
            return new WSProduct(productId, productName, productPrice, (int) quantity);
        }
        return new PSProduct(productId, productName, productPrice, quantity);

    }

    @Override
    public int compareTo(Product o) {
        return Double.compare(getFullPrice(), o.getFullPrice());
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f", id, getFullPrice());
    }
}

class WSProduct extends Product {
    private int quantity;

    public WSProduct(Integer id, String name, double price, int quantity) {
        super(id, name, price);
        this.quantity = quantity;
    }

    @Override
    double getFullPrice() {
        return getPrice() * quantity;
    }
}

class PSProduct extends Product {
    private double quantity;

    public PSProduct(Integer id, String name, double price, double quantity) {
        super(id, name, price);
        this.quantity = quantity;
    }

    @Override
    double getFullPrice() {
        return getPrice() * (quantity / 1000.0);
    }
}

class ShoppingCart {
    List<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    void addItem(String itemData) throws InvalidOperationException {
        products.add(Product.create(itemData));
    }

    public void printShoppingCart(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);

        products.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);

        pw.flush();
    }

    public void blackFridayOffer(List<Integer> discountItems, OutputStream out) throws InvalidOperationException {
        PrintWriter pw = new PrintWriter(out);

        List<Product> discountedList =
                          products.stream().filter(it -> discountItems.contains(it.getId())).collect(Collectors.toList());

        if (discountedList.isEmpty()) {
            throw new InvalidOperationException("There are no products with discount.");
        }

        discountedList.forEach(it -> {
            double priceBefore = it.getFullPrice();
            it.setPrice((it.getPrice() * 0.9f));
            double priceAfter = it.getFullPrice();
            pw.format("%s - %.2f\n", it.getId(), priceBefore - priceAfter);
        });

        pw.flush();
    }

}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}