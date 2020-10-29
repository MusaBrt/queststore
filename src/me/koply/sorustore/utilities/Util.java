package me.koply.sorustore.utilities;

public class Util {

    public static char[] selections = {'A', 'B', 'C', 'D', 'E'};

    // Şık uygunluğu kontrol eden method
    public static boolean selectionsCheck(String str) {
        for (char c : selections) {
            if (str.equalsIgnoreCase(c + "")) {
                return true;
            }
        }
        return false;
    }

    public static String getStrBoolean(boolean bool) {
        if (bool) return "Doğru";
        else return "Yanlış";
    }

}