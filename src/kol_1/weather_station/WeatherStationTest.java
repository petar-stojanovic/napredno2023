package src.kol_1.weather_station;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

class Measurement implements Comparable<Measurement> {
    private float temperature;
    private float wind;
    private float humidity;
    private float visibility;
    private Date date;

    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, wind, humidity, visibility, date);
    }

    @Override
    public int compareTo(Measurement o) {

        return this.date.compareTo(o.date);
    }
}

class WeatherStation {

    List<Measurement> measurements;
    int daysBeforeLastMeasurement;

    public WeatherStation(int days) {
        daysBeforeLastMeasurement = days;
        measurements = new ArrayList<>();
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {
        Measurement m = new Measurement(temperature, wind, humidity, visibility, date);
        long newMeasurementDateMillis = m.getDate().getTime();
        Date d = Date.from(Instant.ofEpochMilli(newMeasurementDateMillis - 1000L * 60 * 60 * 24 * daysBeforeLastMeasurement));

        Optional<Measurement> max = measurements.stream().max(Comparator.naturalOrder());
        if (max.isPresent() && Math.abs(max.get().getDate().getTime() - date.getTime()) < 1000 * 150) {
            return;
        }

        measurements.removeIf(it -> it.getDate().before(d));

        measurements.add(m);
    }

    public int total() {
        return measurements.size();
    }

    public void status(Date from, Date to) {
        List<Measurement> m = measurements.stream()
                .filter(it -> it.getDate().compareTo(from) >= 0 && it.getDate().compareTo(to) <= 0)
                .collect(Collectors.toList());

        if (m.isEmpty()) {
            throw new RuntimeException();
        }
        Collections.sort(m);
        m.forEach(System.out::println);

        System.out.printf("Average temperature: %.2f", m.stream().mapToDouble(Measurement::getTemperature).average().orElse(0.0));
    }

}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde