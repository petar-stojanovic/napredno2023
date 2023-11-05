package src.labs.lab2;

import java.text.DecimalFormat;
import java.util.*;

enum Operator {
    VIP, ONE, TMOBILE
}

abstract class Contact implements Comparable<Contact> {
    private String date;

    public Contact(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Contact o) {
        return date.compareTo(o.date);
    }

    public boolean isNewerThan(Contact c) {
        return this.date.compareTo(c.date) > 0;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return "Contact{" +
                "date='" + date + '\'' +
                '}';
    }
}

class EmailContact extends Contact {
    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String toString() {
        return "\"" + email + "\"";
    }
}

class PhoneContact extends Contact {
    private String phone;
    private Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Operator getOperator() {
        if (phone.startsWith("075") || phone.startsWith("076")) {
            return Operator.ONE;
        } else if (phone.startsWith("077") || phone.startsWith("078")) {
            return Operator.VIP;
        }
        return Operator.TMOBILE;
    }

    @Override
    public String getType() {
        return "Phone";
    }

    @Override
    public String toString() {
        return "\"" + phone + "\"";
    }
}

class Student {
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private List<Contact> contacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        contacts = new ArrayList<>();
    }

    public int getNumberOfContacts() {
        return contacts.size();
    }

    public String getFullName() {
        return firstName.toUpperCase() + " " + lastName.toUpperCase();
    }

    public long getIndex() {
        return index;
    }

    public String getCity() {
        return city;
    }

    public Contact getLatestContact() {
        return contacts.stream().max(Contact::compareTo).orElse(null);
    }

    public void addEmailContact(String date, String email) {
        contacts.add(new EmailContact(date, email));
    }

    public void addPhoneContact(String date, String email) {
        contacts.add(new PhoneContact(date, email));
    }

    public Contact[] getEmailContacts() {
        return contacts.stream().filter(contact -> contact.getType().equals("Email")).toArray(Contact[]::new);
    }

    public Contact[] getPhoneContacts() {
        return contacts.stream().filter(contact -> contact.getType().equals("Phone")).toArray(Contact[]::new);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"ime\":\"").append(firstName).append("\", ");
        sb.append("\"prezime\":\"").append(lastName).append("\", ");
        sb.append("\"vozrast\":").append(age).append(", ");
        sb.append("\"grad\":\"").append(city).append("\", ");
        sb.append("\"indeks\":").append(index).append(", ");
        sb.append("\"telefonskiKontakti\":").append(Arrays.toString(getPhoneContacts())).append(", ");
        sb.append("\"emailKontakti\":").append(Arrays.toString(getEmailContacts())).append("}");
        return sb.toString();
    }

}

class Faculty {
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String cityName) {
        return (int) Arrays.stream(students).filter(student -> student.getCity().equals(cityName)).count();
    }

    public Student getStudent(long index) {
        return Arrays.stream(students).filter(student -> student.getIndex() == index).findFirst().orElse(null);
    }

    public double getAverageNumberOfContacts() {
        double totalContacts = Arrays.stream(students).mapToInt(Student::getNumberOfContacts).sum();
        return totalContacts / students.length;
    }

    public Student getStudentWithMostContacts() {
        return Arrays.stream(students)
                .max(Comparator.comparing(Student::getNumberOfContacts)
                        .thenComparing(Student::getIndex))
                .orElse(null);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("{\"fakultet\":\"").append(name).append("\", ");
        sb.append("\"studenti\":").append(Arrays.toString(students)).append("}");
        return sb.toString();
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
