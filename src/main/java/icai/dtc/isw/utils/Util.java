package icai.dtc.isw.utils;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Util {

    public Util() {
    }

    // devuelve un ID unico en base a la cadena que recibe
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

    // convierte en ArrayList una lista de tipo Array
    public static ArrayList<String> toArrayList(Array sqlArray) {
        if (sqlArray == null) {
            return new ArrayList<>();
        }

        try {
            String[] stringArray = (String[]) sqlArray.getArray();
            return new ArrayList<>(Arrays.asList(stringArray));
        } catch (SQLException e) {
            throw new RuntimeException("Error converting SQL Array to ArrayList", e);
        }
    }
}
