package icai.dtc.isw.domain;

public class Ingrediente {

    private String nombre;
    private String cantidad;
    private Unidad unidad;
    private double precio_unitario;

    public Ingrediente(String nombre, String cantidad, double precio_unitario) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;

    }

    public String getNombre() {return nombre;}
    public String getCantidad() {return cantidad;}
    public void setPrecio_unitario(double precio_unitario) {this.precio_unitario = precio_unitario;}
    public double getPrecio_unitario() {
        return precio_unitario;
    }
    public Unidad getUnidad() {return unidad;}

    @Override
    public String toString()
    {
        return getNombre() + " --> " + getCantidad() + " " + getUnidad();
    }

}
