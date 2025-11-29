package icai.dtc.isw.domain;

import icai.dtc.isw.controler.ProductoControler;

// clase usada para controlar los distintos productos y sus cantidades a comprar en la lista de la compra
public class EntryLista implements Cloneable {
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

    @Override
    public EntryLista clone() {
        try {
            return (EntryLista) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    // vuelve a aparecer el mismo ingrediente en una receta de la semana/receta añadida
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

    // se elimina una receta y por tanto sus productos de la lista de la compra
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

    // formateo de los datos de la linea de la lista
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
