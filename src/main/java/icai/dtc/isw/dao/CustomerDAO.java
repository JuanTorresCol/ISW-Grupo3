package icai.dtc.isw.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import icai.dtc.isw.domain.Customer;

public class CustomerDAO {

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

    public static void registerCliente(Customer customer) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "INSERT INTO usuarios (id, name, password, gender, age, foodrestriction, alimentosnocome) VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, customer.getUserId());
            pst.setString(2, customer.getUserName());
            pst.setString(3, customer.getUserPass());
            pst.setString(4, customer.getUserGender());
            pst.setInt(5, customer.getUserAge());
            pst.setArray(6, pst.getConnection().createArrayOf("VARCHAR", customer.getIllegalFood().toArray(new String[0])));
            pst.setString(7,customer.getAlimentosNoCome());
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Cliente insertado con éxito: " + customer.getUserId());
            }
        } catch (SQLException ex) {
            System.out.println("Error al insertar cliente: " + ex.getMessage());
        }
    }



    public static void getClientes(ArrayList<Customer> lista) {
        Connection con=ConnectionDAO.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM usuarios");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(new Customer(rs.getString(2),rs.getString(3),rs.getString(4), rs.getInt(5),toArrayList(rs.getArray(6)), rs.getString(7)));
            }

        } catch (SQLException ex) {

            System.out.println(ex.getMessage());
        }
    }
    public static Customer getCliente(String inputId) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        Customer cu = null;

        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM usuarios WHERE name = ?")) {
            pst.setString(1, inputId);  // bind parameter safely

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    cu = new Customer(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), toArrayList(rs.getArray(6)), rs.getString(7));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return cu;
    }

    public static void main(String[] args) {


        ArrayList<Customer> lista= new ArrayList<>();
        CustomerDAO.getClientes(lista);


        for (Customer customer : lista) {
            System.out.println("He leído el id: "+customer.getUserId());
        }


    }

}
