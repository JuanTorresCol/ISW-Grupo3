package icai.dtc.isw.domain;


import java.util.HashMap;
import java.util.Map;

public class Menu {

    private final int size = 2;
    private Map<String, Receta> recetas = new HashMap<>(size);;
    // menu se compone de dos recetas
    // se usará un algorítmo de knapsack pará encontrar la combinacion de dos recetas que resulta en el mejor menu
    // menu tendra el metodo crear un menu distinto
    // menu tendra el metodo devolver informacion del menu

    public Menu(Receta receta, Receta receta2) {
        recetas.put("comida",receta);
        recetas.put("cena",receta2);
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

    public void setComida(Map<String, Receta> recetas,  Receta receta) {
        recetas.put("comida",receta);
    }

    public void setCena(Receta receta) {
        recetas.put("cena",receta);
    }



}
