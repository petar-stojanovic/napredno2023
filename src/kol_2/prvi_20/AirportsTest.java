package src.kol_2.prvi_20;

import java.util.*;
import java.util.stream.Collectors;

class Flight implements Comparable<Flight> {
    String from;
    String to;
    int time;
    int duration;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    private String getMinutesFromStart() {
        int hours = time / 60;
        int minutes = time % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    private String getMinutesFromEnd() {
        int endTime = time + duration;
        int hours = endTime / 60;
        int minutes = endTime % 60;

        if (hours > 23) {
            return String.format("%02d:%02d +1d", hours % 24, minutes);
        }

        return String.format("%02d:%02d", hours, minutes);
    }

    private String getFlightDurationAsString() {
        int hours = duration / 60;
        int minutes = duration % 60;
        return String.format("%2dh%02dm", hours, minutes);
    }

    @Override
    public int compareTo(Flight other) {
        return Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime).thenComparing(Flight::getFrom).compare(this,
                      other);
    }


    @Override
    public String toString() {
        return String.format("%s-%s %s-%s%s", from, to, getMinutesFromStart(), getMinutesFromEnd(),
                      getFlightDurationAsString());
    }
}

class Airport {
    String name;
    String country;
    String code;
    int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }


    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }

}

class Airports {

    Map<String, Airport> airports;
    Map<Airport, Set<Flight>> flightsFromAirport;

    public Airports() {
        this.airports = new HashMap<>();
        flightsFromAirport = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        this.airports.put(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from, to, time, duration);

        flightsFromAirport.putIfAbsent(findAirportByName(from), new TreeSet<>());
        flightsFromAirport.putIfAbsent(findAirportByName(to), new TreeSet<>());

        flightsFromAirport.get(findAirportByName(from)).add(flight);
        flightsFromAirport.get(findAirportByName(to)).add(flight);

    }

    private Airport findAirportByName(String name) {
        return airports.get(name);
    }

    public void showFlightsFromAirport(String code) {
        Airport airport = findAirportByName(code);
        System.out.println(airport);
        List<Flight> flightsToPrint = flightsFromAirport.get(airport)
                      .stream().filter(it -> it.from.equals(code)).collect(Collectors.toList());

        for (int i = 0; i < flightsToPrint.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, flightsToPrint.get(i));
        }
    }

    public void showDirectFlightsFromTo(String from, String to) {
        Airport airport = findAirportByName(from);
        List<Flight> flightsToPrint = flightsFromAirport.get(airport)
                      .stream().filter(it -> it.from.equals(from) && it.to.equals(to))
                      .collect(Collectors.toList());

        if (flightsToPrint.isEmpty()) {
            System.out.printf("No flights from %s to %s\n", from, to);
        } else {
            flightsToPrint.forEach(System.out::println);
        }
    }

    public void showDirectFlightsTo(String to) {
        Airport airport = findAirportByName(to);
        flightsFromAirport.get(airport)
                      .stream().filter(it -> it.to.equals(to))
                      .forEach(System.out::println);

    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

