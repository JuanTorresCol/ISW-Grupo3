package icai.dtc.isw.domain;

import icai.dtc.isw.controler.ProductoControler;

public class EntryLista {
    private final String nombre;
    private double precioCompra;
    private double precio;
    private int cantidad;
    private Unidad unidad;
    private int cantidadUsed = 0;

    public EntryLista(String nombre, double precio, int cantidad, Unidad unidad, int cantidadUsed) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.precioCompra = precio;
        this.cantidadUsed = cantidadUsed;
    }

    public EntryLista(String nombre, int cantidad) {
        this.nombre = nombre;
        setPrecioyUnidad();
        this.cantidad = cantidad;
        this.precioCompra = this.precio;
    }

    public void otroMas(int cantidadRequired){
        if(this.unidad.equals(Unidad.u)){
            this.precioCompra = this.precioCompra + this.precio;
            this.cantidad += 1;
            this.cantidadUsed += 1;
        } else if(this.cantidadUsed+cantidadRequired>1000){
            this.precioCompra = this.precioCompra + this.precio;
            this.cantidad += 1;
            this.cantidadUsed = this.cantidadUsed + cantidadRequired - 1000;
        } else{
            this.cantidadUsed += cantidadRequired;
        }
    }

    public void unoMenos(int cantidadRequired){
        if(this.unidad.equals(Unidad.u)){
            this.precioCompra = this.precioCompra - this.precio;
            this.cantidad -= 1;
            this.cantidadUsed -= 1;
        } else if(this.cantidadUsed+cantidadRequired<0){
            this.precioCompra = this.precioCompra - this.precio;
            this.cantidad -= 1;
            this.cantidadUsed = this.cantidadUsed + cantidadRequired + 1000;
        } else{
            this.cantidadUsed -= cantidadRequired;
        }
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
