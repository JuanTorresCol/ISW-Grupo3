package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Producto;

import icai.dtc.isw.domain.Unidad;
import icai.dtc.isw.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductoDAO {

    public static final Util util = new Util();

    // registra un producto en la base de datos
    public static void registerProducto(Producto producto) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "INSERT INTO producto (productoId, nombre, unidad, precio) VALUES (?,?,?,?)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, producto.getId());
            pst.setString(2, producto.getNombre());
            pst.setString(3, producto.getUnidadP().toString());
            pst.setDouble(4,producto.getPrecio());
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Producto insertadao con éxito: " + producto.getId());
            }
        } catch (SQLException ex) {
            System.out.println("Error al insertar el producto: " + ex.getMessage());
        }
    }

    // busca un producto en la base de datos en funcion al nombre que tiene
    public static Producto getProductoName(String productoName) {
        Connection con=ConnectionDAO.getInstance().getConnection();
        Producto pr = null;
        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM producto WHERE nombre = ?")) {
            pst.setString(1, productoName);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    pr = new Producto(rs.getString(1),rs.getString(2), Unidad.valueOf(rs.getString(3)), rs.getDouble(4));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return pr;
    }

    // saca los productos existentes en la base de datos
    public static ArrayList<Producto> getProductos() {
        Connection con=ConnectionDAO.getInstance().getConnection();
        ArrayList<Producto> lista = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM producto")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Producto(rs.getString(1),rs.getString(2), Unidad.valueOf(rs.getString(3)), rs.getDouble(4)));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return lista;
    }


    // imprime por pantalla todos los productos que se encuentran en la base de datos mostrando su ID
    public static void main(String[] args) {

        ArrayList<Producto> lista = ProductoDAO.getProductos();

        for (Producto producto : lista) {
            System.out.println("He leído el producto: " + producto.toString());
        }

    }

}
