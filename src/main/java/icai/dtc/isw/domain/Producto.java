package icai.dtc.isw.domain;

import icai.dtc.isw.utils.Util;

public class Producto {
    private final String codigo;
    private final String nombre;
    private final Unidad unidad;
    private final double precio;

    public Producto(String nombre, Unidad unidad, double precio) {
        this.nombre = nombre;
        this.unidad = unidad;
        this.precio = precio;
        this.codigo = Util.createUserId(nombre);
    }

    // Constructor para el producto a base del ingrediente del que proviene
    public Producto(Ingrediente ingrediente, double precio, Unidad unidad) {
        this.nombre = ingrediente.getNombre();
        this.unidad = unidad;
        this.precio = precio;
        this.codigo = Util.createUserId(ingrediente.getNombre());
    }

    // Constructor default producto
    public Producto(){
        this.codigo = "1";
        this.unidad = null;
        this.precio = 0;
        this.nombre = "a";
    }

    // Constructor para el producto cuando viene de la base de datos
    public Producto(String codigo, String nombre, Unidad unidad, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.unidad = unidad;
        this.precio = precio;
    }

    public String getId() {
        return this.codigo;
    }
    public String getNombre() {
        return this.nombre;
    }
    public Unidad getUnidadP() {
        return this.unidad;
    }
    public double getPrecio() {
        return this.precio;
    }

    @Override
    public String toString() {
        return getNombre() + ": " + getUnidadP() + " a un precio de "+ getPrecio();
    }

}
