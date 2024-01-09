package src.kol_2.zadnji_20;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String id) {
        super(String.format("Product with id %s does not exist in the online shop!", id));
    }
}

class Product {
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double price;

    int quantitySold;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;

        quantitySold = 0;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getProfit() {
        return price * quantitySold;
    }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', createdAt=%s, price=%s, quantitySold=%d}", id,
                      name, createdAt, price, quantitySold);
    }

}

class OnlineShop {
    Map<String, Product> products;

    OnlineShop() {
        products = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        Product product = new Product(category, id, name, createdAt, price);
        products.put(id, product);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException {
        if (!products.containsKey(id)) {
            throw new ProductNotFoundException(id);
        }
        products.get(id).setQuantitySold(products.get(id).getQuantitySold() + quantity);
        return products.get(id).getProfit();
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {

        Comparator<Product> comparator = ProductComparatorFactory.createProductComparator(comparatorType);
        if (comparator == null) {
            throw new RuntimeException();
        }

        List<Product> allProducts = products.values().stream().sorted(comparator).collect(Collectors.toList());

        if (category == null) {
            return splitProductsByPageSize(allProducts, pageSize);
        }

        List<Product> sortedAndFilteredProducts =
                      allProducts.stream().filter(it -> it.category.equals(category)).collect(Collectors.toList());

        return splitProductsByPageSize(sortedAndFilteredProducts, pageSize);

    }

    private List<List<Product>> splitProductsByPageSize(List<Product> allProducts, int pageSize) {

        List<List<Product>> result = new ArrayList<>();

        for (int i = 0, pageIndex = 0; i < allProducts.size(); i += pageSize, pageIndex++) {
            int lastPage = pageIndex * pageSize;
            int currPage = (pageIndex + 1) * pageSize;

            if (currPage > allProducts.size()) {
                currPage = allProducts.size();
            }

            result.add(new ArrayList<>());
            result.get(pageIndex).addAll(
                          allProducts.subList(lastPage, currPage)
            );

        }

        return result;
    }

}

class ProductComparatorFactory {
    public static Comparator<Product> createProductComparator(COMPARATOR_TYPE comparatorType) {
        switch (comparatorType) {
            case NEWEST_FIRST:
                return Comparator.comparing(it -> it.createdAt, Comparator.reverseOrder());
            case OLDEST_FIRST:
                return Comparator.comparing(it -> it.createdAt);
            case LOWEST_PRICE_FIRST:
                return Comparator.comparing(it -> it.price);
            case HIGHEST_PRICE_FIRST:
                return Comparator.comparing(it -> it.price, Comparator.reverseOrder());
            case LEAST_SOLD_FIRST:
                return Comparator.comparing(it -> it.quantitySold);
            case MOST_SOLD_FIRST:
                return Comparator.comparing(it -> it.quantitySold, Comparator.reverseOrder());
            default:
                return null;
        }
    }

}

// 25
public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

