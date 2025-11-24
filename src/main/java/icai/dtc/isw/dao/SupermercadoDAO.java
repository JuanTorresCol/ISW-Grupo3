package icai.dtc.isw.dao;

import icai.dtc.isw.controler.ProductoControler;
import icai.dtc.isw.controler.RecetaControler;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.domain.Producto;
import icai.dtc.isw.domain.Supermercado;
import icai.dtc.isw.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupermercadoDAO {

    private static final Util util = new Util();

    private static String listToString(ArrayList<String> lista) {
        if (lista == null || lista.isEmpty()) return "";
        return String.join(",", lista);
    }


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

    private static Supermercado mapToSuper(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        String pass = rs.getString("password");

        var favRecetasV = rs.getArray("favrecetas");
        ArrayList<String> favRecetasId = util.toArrayList(favRecetasV);
        ArrayList<Producto> productos = new ArrayList<>();
        for(String idProducto: favRecetasId){
            productos.add(ProductoControler.getProductoId(idProducto));
        }

        return new Supermercado(id, name, pass,productos);
    }

    public static void registerSupermercado(Supermercado superM) {
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "INSERT INTO usuarios (id, name, password,favrecetas, puesto) VALUES (?, ?, ?, ?, ?) " +
                "VALUES (?,?,?,?,?)";

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

    public static ArrayList<Supermercado> getSupermercados() {
        ArrayList<Supermercado> listaSupermercados = new ArrayList<>();
        Connection con = ConnectionDAO.getInstance().getConnection();
        String sql = "SELECT * FROM usuarios";

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Supermercado c = mapToSuper(rs);
                listaSupermercados.add(c);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return listaSupermercados;
    }

//    public static void main(String[] args) {
//        // insertar informacion a mano, por cada ejecución se introduce un nuevo super
//        registerSupermercado(new Supermercado());
//    }
}
