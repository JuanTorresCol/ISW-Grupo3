package icai.dtc.isw.controler;

import icai.dtc.isw.dao.ProductoDAO;
import icai.dtc.isw.domain.Producto;

import java.util.ArrayList;

// clase que conecta el DAO con la GUI
public class ProductoControler {

    // Registra un nuevo producto a la base de datos
    public static boolean registerProducto(Producto producto){
        return ProductoDAO.registerProducto(producto);}

    // Devuelve el producto por el nombre que tiene
    public static Producto getProductoName(String productoName) {
        return ProductoDAO.getProductoName(productoName);
    }

    // Devuelve el producto que coincida con el Id introducido
    public static Producto getProductoId(String productoId){return ProductoDAO.getProductoId(productoId);}

    // Devuelve todos los productos
    public static ArrayList<Producto> getProductos() {
        return ProductoDAO.getProductos();
    }

    // Elimina un producto de la base de datos
    public static void eliminarProducto(Producto producto){
        ProductoDAO.deleteProducto(producto);
    }
}
