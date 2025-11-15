package icai.dtc.isw.domain;

import icai.dtc.isw.controler.RecetaControler;
import icai.dtc.isw.utils.Knapsack;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuSemanal {

    private final int size = 5;
    private static int presupuesto;
    private Map<String, MenuDiario> menus_semana = new HashMap<>(size);

    public MenuSemanal() {
    }

    public Map<String,MenuDiario> getMenuSemanal() {return menus_semana;}
    public double getPresupuesto() {return presupuesto;}
    public void setPresupuesto(int presupuesto) {this.presupuesto = presupuesto;}
    public MenuDiario getLunes() {return menus_semana.get("lunes");}
    public MenuDiario getMartes() {return menus_semana.get("martes");}
    public MenuDiario getMiercoles() {return menus_semana.get("miercoles");}
    public MenuDiario getJueves() {return menus_semana.get("jueves");}
    public MenuDiario getViernes() {return menus_semana.get("viernes");}

    // rellena la semana con recetas, dos por dia
    public void generarMenu() {
        ArrayList<Receta> recetas = RecetaControler.getRecetas();
        //ArrayList<Receta> s= Knapsack.selecciona10(recetas,presupuesto);
        ArrayList<Receta> s = Knapsack.prueba(recetas);
        this.menus_semana.put("lunes",new MenuDiario(s.get(0),s.get(1)));
        this.menus_semana.put("martes",new MenuDiario(s.get(2),s.get(3)));
        this.menus_semana.put("miercoles", new MenuDiario(s.get(4),s.get(5)));
        this.menus_semana.put("jueves", new MenuDiario(s.get(6),s.get(7)));
        this.menus_semana.put("viernes",new MenuDiario(s.get(8),s.get(9)));
    }

    @Override
    public String toString() {
        for (MenuDiario menu : menus_semana.values()){
             return menu.toString();
        }
        return "";
    }
}
