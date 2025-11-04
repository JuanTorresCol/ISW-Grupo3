package icai.dtc.isw.domain;

public class Ingrediente {

    private double cantidad;
    private Unidad unidad;
    private double precio_unitario;

    public Ingrediente(double cantidad, Unidad unidad) {
        this.cantidad = cantidad;
        this.unidad = unidad;
    }
    public double getCantidad() {return cantidad;}
    public void setPrecio_unitario(double precio_unitario) {this.precio_unitario = precio_unitario;}
    public double getPrecio_unitario() {
        return precio_unitario;
    }
    public Unidad getUnidad() {return unidad;}
}
