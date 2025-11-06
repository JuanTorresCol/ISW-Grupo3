package icai.dtc.isw.domain;

import icai.dtc.isw.dao.RecetaDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuDiario {

    private final int size = 2;
    private static double precio;
    private Map<String, Receta> recetas = new HashMap<>(size);
    private RecetaDAO dao = new RecetaDAO();

    public MenuDiario() {}

    public MenuDiario(Receta receta, Receta receta2) {
        recetas.put("comida",receta);
        recetas.put("cena",receta2);
    }

    public void setPresupuesto(double precio) {
        this.precio = precio;
    }

    public ArrayList<Receta> loadRecetas(){
        ArrayList<Receta> recetas = new ArrayList<>();
        return dao.getRecetas();
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
