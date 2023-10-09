package src.aud.aud1;

public class SubStringTest {

    public static void main(String[] args) {
        String str1 = "Napr";
        String str2 = "Napredno programiranje";
        System.out.println(isSubstring(str1, str2));
    }

    private static boolean isSubstring(String first, String second) {
        if (first.length() > second.length()) {
            return false;
        }

        for (int i = 0; i < first.length(); i++) {
            if (second.charAt(i) != first.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
