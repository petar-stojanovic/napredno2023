package src.kol_1.times;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}

class InvalidTimeException extends Exception {

    public InvalidTimeException(String message) {
        super(message);
    }
}

class Time implements Comparable<Time> {
    int hour;
    int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public static Time create(String line) throws UnsupportedFormatException, InvalidTimeException {
        String[] parts = line.split("[:.]");
        if (parts.length != 2) {
            throw new UnsupportedFormatException(line);
        }

        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new InvalidTimeException(line);
        }
        return new Time(hour, minute);
    }

    @Override
    public String toString() {
        return String.format("%2d:%02d", hour, minute);
    }

    public String toStringAMPM() {
        if (hour == 0) {
            return String.format("%2d:%02d AM", hour + 12, minute);
        } else if (hour > 0 && hour < 12) {
            return String.format("%2d:%02d AM", hour, minute);
        } else if (hour == 12) {
            return String.format("%2d:%02d PM", hour, minute);
        } else {
            return String.format("%2d:%02d PM", hour - 12, minute);
        }
    }

    @Override
    public int compareTo(Time o) {
        if (hour == o.hour) {
            return Integer.compare(minute, o.minute);
        }
        return Integer.compare(hour, o.hour);
    }
}

class TimeTable {
    List<Time> times;

    public TimeTable() {
        times = new ArrayList<>();
    }

    void readTimes(InputStream inputStream) throws InvalidTimeException, UnsupportedFormatException {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String parts[] = line.split("\\s+");
            for (String part : parts) {
                times.add(Time.create(part));
            }
        }
    }

    void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintWriter pw = new PrintWriter(outputStream);

        Collections.sort(times);

        for (Time time : times) {
            if (format.equals(TimeFormat.FORMAT_24)) {
                pw.println(time);
            } else {
                pw.println(time.toStringAMPM());
            }
        }

        pw.flush();
    }

}

public class TimesTest {

    public static void main(String[] args) throws IOException {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}
