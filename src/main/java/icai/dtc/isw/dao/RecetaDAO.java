package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.domain.Dificultad;
import icai.dtc.isw.domain.Producto;
import icai.dtc.isw.domain.Receta;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RecetaDAO {

    private static Map<Producto,Integer> hola = new HashMap<>();


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

    public static void getRecetas(ArrayList<Receta> lista) {
        Connection con=ConnectionDAO.getInstance().getConnection();
        hola.put(new Producto("pollo"),6);
        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM recetas");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(new Receta(rs.getString(1),rs.getString(2), Dificultad.valueOf(rs.getString(7)),rs.getInt(3), rs.getDouble(4),rs.getString(5),hola));
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public static Receta getRecetaId(String inputId) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        Receta receta = null;

        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM recetas WHERE id = ?")) {
            pst.setString(1, inputId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    receta = new Receta(rs.getString(1),rs.getString(2), Dificultad.valueOf(rs.getString(7)),rs.getInt(3), rs.getDouble(4),rs.getString(5),hola);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return receta;
    }

    public static Receta getReceta(String inputName) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        Receta receta = null;

        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM usuarios WHERE name = ?")) {
            pst.setString(1, inputName);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    receta = new Receta(rs.getString(1),rs.getString(2), Dificultad.valueOf(rs.getString(7)),rs.getInt(3), rs.getDouble(4),rs.getString(5),hola);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return receta;
    }

    public static void main(String[] args) {

        ArrayList<Receta> lista = new ArrayList<>();
        RecetaDAO.getRecetas(lista);


        for (Receta receta : lista) {
            System.out.println("He leído la receta: " + receta.toString());
        }

    }
}
