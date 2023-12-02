package src.kol_1.F1_trial;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

// vashiot kod ovde

class F1Race {
    List<Lap> times;

    public void readResults(InputStream in) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        times = bf.lines().map(Lap::create).collect(Collectors.toList());

        bf.close();
    }

    public void printSorted(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);

        times.sort(Comparator.naturalOrder());
        for (int i = 0; i < times.size(); i++) {
            pw.println(String.format("%d. %s", i + 1, times.get(i).toString()));
        }

        pw.flush();
    }

}

class Lap implements Comparable<Lap> {
    private String driverName;
    private List<Time> laps;

    public Lap(String driverName, List<Time> laps) {
        this.driverName = driverName;
        this.laps = laps;
    }

    public static Lap create(String line) {
        String[] parts = line.split("\\s+");
        String driver = parts[0];

        List<Time> timesList = new ArrayList<>();
        IntStream.range(1, 4).forEach(it -> {
            String[] times = parts[it].split(":");
            int minutes = Integer.parseInt(times[0]);
            int seconds = Integer.parseInt(times[1]);
            int milliseconds = Integer.parseInt(times[2]);

            timesList.add(new Time(minutes, seconds, milliseconds));
        });

        return new Lap(driver, timesList);
    }

    Time getBestLapTime() {
        return laps.stream().sorted().findFirst().get();
    }

    @Override
    public int compareTo(Lap o) {
        return Double.compare(getBestLapTime().getTime(), o.getBestLapTime().getTime());
    }

    @Override
    public String toString() {
        return String.format("%-8s%s", driverName, getBestLapTime());
    }
}

class Time implements Comparable<Time> {
    private int min;
    private int sec;
    private int ms;

    public Time(int min, int sec, int ms) {
        this.min = min;
        this.sec = sec;
        this.ms = ms;
    }

    public double getTime() {
        return min * 60 * 1000 + sec * 1000 + ms;
    }

    @Override
    public int compareTo(Time o) {
        return Double.compare(getTime(), o.getTime());
    }

    @Override
    public String toString() {
        return String.format("%d:%02d:%03d", min, sec, ms);
    }
}