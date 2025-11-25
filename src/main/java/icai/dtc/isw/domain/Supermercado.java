package icai.dtc.isw.domain;

import java.util.ArrayList;

// un supermercado contiene sus productos

public class Supermercado extends Usuario{

    private ArrayList<Producto> productos = new ArrayList<>();

    public Supermercado(){
        super();
        this.productos = new ArrayList<>();
    }

    public Supermercado(String userName, String userPass) {
        super(userName, userPass);
    }

    public Supermercado(String userId, String userName, String userPass,  ArrayList<Producto> productos){
        super(userId,userName,userPass);
        this.productos = productos;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    // devuelve una lista con el Id de los productos de ese supermercado
    public ArrayList<String> getProductosId(){
        ArrayList<String> productoId = new ArrayList<>();
        for(Producto prod : this.productos){
            productoId.add(prod.getId());
        }
        return productoId;
    }

    public int getNumProd(){
        return productos.size();
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    public void anadirProducto(Producto producto){
        this.productos.add(producto);
    }
    public void eliminarProducto(Producto producto){
        this.productos.remove(producto);
    }
}
