package icai.dtc.isw.domain;


import java.util.ArrayList;

public class Producto {

    private String nombre;
    private double precio_unitario;
    private ArrayList<String> illegal_food = new ArrayList<>();

    // un producto va a tener su precio unitario
    // tiene alérgenos
    //

    public Producto(String nombre, double precio_unitario, ArrayList<String> illegal_food) {
        this.nombre = nombre;
        this.precio_unitario = precio_unitario;
        this.illegal_food = illegal_food;
    }

    public Producto(String nombre) {
        this.nombre = nombre;

    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public ArrayList<String> getIllegal_food() {
        return illegal_food;
    }

    public void setIllegal_food(ArrayList<String> illegal_food) {
        this.illegal_food = illegal_food;
    }

}
