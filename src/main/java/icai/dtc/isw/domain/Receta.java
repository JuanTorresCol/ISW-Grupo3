package icai.dtc.isw.domain;

import icai.dtc.isw.utils.Util;

import java.util.HashMap;
import java.util.Map;

public class Receta {
    Util util = new Util();

    private final String nombre;
    private final Map<String,Ingrediente> ingredientes;
    private final String descripcion;
    private final int duracion;
    private final Dificultad dificultad;
    private final String id;

    public Receta(String nombre, Dificultad dificultad, int duracion, String descripcion, Map<String,Ingrediente> ingredientes) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.dificultad = dificultad;
        this.ingredientes = new HashMap<>(ingredientes);
        this.id = util.createUserId(nombre);
    }

    // constructor de receta en funcion a todos sus atributos
    public Receta(String id, String nombre, Dificultad  dificultad, int duracion, String descripcion, Map<String,Ingrediente> ingredientes) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.ingredientes = new HashMap<>(ingredientes);
        this.id = id;
    }

    public String getDescripcion() {return descripcion;}
    public int getDuracion() {return duracion;}
    public Dificultad getDificultad() {return dificultad;}
    public Map<String,Ingrediente> getIngredientes() {return ingredientes;}
    public String getId() {return id;}
    public String getNombre() {return nombre;}

     // imprime en consola los ingredientes que contiene una receta
     public void printIngredientes() {
        for (Ingrediente ingrediente : ingredientes.values()) {
            System.out.println(ingrediente.toString());
        }
    }

    @Override
    public String toString() {
        return getNombre() + " - " + getDuracion() + " mins - " +" \n" + "INGREDIENTES:\n" + getIngredientes() + "\nDESCRIPCIÓN: \n" + getDescripcion() + '\n';
    }

    public double getPrecio(ListaCompra lista){
        double calculo = 0.00;
        for(Ingrediente ing : ingredientes.values()){
            for(EntryLista entry: lista.getEntries()){
                if(ing.getNombre().equals(entry.getNombreEntrada())){
                    calculo = calculo + entry.getPrecio();
                    break;
                }
            }
        }
        return calculo;
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
