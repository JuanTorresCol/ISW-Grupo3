package icai.dtc.isw.domain;

import java.util.ArrayList;

public class Supermercado extends Usuario{

    private ArrayList<Producto> productos = new ArrayList<>();

    public Supermercado(){
        super();
        this.productos = new ArrayList<>();
    }

    public Supermercado(String userName, String userPass, ArrayList<Producto> productos) {
        super(userName, userPass);
        this.productos = productos;
    }

    public Supermercado(String userId, String userName, String userPass,  ArrayList<Producto> productos){
        super(userId,userName,userPass);
        this.productos = productos;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public int getNumProd(){
        return productos.size();
    }
    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
    public void anadirProducto(Producto producto){
        productos.add(producto);
    }
    public void eliminarProducto(Producto producto){
        productos.remove(producto);
    }
}
