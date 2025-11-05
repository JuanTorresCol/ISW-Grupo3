package icai.dtc.isw.domain;

import icai.dtc.isw.utils.Util;

import java.util.HashMap;
import java.util.Map;


public class Receta {
    Util util = new Util();

    private String nombre;
    private double precio;
    private Map<String,Ingrediente> ingredientes;
    private String descripcion;
    private int duracion;
    private Dificultad dificultad;
    private String id;


    private void calcularPrecio() {

        //calcular el precio en función del precio unitario de los productos y la cantidad necesaria de cada uno de ellos
        double calculo = 10;
        // sustituir con la lógica para calcular el precio
        precio = calculo;
    }

    // una receta debe de tener un precio que sera la suma de los alimentos
    // se compone de alimentos
    // tiene un tiempo de preparacion
    // tiene una dificultad

    public Receta(String nombre, Dificultad dificultad, int duracion, String descripcion, Map<String,Ingrediente> ingredientes) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.dificultad = dificultad;
        this.ingredientes = new HashMap<>(ingredientes);
        calcularPrecio();
        this.id = util.createUserId(nombre);
    }
    public Receta(String nombre, Dificultad  dificultad, int duracion, double precio, String descripcion, Map<String,Ingrediente> ingredientes) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.precio = precio;
        this.ingredientes = new HashMap<>(ingredientes);
        this.id = util.createUserId(nombre);
    }
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
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public int getDuracion() {return duracion;}
    public void setDuracion(int duracion) {this.duracion = duracion;}
    public Dificultad getDificultad() {return dificultad;}
    public void setDificultad(Dificultad dificultad) {this.dificultad = dificultad;}
    public Map<String,Ingrediente> getIngredientes() {return ingredientes;}
    public String getId() {return id;}
    public String getNombre() {return nombre;}
    public double getPrecio() {return precio;}

    @Override
    public String toString() {
        return getNombre() + " - " + getDuracion() + " mins - " + getPrecio() + " $ \n"  + getDescripcion();
    }

}
