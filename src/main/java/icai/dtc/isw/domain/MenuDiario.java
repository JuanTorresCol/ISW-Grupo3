package icai.dtc.isw.domain;

import java.util.HashMap;
import java.util.Map;

public class MenuDiario {

    private final int size = 2;
    private double precio;
    private Map<String, Receta> recetas = new HashMap<>(size);

    public MenuDiario(Receta receta, Receta receta2) {
        recetas.put("comida",receta);
        recetas.put("cena",receta2);
        this.precio= 10;
//        this.precio=receta.getPrecio() + receta2.getPrecio();
    }

    public Map<String,Receta> getRecetas() {
        return recetas;
    }

    public Receta getComida() {
        return recetas.get("comida");
    }

    public Receta getCena() {
        return recetas.get("cena");
    }

    public void setComida(Receta receta) {
        this.recetas.put("comida",receta);
    }

    public void setCena(Receta receta) {
        this.recetas.put("cena",receta);
    }

    public double getprecio(){
        return this.precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        for (Receta r: recetas.values()){
            return r.toString();
        }
        return "";
    }

}
