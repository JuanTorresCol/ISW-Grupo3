package icai.dtc.isw.dao;

import icai.dtc.isw.controler.ProductoControler;
import icai.dtc.isw.domain.Producto;
import icai.dtc.isw.domain.Supermercado;
import icai.dtc.isw.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupermercadoDAO {

    // añade un producto a un supermercado
    public static void addProducto(Supermercado supermercado) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "UPDATE usuarios SET favrecetas = ? WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setArray(1, pst.getConnection().createArrayOf(
                    "varchar",
                    supermercado.getProductosId().toArray(new String[0])
            ));

            pst.setString(2, supermercado.getUserId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Producto añadido con éxito: " + supermercado.getUserId());
            }
        } catch (SQLException ex) {
            System.out.println("Error al añadir producto: " + ex.getMessage());
        }
    }

    // devuelve un supermercado de los datos obtenidos de la base de datos
    private static Supermercado mapToSuper(ResultSet rs) throws SQLException {
        if(rs.getInt("puesto")==1) {
            String id = rs.getString("id");
            String name = rs.getString("name");
            String pass = rs.getString("password");

            var favRecetasV = rs.getArray("favrecetas");
            ArrayList<String> favRecetasId = Util.toArrayList(favRecetasV);
            ArrayList<Producto> productos = new ArrayList<>();
            for (String idProducto : favRecetasId) {
                productos.add(ProductoControler.getProductoId(idProducto));
            }
            return new Supermercado(id, name, pass, productos);
        } else{return null;}
    }

    // registra un nuevo supermercado en la base de datos
    public static void registerSupermercado(Supermercado superM) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "INSERT INTO usuarios (id, name, password,favrecetas, puesto) VALUES (?, ?, ?, ?, ?) ";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, superM.getUserId());
            pst.setString(2, superM.getUserName());
            pst.setString(3, superM.getUserPass());

            pst.setArray(4, pst.getConnection().createArrayOf(
                    "varchar",
                    superM.getProductosId().toArray(new String[0])));

            pst.setInt(5,1);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Supermercado insertado con éxito: " + superM.getUserId());
            }
        } catch (SQLException ex) {
            System.out.println("Error al insertar supermercado: " + ex.getMessage());
        }
    }

    // devuelve un supermercado en función a su nombre
    public static Supermercado getSuper(String inputName){
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "SELECT * FROM usuarios WHERE name = ?";
        Supermercado cu;

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, inputName);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    cu = mapToSuper(rs);
                    return cu;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static void updateProducto(Supermercado superM) {
        Connection con=ConnectionDAO.getInstance().getConnection();
        try (PreparedStatement pst = con.prepareStatement("UPDATE usuarios SET favrecetas = ? WHERE id = ?")) {
            pst.setArray(1, pst.getConnection().createArrayOf(
                    "varchar",
                    superM.getProductosId().toArray(new String[0])
            ));
            pst.setString(2, superM.getUserId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Producto editado con éxito: " + superM.getUserId());
            }
        } catch (SQLException ex) {
            System.out.println("Error al editar el producto: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // insertar informacion a mano, por cada ejecución se introduce un nuevo super
        registerSupermercado(new Supermercado("Alcampo", "a123"));
    }
}
