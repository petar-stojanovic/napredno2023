package src.labs.lab4;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * ZonedDateTime tests
 */
public class ZonedDateTimeTest {
    public static void main(String[] args) {
        System.out.println(zonedDateTimeOf());
        System.out.println(zonedDateTimeParse());
        System.out.println(zonedDateTimeFormat());
        System.out.println(toPST());
        System.out.println(sameInstantAs());
        System.out.println(sameLocalAs());
    }

    static java.time.ZonedDateTime zonedDateTimeOf() {
        /**
         * Create a {@link java.time.ZonedDateTime} with time of 2015-07-10 2:14:25.000 as Japan Standard Time
         * by using {@link java.time.ZonedDateTime#of} and {@link ZoneId#of}
         */
        return ZonedDateTime.of(2015,7,10,2,14,25,0,ZoneId.of("Asia/Tokyo"));
    }

    static java.time.ZonedDateTime zonedDateTimeParse() {
        /**
         * Create a {@link java.time.ZonedDateTime} with time of 2015-06-18 23:07:25.000 as Japan Standard Time
         * by using {@link java.time.ZonedDateTime#parse}
         */
        return ZonedDateTime.of(2015,6,18,23,7,25,0,ZoneId.of("Asia/Tokyo"));
    }

    static String zonedDateTimeFormat() {
        ZonedDateTime zdt = DateAndTimes.ZDT_20150618_23073050;

        /**
         * Format {@link zdt} to a {@link String} as "2015_06_18_23_07_30_JST"
         * by using {@link ZonedDateTime#format}
         */
        return zdt.format(DateTimeFormatter.ofPattern("YYYY_MM_dd_HH_mm_ss_z"));
    }

    static ZonedDateTime toPST() {
        LocalDateTime ldt = DateAndTimes.LDT_20150618_23073050;
        /**
         * Create a {@link ZonedDateTime} from {@link ldt} with Pacific Standard Time
         */
        return ldt.atZone(ZoneId.of("America/Los_Angeles"));
    }

    static ZonedDateTime sameInstantAs() {
        ZonedDateTime zdt = DateAndTimes.ZDT_20150618_23073050;
        /**
         * Create a {@link ZonedDateTime} same instant as {@link zdt} with Pacific Standard Time
         * by using {@link ZonedDateTime#withZoneSameInstant}
         */
        return zdt.withZoneSameInstant(ZoneId.of("America/Los_Angeles"));
    }

    static ZonedDateTime sameLocalAs() {
        ZonedDateTime zdt = DateAndTimes.ZDT_20150618_23073050;
        /**
         * Create a {@link ZonedDateTime} same local time as {@link zdt} with Pacific Standard Time
         * by using {@link ZonedDateTime#withZoneSameLocal}
         */
        return zdt.withZoneSameLocal(ZoneId.of("America/Los_Angeles"));
    }

    static class DateAndTimes {

        public static final LocalDateTime LDT_20150618_23073050 = LocalDateTime.of(2015, 6, 18, 23, 7, 30, 500000000);
        public static final java.time.ZonedDateTime
                      ZDT_20150618_23073050 = java.time.ZonedDateTime.of(LDT_20150618_23073050, ZoneId.of("Asia/Tokyo"));
    }
}
