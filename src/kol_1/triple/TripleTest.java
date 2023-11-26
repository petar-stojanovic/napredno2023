package src.kol_1.triple;

import java.util.*;

class Triple<T extends Number & Comparable<T>> {
    T first;
    T second;
    T third;

    public Triple(T first, T second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public double max() {
        T max = first;
        if (max.doubleValue() < second.doubleValue()) {
            max = second;
        }
        if(max.doubleValue() < third.doubleValue()){
            max = third;
        }
        return max.doubleValue();
    }

    public double average() {
        return (first.doubleValue() + second.doubleValue() + third.doubleValue()) / 3.0;
//        return numbers.stream().mapToDouble(Number::doubleValue).summaryStatistics().getAverage();
    }

    public void sort() {
        if(first.doubleValue() > second.doubleValue()){
            T temp = first;
            first = second;
            second = temp;
        }
        if(first.doubleValue()> third.doubleValue()){
            T temp = first;
            first = third;
            third =temp;
        }
        if(second.doubleValue()> third.doubleValue()){
            T temp = second;
            second = third;
            third =temp;
        }
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", first.doubleValue(), second.doubleValue(), third.doubleValue());
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}