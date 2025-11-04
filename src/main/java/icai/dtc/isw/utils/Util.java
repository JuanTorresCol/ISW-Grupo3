package icai.dtc.isw.utils;

public class Util {

    public Util() {
    }

    public static String createUserId(String username) {
        char firstLetter = Character.toUpperCase(username.charAt(0));
        int hash = 0;
        for (char c : username.toCharArray()) {
            hash += c;
        }
        int digits = Math.abs(hash % 100000);
        String formattedDigits = String.format("%05d", digits);
        return firstLetter + formattedDigits;
    }

}
