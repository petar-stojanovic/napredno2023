package src.kol_1.subtitles;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.*;

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде
class Subtitle {
    int num;
    int startTime;
    int endTime;
    String text;

    public Subtitle(int num, String startTime, String endTime, String text) {
        this.num = num;
        this.startTime = timeStringToInt(startTime);
        this.endTime = timeStringToInt(endTime);
        this.text = text;
    }

    private int timeStringToInt(String time) {
        String[] parts = time.split(",");
        int ms = Integer.parseInt(parts[1]);
        parts = parts[0].split(":");

        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        int second = Integer.parseInt(parts[2]);

        ms += second * 1000;
        ms += minute * 60 * 1000;
        ms += hour * 60 * 60 * 1000;
        return ms;
    }

    private String timeIntToString(int time) {
        int hour = time / (60 * 60 * 1000);
        time -= hour * 60 * 60 * 1000;
        int min = time / (60 * 1000);
        time -= min * 60 * 1000;
        int sec = time / (1000);
        time -= sec * 1000;

        return String.format("%02d:%02d:%02d,%03d", hour, min, sec, time);
    }

    @Override
    public String toString() {
        return String.format("%d\n%s --> %s\n%s", num, timeIntToString(startTime), timeIntToString(endTime), text);
    }

    public void shift(int ms) {
        startTime += ms;
        endTime += ms;
    }
}

class Subtitles {

    private List<Subtitle> subtitles;

    public Subtitles() {
        this.subtitles = new ArrayList<>();
    }

    public int loadSubtitles(InputStream in) {
        Scanner scanner = new Scanner(in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int serialNumber = Integer.parseInt(line);

            String[] parts = scanner.nextLine().split("-->");

            String text = "";
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                text += line;
                text += "\n";
            }

            this.subtitles.add(new Subtitle(serialNumber, parts[0].trim(), parts[1].trim(), text));
        }

        return subtitles.size();
    }

    public void print() {
        subtitles.forEach(System.out::println);
    }

    public void shift(int ms) {
        subtitles.forEach(it -> it.shift(ms));
    }
}
