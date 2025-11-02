package icai.dtc.isw.domain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Receta {

    private String nombre;
    private double precio;
    private Map<Producto,Integer> ingredientes;
    private String descripcion;
    private int duracion;
    private Dificultad dificultad;


    private void calcularPrecio() {

        //calcular el precio en función del precio unitario de los productos y la cantidad necesaria de cada uno de ellos
        double calculo = 0;
        for (Producto p : ingredientes.keySet()) {
            calculo += p.getPrecio_unitario() * ingredientes.get(p);
        }
        // sustituir con la lógica para calcular el precio
        precio = calculo;
    }

    // una receta debe de tener un precio que sera la suma de los alimentos
    // se compone de alimentos
    // tiene un tiempo de preparacion
    // tiene una dificultad

    public Receta(String nombre, String descripcion, int duracion, Dificultad dificultad, Map<Producto,Integer> ingredientes) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.ingredientes = new HashMap<>(ingredientes);
        calcularPrecio();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Dificultad getDificultad() {
        return dificultad;
    }

    public void setDificultad(Dificultad dificultad) {
        this.dificultad = dificultad;
    }

    public List<Producto> getIngredientes() {
        return new ArrayList<>(ingredientes.keySet());
    }



    public double getPrecio() {
        return precio;
    }

}
