package icai.dtc.isw.dao;

import icai.dtc.isw.controler.RecetaControler;
import icai.dtc.isw.domain.*;
import icai.dtc.isw.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO {

    // Convierte una lista de String a una String
    private static String listToString(ArrayList<String> lista) {
        if (lista == null || lista.isEmpty()) return "";
        return String.join(",", lista);
    }

    // Convierte un String a una list separando los elementos
    private static ArrayList<String> stringToList(String data) {
        ArrayList<String> res = new ArrayList<>();
        if (data == null || data.isBlank()) return res;

        String[] parts = data.split(",");
        for (String p : parts) {
            String t = p.trim();
            if (!t.isEmpty()) {
                res.add(t);
            }
        }
        return res;
    }

    // Devuelve un cliente de los datos extraidos de la base de datos
    private static Customer mapToCustomer(ResultSet rs) throws SQLException {
        if(rs.getInt("puesto")==0) {
            String id = rs.getString("id");
            String name = rs.getString("name");
            String pass = rs.getString("password");
            String gender = rs.getString("gender");
            int age = rs.getInt("age");

            // foodrestriction
            var foodRestrictionArray = rs.getArray("foodrestriction");
            ArrayList<String> illegalFood = Util.toArrayList(foodRestrictionArray);

            // alimentosnocome
            String noComeStr = rs.getString("alimentosnocome");
            ArrayList<String> alimentosNoCome = stringToList(noComeStr);

            var favRecetasV = rs.getArray("favrecetas");
            ArrayList<String> favRecetasId = Util.toArrayList(favRecetasV);
            ArrayList<Receta> favRecetas = new ArrayList<>();
            for (String idReceta : favRecetasId) {
                favRecetas.add(RecetaControler.getRecetaId(idReceta));
            }
            int presupuesto = rs.getInt("presupuesto");

            return new Customer(id, name, pass, gender, age, illegalFood, alimentosNoCome, favRecetas, presupuesto);
        } else{return null;}
    }

    // Devuelve el menu asociado a un cliente de la base de datos
    private static MenuSemanal mapToNewMenu(ResultSet rs) throws SQLException {
        if(rs.getInt("puesto")==0) {
            // Id recetas
            var recetasArr = rs.getArray("menusemanal");
            ArrayList<String> recetasId = Util.toArrayList(recetasArr);

            ArrayList<Receta> recetas = new ArrayList<>();
            for(String recetaId : recetasId){
                recetas.add(RecetaControler.getRecetaId(recetaId));
            }

            return new MenuSemanal(recetas);
        } else{return null;}
    }

    // Registra un nuevo cliente en la base de datos
    public static boolean registerCliente(Customer customer) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "INSERT INTO usuarios " +
                "(id, name, password, gender, age, foodrestriction, alimentosnocome, favrecetas, puesto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, customer.getUserId());
            pst.setString(2, customer.getUserName());
            pst.setString(3, customer.getUserPass());
            pst.setString(4, customer.getUserGender());
            pst.setInt(5, customer.getUserAge());

            // illegalFood como ARRAY (ej. varchar[])
            pst.setArray(6, pst.getConnection().createArrayOf(
                    "varchar",
                    customer.getIllegalFood().toArray(new String[0])
            ));

            // alimentosNoCome como CSV en un VARCHAR/TEXT
            pst.setString(7, listToString(customer.getAlimentosNoCome()));

            pst.setArray(8, pst.getConnection().createArrayOf(
                    "varchar",
                    customer.getRecetasFavId().toArray(new String[0])));

            pst.setInt(9,0);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Cliente insertado con éxito: " + customer.getUserId());
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al insertar cliente: " + ex.getMessage());
            return false;
        }
    }

    // Devuelve una lista con todos los usuarios
    public static void getClientes(ArrayList<Customer> lista) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "SELECT * FROM usuarios";

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Customer c = mapToCustomer(rs);
                lista.add(c);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Devuelve un cliente en función al ID
    public static Customer getClienteId(String inputId) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Customer cu = null;

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, inputId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    cu = mapToCustomer(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return cu;
    }

    // Devuelve un cliente en función a su nombre
    public static ContainerMenuCustomer getCliente(String inputName) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "SELECT * FROM usuarios WHERE name = ?";
        Customer cu;
        MenuSemanal ms;
        ContainerMenuCustomer mmmmmmmmmmmmm = null;

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, inputName);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    cu = mapToCustomer(rs);
                    ms = mapToNewMenu(rs);
                    mmmmmmmmmmmmm = new ContainerMenuCustomer(ms, cu);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return mmmmmmmmmmmmm;
    }

    // Guarda en la base de datos
    public static void guardaMenu(Customer cu, ArrayList<String> recetasId, int presupuesto){
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "UPDATE usuarios SET menusemanal = ?, presupuesto = ? WHERE id = ?";

        try(PreparedStatement pst = con.prepareStatement(sql)){
            pst.setArray(1, pst.getConnection().createArrayOf(
                    "varchar",
                    recetasId.toArray(new String[0])
            ));
            pst.setInt(2, presupuesto);
            pst.setString(3,cu.getUserId())
            ;
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Menú guardado con éxito: " + cu.getUserId());
            }
        } catch (SQLException ex) {
            System.out.println("Error al guardar el menú: " + ex.getMessage());
        }
    }

    // Edita la información de un cliente en la base de datos
    public static void editCliente(Customer customerEdit) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "UPDATE usuarios SET name = ?, password = ?, gender = ?, age = ?, " +
                "foodrestriction = ?, alimentosnocome = ?, favrecetas = ? WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, customerEdit.getUserName());
            pst.setString(2, customerEdit.getUserPass());
            pst.setString(3, customerEdit.getUserGender());
            pst.setInt(4, customerEdit.getUserAge());

            pst.setArray(5, pst.getConnection().createArrayOf(
                    "varchar",
                    customerEdit.getIllegalFood().toArray(new String[0])
            ));

            pst.setString(6, listToString(customerEdit.getAlimentosNoCome()));

            pst.setArray(7, pst.getConnection().createArrayOf(
                    "varchar",
                    customerEdit.getRecetasFavId().toArray(new String[0])
            ));

            pst.setString(8, customerEdit.getUserId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cliente editado con éxito: " + customerEdit.getUserId());
            }
        } catch (SQLException ex) {
            System.out.println("Error al editar cliente: " + ex.getMessage());
        }
    }

    // Muestra todos los usuarios que se encuentran en la base de datos
    public static void main(String[] args) {
        ArrayList<Customer> lista = new ArrayList<>();
        CustomerDAO.getClientes(lista);

        for (Customer customer : lista) {
            System.out.println("He leído el id: " + customer.getUserId());
        }
    }
}

