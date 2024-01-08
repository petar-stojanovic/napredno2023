package src.kol_2.zadnji_20;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class WrongDateException extends Exception {

    public WrongDateException(Date date) {
        super(String.format("Wrong date: %s", date));
    }
}

class Event implements Comparable<Event> {
    String name;
    String location;
    Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM, YYY HH:mm");
        return String.format("%s at %s, %s", dateFormat.format(date), location, name);
    }

    @Override
    public int compareTo(Event other) {
        return Comparator.comparing(Event::getDate).thenComparing(Event::getName).compare(this, other);
    }
}

class EventCalendar {
    int year;
    Set<Event> events;

    public EventCalendar(int year) {
        this.year = year;
        events = new TreeSet<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.YEAR) != year) {
            throw new WrongDateException(date);
        }

        Event event = new Event(name, location, date);

        events.add(event);
    }

    public void listEvents(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM, YYY");

        List<Event> eventsToPrint = events.stream()
                      .filter(it -> dateFormat.format(it.date).equals(dateFormat.format(date)))
                      .collect(Collectors.toList());

        if (eventsToPrint.isEmpty()) {
            System.out.println("No events on this day!");
        } else {
            eventsToPrint.forEach(System.out::println);
        }

    }

    public void listByMonth() {
        Map<Integer, Integer> eventCountByMonth = events.stream().collect(Collectors.groupingBy(it -> {
                          Calendar calendar = Calendar.getInstance();
                          calendar.setTime(it.date);

                          return calendar.get(Calendar.MONTH);
                      }, Collectors.collectingAndThen(Collectors.counting(), Long::intValue)

        ));

        for (int i = 0; i < 12; i++) {
            System.out.printf("%d : %d\n", i + 1, eventCountByMonth.getOrDefault(i, 0));
        }

    }
}

// 22
public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}
