package icai.dtc.isw.controler;

import icai.dtc.isw.dao.SupermercadoDAO;
import icai.dtc.isw.domain.Supermercado;


public class SupermercadoControler {

    // Devuelve un supermercado con el mismo nombre que el introducido
    public static Supermercado loginSupermercado(String user) {
        return SupermercadoDAO.getSuper(user);
    }

    // Actualiza los productos de un supermercado
    public static void updateProducto(Supermercado supermercado){
        SupermercadoDAO.updateProducto(supermercado);
    }

    // Añade un producto a los propios del supermercado
    public static void addProducto(Supermercado supermercado){
        SupermercadoDAO.addProducto(supermercado);
    }
}
