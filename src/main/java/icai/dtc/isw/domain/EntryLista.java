package icai.dtc.isw.domain;

import icai.dtc.isw.controler.ProductoControler;

public class EntryLista {
    private final String nombre;
    private double precioCompra;
    private double precio;
    private int cantidad;
    private Unidad unidad;

    public EntryLista(String nombre, double precio, int cantidad, Unidad unidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.precioCompra = precio;
    }

    public EntryLista(String nombre, int cantidad) {
        this.nombre = nombre;
        setPrecioyUnidad();
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.precioCompra = this.precio;
    }

    public void otroMas(){
        this.precioCompra = this.precioCompra + this.precio;
        this.cantidad += 1;
    }

    public void unoMenos(){
        this.precioCompra = this.precioCompra - this.precio;
        this.cantidad -= 1;
    }

    public String entradaString(){
        return(String.valueOf(this.cantidad)+ this.unidad +" de "+this.nombre+" a un precio de: "+ String.format("%.2f", this.precioCompra) + "$.");
    }

    public String getNombreEntrada(){
        return this.nombre;
    }

    public double getPrecio(){
        return this.precio;
    }
    public int getCantidad(){
        return this.cantidad;
    }

    public void setPrecioyUnidad(){
        Producto prod = ProductoControler.getProductoName(this.nombre);
        this.precio = prod.getPrecio();
        this.unidad = prod.getUnidadP();
    }
}
