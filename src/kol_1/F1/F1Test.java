package src.kol_1.F1;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class LapTime implements Comparable<LapTime> {
    int min;
    int sec;
    int ms;

    public LapTime(int min, int sec, int ms) {
        this.min = min;
        this.sec = sec;
        this.ms = ms;
    }

    public static LapTime create(String line) {
        String[] parts = line.split(":");
        int min = Integer.parseInt(parts[0]);
        int sec = Integer.parseInt(parts[1]);
        int ms = Integer.parseInt(parts[2]);
        return new LapTime(min, sec, ms);
    }

    @Override
    public String toString() {
        return String.format("%d:%02d:%03d", min, sec, ms);
    }

    public int getTotalTime() {
        return min * 60 * 1000 + sec * 1000 + ms;
    }

    @Override
    public int compareTo(LapTime other) {
        return Integer.compare(getTotalTime(), other.getTotalTime());
    }
}

class Lap implements Comparable<Lap> {
    String driverName;
    LapTime[] times;

    public Lap(String driverName, LapTime first, LapTime second, LapTime third) {
        this.driverName = driverName;
        times = new LapTime[3];
        times[0] = first;
        times[1] = second;
        times[2] = third;
    }

    public static Lap create(String line) {
        String[] parts = line.split("\\s+");
        String name = parts[0];
        LapTime firstLap = LapTime.create(parts[1]);
        LapTime secondLap = LapTime.create(parts[2]);
        LapTime thirdLap = LapTime.create(parts[3]);

        return new Lap(name, firstLap, secondLap, thirdLap);
    }

   public LapTime getBestTime(){
        return Arrays.stream(times).max(Comparator.reverseOrder()).get();
   }

    @Override
    public int compareTo(Lap o) {
        int thisTime = Arrays.stream(times).min(Comparator.naturalOrder()).get().getTotalTime();
        int otherTime = Arrays.stream(o.times).min(Comparator.naturalOrder()).get().getTotalTime();
        return Integer.compare(thisTime, otherTime);
    }
}

class F1Race {
    List<Lap> laps;

    public F1Race() {
        laps = new ArrayList<>();
    }

    public void readResults(InputStream in) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        laps = bf.lines().map(it -> {
            return Lap.create(it);
        }).collect(Collectors.toList());

        bf.close();
    }

    public void printSorted(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        List<Lap> lapToPrint = laps.stream()
                          .sorted(Comparator.naturalOrder())
                          .collect(Collectors.toList());

        lapToPrint.forEach(it -> out.format("%d. %-10s%10s\n", lapToPrint.indexOf(it)+1, it.driverName,
                          it.getBestTime()));

        pw.flush();
    }
}