package icai.dtc.isw.domain;

import icai.dtc.isw.dao.ProductoDAO;

import java.util.ArrayList;
import java.util.Map;

public class Lista {

    private int semana;
    private ArrayList<Producto> productos;

    public Lista(int semana) {
        this.semana = semana;
        rellenarProductos();
    }

    // rellena la lista de los productos para el menu de esa semana
    public void rellenarProductos() {
        productos = new ArrayList<>();
        MenuSemanal menu = new MenuSemanal();
        ArrayList<Receta> recetas = menu.getRecetasMenu();
        for(Receta receta: recetas){
            Map<String, Ingrediente> ingredienteMap = receta.getIngredientes();
            for(String nombre : ingredienteMap.keySet()){
                Producto producto = ProductoDAO.getProductoName(nombre);
                if(productos.contains(producto)){
                    productos.get(productos.indexOf(producto)).repiteProd();
                }   else{productos.add(producto);}
            }
        }
    }

    public ArrayList<Producto> getProductos() {
        return this.productos;
    }
}
