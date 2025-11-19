package icai.dtc.isw.controler;

import icai.dtc.isw.dao.ProductoDAO;
import icai.dtc.isw.domain.Producto;

import java.util.ArrayList;

// clase que conecta el DAO con la GUI
public class ProductoControler {

    static ProductoDAO productoDAO = new ProductoDAO();

    // Registra un nuevo producto a la base de datos
    public static void registerProducto(Producto producto){
        productoDAO.registerProducto(producto);}

    // Devuelve el producto por el nombre que tiene
    public static Producto getProductoName(String productoName) {
        return productoDAO.getProductoName(productoName);
    }

    public static ArrayList<Producto> getProductos() {
        return productoDAO.getProductos();
    }
}
