package icai.dtc.isw.domain;

import icai.dtc.isw.controler.ProductoControler;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Lista {

    private int semana;
    private ArrayList<Producto> productos;

    public Lista(int semana, MenuSemanal menu) {
        this.semana = semana;
        rellenarProductos(menu);
    }

    // rellena la lista de los productos para el menu de esa semana
    public void rellenarProductos(MenuSemanal menu) {
        productos = new ArrayList<>();
        if (menu != null){
            Map<String, MenuDiario> menus = menu.getMenuSemanal();
            for (MenuDiario menuD : menus.values()) {
                if(menuD != null) {
                    Collection<Receta> recetas = menuD.getRecetas().values();

                    for (Receta receta : recetas) {
                        Map<String, Ingrediente> ingredienteMap = receta.getIngredientes();
                        for (String nombre : ingredienteMap.keySet()) {
                            Producto producto = ProductoControler.getProductoName(nombre);
                            if (comprobarSiExiste(producto, productos)) {
                                productos.add(producto);
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList<String> visualizarProductos(){
        DecimalFormat df = new DecimalFormat("#,##0.00");
        ArrayList cadena = new ArrayList<>();
        cadena.add("Lista de la compra:");
        for(Producto prod : this.productos){
            cadena.add(String.valueOf(df.format(prod.getCantidad()))+prod.getUnidadP()+" "+prod.getNombre() + " a un precio de "+ df.format(prod.getPrecio_unit()) +"$");
        }
        return cadena;
    }
    private boolean comprobarSiExiste(Producto producto, ArrayList<Producto> productos){
        boolean f = true;
        for(Producto productoCheck : productos){
            if(productoCheck.getNombre().equals(producto.getNombre())){
                f = false;
                break;
            }
        }
        return f;
    }

    public ArrayList<Producto> getProductos() { return productos; }

}
