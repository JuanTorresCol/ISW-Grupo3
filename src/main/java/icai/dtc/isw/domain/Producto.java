package icai.dtc.isw.domain;

import icai.dtc.isw.dao.ProductoDAO;
import icai.dtc.isw.dao.RecetaDAO;
import icai.dtc.isw.utils.Util;

import java.util.ArrayList;
import java.util.Map;

public class Producto {
    private String codigo;
    private String nombre;
    private int cantidad;
    private Unidad unidad;
    private double precio_unit;

    private Util util = new Util();

    // Constructor para el producto a base del ingrediente del que proviene
    public Producto(Ingrediente ingrediente) {
        this.nombre = ingrediente.getNombre();
        calcCantidad(ingrediente);
        this.codigo = util.createUserId(ingrediente.getNombre());
    }

    // Constructor default producto
    public Producto(){
        this.codigo = "1";
        this.cantidad = 0;
        this.unidad = null;
        this.precio_unit = 0;
        this.nombre = "a";
    }

    // Constructor para el producto cuando viene de la base de datos
    public Producto(String codigo, String nombre, int cantidad, Unidad unidad, double precio_unit) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.precio_unit = precio_unit;
    }

    // Calculo de la cantidad y unidad del producto asi como de su precio unitario
    public void calcCantidad(Ingrediente ingrediente){
        String cantidad;
        cantidad = ingrediente.getCantidad();
        if(cantidad.contains("u")){
            this.unidad = Unidad.u;
            this.cantidad = Integer.parseInt(cantidad.replace("u",""));
            this.precio_unit = ingrediente.getPrecio_unitario()/this.cantidad;
        } else if (cantidad.contains("ml")) {
            this.unidad = Unidad.ml;
            this.cantidad = 1;
            this.precio_unit=ingrediente.getPrecio_unitario();
        } else if (cantidad.contains("l")) {
            this.unidad = Unidad.l;
            this.cantidad = Integer.parseInt(cantidad.replace("l",""));
            this.precio_unit=ingrediente.getPrecio_unitario()/this.cantidad;
        } else if (cantidad.contains("g")) {
            this.unidad = Unidad.g;
            this.cantidad = 1;
            this.precio_unit=ingrediente.getPrecio_unitario();
        } else if (cantidad.contains("kg")) {
            this.unidad = Unidad.kg;
            this.cantidad = Integer.parseInt(cantidad.replace("kg",""));
            this.precio_unit=ingrediente.getPrecio_unitario()/this.cantidad;
        } else if (cantidad.contains("cda")) {
            this.unidad = Unidad.cda;
            this.cantidad = Integer.parseInt(cantidad.replace("cda",""));
            this.precio_unit = ingrediente.getPrecio_unitario()/this.cantidad;
        } else if (cantidad.contains("cdta")) {
            this.unidad = Unidad.cdta;
            this.cantidad = Integer.parseInt(cantidad.replace("cdta",""));
            this.precio_unit = ingrediente.getPrecio_unitario()/this.cantidad;
        } else{
            this.unidad = Unidad.u;
            this.cantidad = 1;
            this.precio_unit = 1.00;
        }
    }

    public int getCantidad() {
        return cantidad;
    }
    public String getId() {
        return codigo;
    }
    public String getNombre() {
        return nombre;
    }
    public Unidad getUnidadP() {
        return unidad;
    }
    public double getPrecio_unit() {
        return precio_unit;
    }

    // metodo para registrar todos los productos que no aparecen ya en la base de datos
    public static void registerAllProductos(){
        ProductoDAO productoDAO = new ProductoDAO();
        RecetaDAO recetaDAO = new RecetaDAO();

        ArrayList<Receta> recetas;
        recetas = recetaDAO.getRecetas();
        for (Receta receta : recetas) {
            Map<String,Ingrediente> ingredientes = receta.getIngredientes();;
            for (Ingrediente ingrediente : ingredientes.values()) {
                Producto test = productoDAO.getProductoName(ingrediente.getNombre());
                if (test==(null)) {
                    productoDAO.registerProducto(new Producto(ingrediente));
                }
            }
        }
    }

    // rellena la base de datos de productos
    // solo ejecutar una vez o despues de hacer un truncate a la db
    public static void main(String[] args) {
        registerAllProductos();
    }
}