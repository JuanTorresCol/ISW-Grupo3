package icai.dtc.isw.dao;

import icai.dtc.isw.domain.*;
import icai.dtc.isw.utils.Knapsack;
import icai.dtc.isw.utils.Util;

import java.sql.*;
import java.util.*;

public class RecetaDAO {

    public static final Util util = new Util();

    // devuelve una lista con todas las recetas que se encuentran en la base de datos
    public static ArrayList<Receta> getRecetas() {
        Map<String, Ingrediente> ingredientes = new HashMap<>();
        Connection con=ConnectionDAO.getInstance().getConnection();
        ArrayList<Receta> lista = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM recetas");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                ArrayList<String> ingredient;
                ingredient = util.toArrayList(rs.getArray(7));
                for (int i = 0; i < ingredient.size(); i++) {
                    List<String> partes = Arrays.asList(ingredient.get(i).split(","));
                    ingredientes.put(partes.get(0),new Ingrediente(partes.get(0), partes.get(1), Double.parseDouble(partes.get(2))));
                }
                lista.add(new Receta(rs.getString(1),rs.getString(2), Dificultad.valueOf(rs.getString(6)),rs.getInt(3), rs.getDouble(4),rs.getString(5),ingredientes));
            }

        } catch (SQLException ex) {
            lista = new ArrayList<>();
            System.out.println(ex.getMessage());
        }
        return lista;
    }

    // devuelve una receta en funcion al Id que tiene
    public static Receta getRecetaId(String recetaId) {
        Map<String, Ingrediente> ingredientes = new HashMap<>();
        Connection con=ConnectionDAO.getInstance().getConnection();
        Receta re = null;
        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM recetas WHERE id = ?")) {
            pst.setString(1, recetaId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    ArrayList<String> ingredients;
                    ingredients = util.toArrayList(rs.getArray(7));
                    for (int i = 0; i < ingredients.size(); i++) {
                        List<String> partes = Arrays.asList(ingredients.get(i).split(","));
                        ingredientes.put(partes.get(0),new Ingrediente(partes.get(0),partes.get(1),Double.parseDouble(partes.get(2))));
                    }
                    re = new Receta(rs.getString(1),rs.getString(2), Dificultad.valueOf(rs.getString(6)),rs.getInt(3), rs.getDouble(4),rs.getString(5),ingredientes);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return re;
    }

    // devuelve una receta en funcion al nombre que esta tiene
    public static Receta getRecetaName(String recetaName) {
        Map<String, Ingrediente> ingredientes = new HashMap<>();
        Connection con=ConnectionDAO.getInstance().getConnection();
        Receta re = null;
        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM recetas WHERE nombre = ?")) {
            pst.setString(1, recetaName);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    ArrayList<String> ingredients;
                    ingredients = util.toArrayList(rs.getArray(7));
                    for (int i = 0; i < ingredients.size(); i++) {
                        List<String> partes = Arrays.asList(ingredients.get(i).split(","));
                        ingredientes.put(partes.get(0),new Ingrediente(partes.get(0), partes.get(1),Double.parseDouble(partes.get(2))));
                    }
                    re = new Receta(rs.getString(1),rs.getString(2), Dificultad.valueOf(rs.getString(6)),rs.getInt(3), rs.getDouble(4),rs.getString(5),ingredientes);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return re;
    }

    // registra una nueva receta en la base de datos
    public static void registerReceta(Receta receta) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "INSERT INTO recetas (id, nombre, duracion, precio, descripcion, dificultad, ingredientes) VALUES (?,?,?,?,?,?,?)";
        Map<String, Ingrediente> ing =  new HashMap<>();
        ing = receta.getIngredientes();
        ArrayList<String> lista;
        lista = new ArrayList<>();
        for (Map.Entry<String, Ingrediente> entry : ing.entrySet()) {
            String key = entry.getKey();
            Ingrediente values = entry.getValue();

            String concatenado = key + "," + values.getCantidad() + "," + values.getPrecio_unitario();
            lista.add(concatenado);
        }
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, receta.getId());
            pst.setString(2, receta.getNombre());
            pst.setInt(3, receta.getDuracion());
            pst.setDouble(4, receta.getPrecio());
            pst.setString(5, receta.getDescripcion());
            pst.setString(6, String.valueOf(receta.getDificultad()));
            pst.setArray(7,pst.getConnection().createArrayOf("VARCHAR", lista.toArray(new String[0])));
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Receta insertada con éxito: " + receta.getId());
            }
        } catch (SQLException ex) {
            System.out.println("Error al insertar la receta: " + ex.getMessage());
        }
    }

    // imprime por pantalla todas las recetas que se encuentran en la base de datos mostrando su ID
    public static void main(String[] args) {

        ArrayList<Receta> lista = RecetaDAO.getRecetas();
        /*
        for (Receta receta : lista) {
            System.out.println("He leído la receta: " + receta.toString());
        }

         */

        ArrayList<Receta> seleccion = Knapsack.seleccionar10(lista,100);
        for (Receta receta : seleccion) {
            System.out.println("He escogido las recetas: " + receta.toString());
        }



    }
}
