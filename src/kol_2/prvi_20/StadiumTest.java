package src.kol_2.prvi_20;

import java.util.*;

class SeatNotAllowedException extends Exception {
    public SeatNotAllowedException() {
        super("SeatNotAllowedException");
    }
}

class SeatTakenException extends Exception {
    public SeatTakenException() {
        super("SeatTakenException");
    }
}

class Sector implements Comparable<Sector> {
    String code;
    int numberOfSeats;

    Map<Integer, Integer> takenSeats;

    public Sector(String code, int numberOfSeats) {
        this.code = code;
        this.numberOfSeats = numberOfSeats;
        takenSeats = new HashMap<>();
    }

    public String getCode() {
        return code;
    }

    public void buyTicket(int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if (takenSeats.containsKey(seat)) {
            throw new SeatTakenException();
        } else if (type == 1 && takenSeats.containsValue(2)) {
            throw new SeatNotAllowedException();
        } else if (type == 2 && takenSeats.containsValue(1)) {
            throw new SeatNotAllowedException();
        }
        takenSeats.put(seat, type);
    }

    private float percentageTakenSeats() {
        return 100.0f * takenSeats.size() / numberOfSeats;
    }

    private int numberOfFreeSeats() {
        return numberOfSeats - takenSeats.size();
    }

    @Override
    public int compareTo(Sector other) {
        return Comparator.comparing(Sector::numberOfFreeSeats).reversed().thenComparing(Sector::getCode).compare(this, other);
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, numberOfFreeSeats(), numberOfSeats,
                      percentageTakenSeats());
    }

}

class Stadium {
    String name;
    Map<String, Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        sectors = new TreeMap<>();
    }

    void createSectors(String[] sectorNames, int[] sizes) {
        for (int i = 0; i < sectorNames.length; i++) {
            sectors.put(sectorNames[i], new Sector(sectorNames[i], sizes[i]));
        }
    }

    void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        sectors.get(sectorName).buyTicket(seat, type);
    }

    void showSectors() {
        sectors.values().stream().sorted().forEach(System.out::println);
    }

}

public class StadiumTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                              Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
