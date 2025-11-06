package icai.dtc.isw.domain;

import icai.dtc.isw.dao.RecetaDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuDiario {

    private final int size = 2;
    private static double presupuesto;
    private Map<String, Receta> recetas = new HashMap<>(size);
    private RecetaDAO dao = new RecetaDAO();
    // menu diario se compone de dos recetas
    // se usará un algorítmo de knapsack pará encontrar la combinacion de dos recetas que resulta en el mejor menu
    // menu tendra el metodo crear un menu distinto
    // menu tendra el metodo devolver informacion del menu

    public MenuDiario() {}

    public MenuDiario(Receta receta, Receta receta2) {
        recetas.put("comida",receta);
        recetas.put("cena",receta2);
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public ArrayList<Receta> loadRecetas(){
        ArrayList<Receta> recetas = new ArrayList<>();
        return dao.getRecetas();
    }

    public void generateMenu(){
        ArrayList<Receta> recetas;
        recetas = loadRecetas();

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

    public double getprecio(){
        double precio_menu = 0;
        for (Receta r: recetas.values()){
            precio_menu += r.getPrecio();
        }
        return precio_menu;
    }

}
