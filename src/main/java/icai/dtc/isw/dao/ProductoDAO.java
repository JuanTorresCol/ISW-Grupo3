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
        String sql = "INSERT INTO producto (productoId, nombre, cantidad, unidad, preciounit) VALUES (?,?,?,?,?)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, producto.getId());
            pst.setString(2, producto.getNombre());
            pst.setInt(3, producto.getCantidad());
            pst.setString(4, producto.getUnidadP().toString());
            pst.setDouble(5,producto.getPrecio_unit());
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
                    pr = new Producto(rs.getString(1),rs.getString(2),rs.getInt(3), Unidad.valueOf(rs.getString(4)), rs.getDouble(5));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return pr;
    }

}
