package icai.dtc.isw.domain;

import icai.dtc.isw.utils.Util;

import java.util.HashMap;
import java.util.Map;

public class Receta {

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
        this.id = Util.createUserId(nombre);
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

    @Override
    public String toString() {
        return getNombre() + " - " + getDuracion() + " mins - " +" \n" + "INGREDIENTES:\n" + getIngredientes() + "\nDESCRIPCIÓN: \n" + getDescripcion() + '\n';
    }

    // devuelve el precio de la lista de la compra
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

    // devuelve la información de los ingredientes en formato string
    public String ingredientesToString() {
        if (ingredientes == null || ingredientes.isEmpty()) {
            return "No hay ingredientes registrados.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Ingredientes:\n");

        for (Ingrediente ing : ingredientes.values()) {
            sb.append("- ")
                    .append(ing.getNombre())
                    .append(": ")
                    .append(ing.getCantidad())
                    .append("\n");
        }

        return sb.toString();
    }

    // formatea la descricpión de una receta para mostrarla en la información de una receta
    public static String formatearDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            return "";
        }

        StringBuilder resultado = new StringBuilder();
        String[] palabras = descripcion.split("\\s+");
        StringBuilder lineaActual = new StringBuilder();

        for (String palabra : palabras) {
            if (lineaActual.length() + palabra.length() + 1 > 50) {
                resultado.append(lineaActual.toString().trim()).append("\n");
                lineaActual.setLength(0); // limpiar línea
            }
            lineaActual.append(palabra).append(" ");
        }
        if (!lineaActual.isEmpty()) {
            resultado.append(lineaActual.toString().trim());
        }

        return resultado.toString();
    }
}
