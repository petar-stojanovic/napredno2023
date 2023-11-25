package src.labs.lab3;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {

    T min;
    T max;
    int updateMethodsCalled;
    int maxCalled;
    int minCalled;

    public MinMax() {
        updateMethodsCalled = 0;
        minCalled = maxCalled = 0;
    }

    void update(T element) {
        if (updateMethodsCalled == 0) {
            min = max = element;
        }

        if (min.compareTo(element) > 0) {
            min = element;
            minCalled = 1;
        } else if (min.compareTo(element) == 0) {
            minCalled++;
        }

        if (max.compareTo(element) < 0) {
            max = element;
            maxCalled = 1;
        } else if (max.compareTo(element) == 0) {
            maxCalled++;
        }

        updateMethodsCalled++;
    }

    T min() {
        return min;
    }

    T max() {
        return max;
    }

    @Override
    public String toString() {
        return min + " " + max + " " + (updateMethodsCalled - (maxCalled + minCalled)) + "\n";
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
