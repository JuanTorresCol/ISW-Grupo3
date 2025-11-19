package icai.dtc.isw.domain;

public class EntryLista {
    private final String nombre;
    private double precioCompra;
    private final double precio;
    private int cantidad;
    private Unidad unidad;

    public EntryLista(String nombre, double precio, int cantidad, Unidad unidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.precioCompra = precio;
    }

    public void otroMas(){
        this.precioCompra = this.precioCompra + this.precio;
        this.cantidad += 1;
    }

    public String entradaString(){
        return(String.valueOf(this.cantidad)+ this.unidad +" de "+this.nombre+" a un precio de: "+ this.precioCompra + "$.");
    }

    public String getNombreEntrada(){
        return this.nombre;
    }

    public double getPrecio(){
        return this.precio;
    }
}
