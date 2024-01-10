package src.kol_2.prvi_20;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract class Employee implements Comparable<Employee> {

    String id;
    String level;

    Double rate;

    public Employee(String id, String level, Double rate) {
        this.id = id;
        this.level = level;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    abstract double salary();

    abstract Character getType();

    @Override
    public int compareTo(Employee o) {
        return Comparator.comparing(Employee::salary, Comparator.reverseOrder()).thenComparing(Employee::getLevel).compare(this, o);
    }
}

class HourlyEmployee extends Employee {
    double hours;

    static final double COEFFICIENT = 1.5;

    public HourlyEmployee(String id, String level, Double rate, double hours) {
        super(id, level, rate);
        this.hours = hours;
    }

    @Override
    double salary() {
        return normalHours() * rate + overtimeHours() * rate * COEFFICIENT;
    }

    @Override
    Character getType() {
        return 'H';
    }

    private double normalHours() {
        return Math.min(hours, 40);
    }

    private double overtimeHours() {
        return Math.max(hours - 40, 0);
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f", id, level, salary(), normalHours(), overtimeHours());
    }

}

class FreelanceEmployee extends Employee {

    List<Integer> ticketPoints;

    public FreelanceEmployee(String id, String level, Double rate, List<Integer> ticketPoints) {
        super(id, level, rate);
        this.ticketPoints = ticketPoints;
    }

    @Override
    double salary() {
        return ticketSum() * rate;
    }

    int ticketSum() {
        return ticketPoints.stream().mapToInt(i -> i).sum();
    }

    @Override
    Character getType() {
        return 'F';
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",
                      id,
                      level,
                      salary(),
                      ticketPoints.size(),
                      ticketSum());
    }
}

class PayrollSystem {
    Map<String, Double> hourlyRateByLevel;
    Map<String, Double> ticketRateByLevel;

    Map<String, Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        employees = new HashMap<>();
    }

    public void readEmployees(InputStream is) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));

        employees = bf.lines()
                      .map(line -> EmployeeFactory.createEmployee(line, hourlyRateByLevel, ticketRateByLevel))
                      .collect(Collectors.toMap(it -> it.id, it -> it));

    }

    public Map<String, Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        PrintWriter pw = new PrintWriter(os);

        Set<Employee> employeeSet = employees.values().stream().filter(emp -> levels.stream().anyMatch(level -> level.equals(emp.level))).collect(Collectors.toSet());
        pw.flush();

        return employeeSet.stream().collect(Collectors.groupingBy(
                      it -> it.level,
                      () -> new TreeMap<>(),
                      Collectors.toCollection(TreeSet::new)
        ));

    }
}

class EmployeeFactory {
    public static Employee createEmployee(String line, Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        String[] parts = line.split(";");
        Character type = parts[0].charAt(0);
        String id = parts[1];
        String level = parts[2];

        if (type.equals('H')) {
            double hours = Double.parseDouble(parts[3]);
            return new HourlyEmployee(id, level, hourlyRateByLevel.get(level), hours);
        }

        List<Integer> points = IntStream.range(3, parts.length)
                      .map(i -> Integer.parseInt(parts[i]))
                      .boxed()
                      .collect(Collectors.toList());

        return new FreelanceEmployee(id, level, ticketRateByLevel.get(level), points);
    }
}

// 06
public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
//        payrollSystem.printEmployeesByLevels(System.out, levels);
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });

    }
}