package src.kol_2.zadnji_20;

import java.util.*;
import java.util.stream.Collectors;

/*
YOUR CODE HERE
DO NOT MODIFY THE interfaces and classes below!!!
*/
class User {
    String id;
    String name;

    List<Float> orders;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        orders = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public float totalMoneySpent() {
        return (float) orders.stream().mapToDouble(it -> it).sum();
    }

    public double averageMoneySpent() {
        return orders.stream().mapToDouble(it -> it).average().orElse(0);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                      id,
                      name,
                      orders.size(),
                      totalMoneySpent(),
                      averageMoneySpent());
    }
}

class DeliveryPerson extends User implements Comparable<DeliveryPerson> {
    Location location;
    List<Float> orders;

    public DeliveryPerson(String id, String name, Location location) {
        super(id, name);
        this.location = location;
        orders = new ArrayList<>();
    }

    public void addDelivery(float cost) {
        orders.add(cost);
    }

    public float totalMoneyEarned() {
        return (float) orders.stream().mapToDouble(it -> it).sum();
    }

    public double averageMoneyEarned() {
        return orders.stream().mapToDouble(it -> it).average().orElse(0);
    }

    @Override
    public int compareTo(DeliveryPerson o) {
        return location.distance(o.location);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                      id,
                      name,
                      orders.size(),
                      totalMoneyEarned(),
                      averageMoneyEarned());
    }
}

class Restaurant {
    String id;
    String name;
    Location location;

    List<Float> orders;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        orders = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Double findAverageOrderPrice() {
        return orders.stream().mapToDouble(it -> it).average().orElse(0);
    }

    public float totalMoneyEarned() {
        return (float) orders.stream().mapToDouble(it -> it).sum();
    }

    public double averageMoneyEarned() {
        return orders.stream().mapToDouble(it -> it).average().orElse(0);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                      id,
                      name,
                      orders.size(),
                      totalMoneyEarned(),
                      averageMoneyEarned());
    }

}

class Address {
    String name;
    Location location;

    public Address(String name, Location location) {
        this.name = name;
        this.location = location;
    }
}

class DeliveryApp {
    String name;
    Set<DeliveryPerson> deliveryPersons;
    Map<String, Restaurant> restaurants;
    Map<String, User> users;
    Map<User, List<Address>> userAddresses;

    public DeliveryApp(String name) {
        this.name = name;
        deliveryPersons = new TreeSet<>();
        restaurants = new TreeMap<>();
        users = new TreeMap<>();
        userAddresses = new HashMap<>();
    }

    public void registerDeliveryPerson(String id, String name, Location currentLocation) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(id, name, currentLocation);
        deliveryPersons.add(deliveryPerson);
    }

    public void addRestaurant(String id, String name, Location location) {
        Restaurant restaurant = new Restaurant(id, name, location);
        restaurants.put(id, restaurant);
    }

    public void addUser(String id, String name) {
        User user = new User(id, name);
        users.put(id, user);
    }

    public void addAddress(String id, String addressName, Location location) {
        User user = findUserById(id);
        userAddresses.putIfAbsent(user, new ArrayList<>());

        Address address = new Address(addressName, location);
        userAddresses.get(user).add(address);
    }

    private User findUserById(String id) {
        return users.get(id);
    }

    private Address findUserAddress(User user, String addressName) {
        return userAddresses.get(user).stream().filter(it -> it.name.equals(addressName)).collect(Collectors.toList()).get(0);
    }

    public void orderFood(String userId, String userAddressName, String restaurantId, float cost) {
        User user = findUserById(userId);
        user.orders.add(cost);

        Address address = findUserAddress(user, userAddressName);

        Restaurant restaurant = restaurants.get(restaurantId);
        restaurant.orders.add(cost);

        DeliveryPerson deliveryPerson = findDeliveryPersonClosestToRestaurant(restaurant);
        deliveryPerson.addDelivery((float) (90 + deliveryPerson.location.distance(restaurant.location) / 10 * 10));
        deliveryPerson.location = address.location;
    }

    private DeliveryPerson findDeliveryPersonClosestToRestaurant(Restaurant restaurant) {
        return deliveryPersons.stream()
                      .sorted(Comparator.comparing((DeliveryPerson it) -> it.location.distance(restaurant.location)).thenComparing(it -> it.orders.size()))
                      .findFirst().get();

//        .sorted((l, r) -> l.location.distance(restaurant.location)).
    }

    public void printUsers() {
        users.values().stream()
                      .sorted(Comparator.comparing(User::totalMoneySpent).thenComparing(User::getId).reversed())
                      .forEach(System.out::println);
    }

    void printRestaurants() {
        restaurants.values().stream()
                      .sorted(Comparator.comparing(Restaurant::findAverageOrderPrice).thenComparing(Restaurant::getId).reversed())
                      .forEach(System.out::println);
    }

    void printDeliveryPeople() {
        deliveryPersons.stream()
                      .sorted(Comparator.comparing(DeliveryPerson::totalMoneyEarned).thenComparing(DeliveryPerson::getId).reversed())
                      .forEach(System.out::println);
    }
}

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
