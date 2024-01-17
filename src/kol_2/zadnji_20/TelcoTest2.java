package src.kol_2.zadnji_20;

import java.util.*;
import java.util.stream.Collectors;

class DurationConverter {
    public static String convert(long duration) {
        long minutes = duration / 60;
        duration %= 60;
        return String.format("%02d:%02d", minutes, duration);
    }
}

interface State {
    void answer(long timestamp);

    void end(long timestamp);

    void hold(long timestamp);

    void resume(long timestamp);

}

abstract class CallState implements State {
    Call call;
    long callStartedTimestamp;
    long callEndedTimestamp;
    long callPausedTimestamp;
    long callResumedTimestamp;
    long totalPausedTime;

    public CallState(Call call) {
        this.call = call;
    }

    public CallState(CallState oldState) {
        this.call = oldState.call;
        this.callStartedTimestamp = oldState.callStartedTimestamp;
        this.callEndedTimestamp = oldState.callEndedTimestamp;
        this.callPausedTimestamp = oldState.callPausedTimestamp;
        this.callResumedTimestamp = oldState.callResumedTimestamp;
        this.totalPausedTime = oldState.totalPausedTime;
    }

    public long getTotalDuration() {
        return callEndedTimestamp == 0 ? 0 : callEndedTimestamp - callStartedTimestamp - totalPausedTime;
    }
}

class IdleCallState extends CallState {

    public IdleCallState(Call call) {
        super(call);
    }

    public IdleCallState(CallState oldState) {
        super(oldState);
    }

    @Override
    public void answer(long timestamp) {
        this.callStartedTimestamp = timestamp;
        call.callState = new InProgressCallState(this);
    }

    @Override
    public void end(long timestamp) {
        callStartedTimestamp = timestamp;
        callEndedTimestamp = timestamp;
        call.callState = new IdleCallState(this);
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }
}

class RingingCallState extends CallState {

    public RingingCallState(Call call) {
        super(call);
    }

    public RingingCallState(CallState oldState) {
        super(oldState);
    }

    @Override
    public void answer(long timestamp) {
        callStartedTimestamp = timestamp;
        call.callState = new InProgressCallState(this);
    }

    @Override
    public void end(long timestamp) {
        callEndedTimestamp = timestamp;
        call.callState = new IdleCallState(this);
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }
}

class InProgressCallState extends CallState {

    public InProgressCallState(Call call) {
        super(call);
    }

    public InProgressCallState(CallState oldState) {
        super(oldState);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        this.callEndedTimestamp = timestamp;
        call.callState = new IdleCallState(this);
    }

    @Override
    public void hold(long timestamp) {
        this.callPausedTimestamp = timestamp;
        call.callState = new PausedCallState(this);
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }
}

class PausedCallState extends CallState {

    public PausedCallState(Call call) {
        super(call);
    }

    public PausedCallState(CallState oldState) {
        super(oldState);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        callResumedTimestamp = timestamp;
        callEndedTimestamp = timestamp;
        totalPausedTime += callResumedTimestamp - callPausedTimestamp;
        call.callState = new IdleCallState(this);
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        //TODO
        callResumedTimestamp = timestamp;
        totalPausedTime += callResumedTimestamp - callPausedTimestamp;
        callPausedTimestamp = 0;
        callResumedTimestamp = 0;
        call.callState = new InProgressCallState(this);
    }
}

class Call implements Comparable<Call> {
    String uuid;
    String dialer;
    String receiver;

    public CallState callState;

    public Call(String uuid, String dialer, String receiver, long timestamp) {
        this.uuid = uuid;
        this.dialer = dialer;
        this.receiver = receiver;
        callState = new IdleCallState(this);
        callState.callStartedTimestamp = timestamp;
    }

    public void updateCall(long timestamp, String action) {
        if (action.equals("ANSWER")) {
            this.callState.answer(timestamp);
        } else if (action.equals("END")) {
            this.callState.end(timestamp);
        } else if (action.equals("HOLD")) {
            this.callState.hold(timestamp);
        } else if (action.equals("RESUME")) {
            this.callState.resume(timestamp);
        }
    }

    public long getStart() {
        return callState.callStartedTimestamp;
    }

    public long getEndTimestamp() {
        return callState.callEndedTimestamp;
    }

    public long getTotalDuration() {
        return callState.getTotalDuration();
    }

    @Override
    public int compareTo(Call o) {
        return Long.compare(callState.callStartedTimestamp, o.callState.callStartedTimestamp);
    }

    public boolean isMissedCall() {
        return callState.getTotalDuration() == 0;
    }
}

class TelcoApp {
    Map<String, Call> callsByUUID;
    Map<String, List<Call>> callsByPhoneNumber;

    public TelcoApp() {
        this.callsByUUID = new HashMap<>();
        callsByPhoneNumber = new HashMap<>();
    }

    public void addCall(String uuid, String dialer, String receiver, long timestamp) {
        Call call = new Call(uuid, dialer, receiver, timestamp);
        callsByUUID.putIfAbsent(uuid, call);

        callsByPhoneNumber.putIfAbsent(dialer, new ArrayList<>());
        callsByPhoneNumber.putIfAbsent(receiver, new ArrayList<>());
        callsByPhoneNumber.get(dialer).add(call);
        callsByPhoneNumber.get(receiver).add(call);
    }

    public void updateCall(String uuid, long timestamp, String action) {
        callsByUUID.get(uuid).updateCall(timestamp, action);
    }

    public void printChronologicalReport(String phoneNumber) {
        callsByPhoneNumber.get(phoneNumber).stream()
                      .sorted()
                      .forEach(it -> printCall(it, phoneNumber));
    }

    void printCall(Call c, String phoneNumber) {
        String endTime = String.valueOf(c.getEndTimestamp());
        if (c.getTotalDuration() == 0) endTime = "MISSED CALL";
        System.out.printf("%s %s %d %s %s\n",
                      c.dialer.equals(phoneNumber) ? "D" : "R",
                      c.dialer.equals(phoneNumber) ? c.receiver : c.dialer,
                      c.callState.callStartedTimestamp,
                      endTime,
                      DurationConverter.convert(c.getTotalDuration()));

    }

    public void printReportByDuration(String phoneNumber) {
        callsByPhoneNumber.get(phoneNumber).stream()
                      .sorted(Comparator.comparing(Call::getTotalDuration).thenComparing(Call::getStart).reversed())
                      .forEach(it -> printCall(it, phoneNumber));
    }

    public void printCallsDuration() {
        TreeMap<String, Long> result = callsByUUID.values().stream()
                      .collect(Collectors.groupingBy(
                                    it -> String.format("%s <-> %s", it.dialer, it.receiver),
                                    TreeMap::new,
                                    Collectors.summingLong(Call::getTotalDuration)
                      ));

        result.entrySet().stream()
                      .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                      .forEach(it -> System.out.printf("%s : %s\n", it.getKey(), DurationConverter.convert(it.getValue())));
    }
}

public class TelcoTest2 {
    public static void main(String[] args) {
        TelcoApp app = new TelcoApp();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String command = parts[0];

            if (command.equals("addCall")) {
                String uuid = parts[1];
                String dialer = parts[2];
                String receiver = parts[3];
                long timestamp = Long.parseLong(parts[4]);
                app.addCall(uuid, dialer, receiver, timestamp);
            } else if (command.equals("updateCall")) {
                String uuid = parts[1];
                long timestamp = Long.parseLong(parts[2]);
                String action = parts[3];
                app.updateCall(uuid, timestamp, action);
            } else if (command.equals("printChronologicalReport")) {
                String phoneNumber = parts[1];
                app.printChronologicalReport(phoneNumber);
            } else if (command.equals("printReportByDuration")) {
                String phoneNumber = parts[1];
                app.printReportByDuration(phoneNumber);
            } else {
                app.printCallsDuration();
            }
        }

    }
}
