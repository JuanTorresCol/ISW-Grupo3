package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO {

    private static final Util util = new Util();

    // ----------------- helpers internos -----------------

    // Convierte lista -> "a,b,c"
    private static String listToString(ArrayList<String> lista) {
        if (lista == null || lista.isEmpty()) return "";
        return String.join(",", lista);
    }

    // Convierte "a,b,c" -> lista
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

    // Construye un Customer desde un ResultSet de `usuarios`
    private static Customer mapToCustomer(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        String pass = rs.getString("password");
        String gender = rs.getString("gender");
        int age = rs.getInt("age");

        // foodrestriction: ARRAY en BD
        var foodRestrictionArray = rs.getArray("foodrestriction");
        ArrayList<String> illegalFood = util.toArrayList(foodRestrictionArray);

        // alimentosnocome: VARCHAR/TEXT en BD
        String noComeStr = rs.getString("alimentosnocome");
        ArrayList<String> alimentosNoCome = stringToList(noComeStr);

        return new Customer(id, name, pass, gender, age, illegalFood, alimentosNoCome);
    }

    // ----------------- métodos públicos -----------------

    // registra un nuevo cliente en la base de datos
    public static void registerCliente(Customer customer) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "INSERT INTO usuarios (id, name, password, gender, age, foodrestriction, alimentosnocome) " +
                "VALUES (?,?,?,?,?,?,?)";

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

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Cliente insertado con éxito: " + customer.getUserId());
            }
        } catch (SQLException ex) {
            System.out.println("Error al insertar cliente: " + ex.getMessage());
        }
    }

    // devuelve una lista con todos los usuarios
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

    // devuelve un cliente en función al ID
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

    // devuelve un cliente en función a su nombre
    public static Customer getCliente(String inputName) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "SELECT * FROM usuarios WHERE name = ?";
        Customer cu = null;

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, inputName);

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

    // edita la información de un cliente en la base de datos
    public static void editCliente(Customer customerEdit) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "UPDATE usuarios SET name = ?, password = ?, gender = ?, age = ?, " +
                "foodrestriction = ?, alimentosnocome = ? WHERE id = ?";

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

            pst.setString(7, customerEdit.getUserId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cliente editado con éxito: " + customerEdit.getUserId());
            }
        } catch (SQLException ex) {
            System.out.println("Error al editar cliente: " + ex.getMessage());
        }
    }

    // muestra todos los usuarios que se encuentran en la base de datos
    public static void main(String[] args) {
        ArrayList<Customer> lista = new ArrayList<>();
        CustomerDAO.getClientes(lista);

        for (Customer customer : lista) {
            System.out.println("He leído el id: " + customer.getUserId());
        }
    }
}

