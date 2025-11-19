package icai.dtc.isw.domain;

import icai.dtc.isw.controler.ProductoControler;
import icai.dtc.isw.utils.Util;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.round;


public class Receta {
    Util util = new Util();

    private final String nombre;
    private double precio;
    private final Map<String,Ingrediente> ingredientes;
    private String descripcion;
    private int duracion;
    private Dificultad dificultad;
    private final String id;
    private final int precioInt = (int) round(precio * 100);

    // calcula el precio de una receta
    private void calcularPrecio() {
        double calculo = 0;
//        for(Ingrediente ingrediente: ingredientes.values()){
//            ProductoControler productoCon = new ProductoControler();
//            Producto producto = productoCon.getProductoName(ingrediente.getNombre());
//            calculo = calculo + producto.getPrecio_unit();
//        }
//        this.precio = Math.round(calculo * 100.0) / 100.0;
    }

    // contructor de receta sin saber su precio
    public Receta(String nombre, Dificultad dificultad, int duracion, String descripcion, Map<String,Ingrediente> ingredientes) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.dificultad = dificultad;
        this.ingredientes = new HashMap<>(ingredientes);
        calcularPrecio();
        this.id = util.createUserId(nombre);
    }

    // constructor de receta sin conocer el ID
    public Receta(String nombre, Dificultad  dificultad, int duracion, double precio, String descripcion, Map<String,Ingrediente> ingredientes) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.precio = precio;
        this.ingredientes = new HashMap<>(ingredientes);
        this.id = util.createUserId(nombre);
    }

    // constructor de receta en funcion a todos sus atributos
    public Receta(String id, String nombre, Dificultad  dificultad, int duracion, double precio, String descripcion, Map<String,Ingrediente> ingredientes) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.precio = precio;
        this.ingredientes = new HashMap<>(ingredientes);
        this.id = id;
    }

    public String getDescripcion() {return descripcion;}
    //public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public int getDuracion() {return duracion;}
    //public void setDuracion(int duracion) {this.duracion = duracion;}
    public Dificultad getDificultad() {return dificultad;}
    //public void setDificultad(Dificultad dificultad) {this.dificultad = dificultad;}
    public Map<String,Ingrediente> getIngredientes() {return ingredientes;}
    public String getId() {return id;}
    public String getNombre() {return nombre;}
    public double getPrecio() {return precio;}
    //public int getPrecioInt() {return precioInt;}

    // imprime en consola los ingredientes que contiene una receta
//     public void printIngredientes() {
//        for (Ingrediente ingrediente : ingredientes.values()) {
//            System.out.println(ingrediente.toString());
//        }
//    }

    @Override
    public String toString() {
        return getNombre() + " - " + getDuracion() + " mins - " + getPrecio() + " $ \n" + "INGREDIENTES:\n" + getIngredientes() + "\nDESCRIPCIÓN: \n" + getDescripcion() + '\n';
    }

//    public boolean esAptaPara(Customer customer) {
//        if (customer == null) return true;
//
//        for (Ingrediente ingrediente : ingredientes.values()) {
//            if (customer.noPuedeConsumir(ingrediente)) {
//                return false;
//            }
//        }
//        return true;
//    }
}
