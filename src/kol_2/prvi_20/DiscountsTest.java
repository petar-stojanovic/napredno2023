package src.kol_2.prvi_20;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde

class Store {

    String name;
    List<Product> products;

    public Store(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public static Store createStore(String input) {
        String[] parts = input.split("\\s+");

        List<Product> productList = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            int discountedPrice = Integer.parseInt(parts[i].split(":")[0]);
            int realPrice = Integer.parseInt(parts[i].split(":")[1]);
            productList.add(new Product(discountedPrice, realPrice));
        }
        return new Store(parts[0], productList);
    }

    @Override
    public String toString() {
        String productsString = products.stream()
                      .sorted(
                                    Comparator.comparing(Product::calculateProductPercentage, Comparator.reverseOrder())
                                                  .thenComparing(Product::calculateTotalDifference,Comparator.reverseOrder())
                      )
                      .map(Product::toString).collect(Collectors.joining("\n"));
        return String.format("%s\nAverage discount: %.1f%%\nTotal discount: %d\n%s", name, averageDiscount(),
                      totalDiscount(), productsString);
    }

    public int totalDiscount() {
        return products.stream().mapToInt(Product::calculateTotalDifference).sum();
    }

    public double averageDiscount() {
        return products.stream().mapToInt(Product::calculateProductPercentage).average().orElse(0);
    }

    public String getName() {
        return name;
    }
}

class Product {
    int discountedPrice;
    int realPrice;

    public Product(int discountedPrice, int realPrice) {
        this.discountedPrice = discountedPrice;
        this.realPrice = realPrice;
    }

    public int calculateProductPercentage() {
        return (int) ((1 - ((float) discountedPrice / realPrice)) * 100);
    }

    public int calculateTotalDifference() {
        return realPrice - discountedPrice;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", calculateProductPercentage(), discountedPrice, realPrice);
    }

}

class Discounts {

    List<Store> stores;

    public Discounts() {
        this.stores = new ArrayList<>();
    }

    //[ime] [cena_na_popust1:cena1] [cena_na_popust2:cena2]
    public int readStores(InputStream in) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        stores = bf.lines().map(Store::createStore).collect(Collectors.toList());

        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        return stores.stream()
                      .sorted(Comparator.comparing(Store::averageDiscount).reversed().thenComparing(Store::getName
                      )).limit(3).collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores.stream()
                      .sorted(Comparator.comparing(Store::totalDiscount).thenComparing(Store::getName
                      )).limit(3).collect(Collectors.toList());
    }
}