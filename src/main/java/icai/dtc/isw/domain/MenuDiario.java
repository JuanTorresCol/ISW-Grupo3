package icai.dtc.isw.domain;

import java.util.HashMap;
import java.util.Map;

public class MenuDiario {

    private final int size = 2;
    private Map<String, Receta> recetas = new HashMap<>(size);

    public MenuDiario(Receta receta, Receta receta2) {
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

    public void setComida(Receta receta) {
        this.recetas.put("comida",receta);
    }

    public void setCena(Receta receta) {
        this.recetas.put("cena",receta);
    }

    public double getPrecioMenu(ListaCompra lista){
        double calculo = 0;
        for (Receta receta : recetas.values()){
            String nombre = receta.getNombre();
            for(EntryLista entry : lista.getEntries()){
                if(entry.getNombreEntrada().equals(nombre)){
                    calculo+=entry.getPrecio();
                    break;
                }
            }
        }
        return calculo;
    }


    @Override
    public String toString() {
        for (Receta r: recetas.values()){
            return r.toString();
        }
        return "";
    }

}
