//package src.labs.lab1;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Scanner;
//import java.util.stream.IntStream;
//
//public class RomanConverterTest {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        int n = scanner.nextInt();
//        IntStream.range(0, n)
//                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
//        scanner.close();
//    }
//}
//
//
//class RomanConverter {
//    /**
//     * Roman to decimal converter
//     *
//     * @param n number in decimal format
//     * @return string representation of the number in Roman numeral
//     */
//    public static String toRoman(int n) {
//        // your solution here
//        Map<Integer, String> romanLetters = getAllRomanLetters();
//
//        StringBuilder sb = new StringBuilder();
//        int numberOfTens = 1;
//        while (n > 0) {
//            int digit = n % 10 * numberOfTens;
//
//            String lettersToInsert;
//            if(numberOfTens == 1000){
//                lettersToInsert = romanLetters.get(1000).repeat(n);
//            }else{
//            lettersToInsert = romanLetters.get(digit);
//            }
//            sb.insert(0, lettersToInsert);
//
//            n /= 10;
//            numberOfTens *= 10;
//        }
////MMMMMMDCCLXIII
//        return sb.toString();
//    }
//
//    private static Map<Integer, String> getAllRomanLetters() {
//        Map<Integer, String> romanLetters = new HashMap<>();
//        romanLetters.put(0, "");
//        romanLetters.put(1, "I");
//        romanLetters.put(2, "II");
//        romanLetters.put(3, "III");
//        romanLetters.put(4, "IV");
//        romanLetters.put(5, "V");
//        romanLetters.put(6, "VI");
//        romanLetters.put(7, "VII");
//        romanLetters.put(8, "VIII");
//        romanLetters.put(9, "IX");
//        romanLetters.put(10, "X");
//
//        romanLetters.put(20, "XX");
//        romanLetters.put(30, "XXX");
//        romanLetters.put(40, "XL");
//        romanLetters.put(50, "L");
//        romanLetters.put(60, "LX");
//        romanLetters.put(70, "LXX");
//        romanLetters.put(80, "LXXX");
//        romanLetters.put(90, "XC");
//
//        romanLetters.put(100, "C");
//        romanLetters.put(200, "CC");
//        romanLetters.put(300, "CCC");
//        romanLetters.put(400, "CD");
//        romanLetters.put(500, "D");
//        romanLetters.put(600, "DC");
//        romanLetters.put(700, "DCC");
//        romanLetters.put(800, "DCCC");
//        romanLetters.put(900, "CM");
//        romanLetters.put(1000, "M");
//
//        return romanLetters;
//    }
//
//}
