package icai.dtc.isw.domain;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MenuSemanal {


    private final int size = 5;
    private double presupuesto;
    private Map<String, MenuDiario> menus_semana = new HashMap<>(size);

    // menu semanal esta compuesto por 5 menus diarios (lunes,martes,miercoles,jueves y viernes)
    // menu semanal es el que tiene que cumplir que el presupuesto establecido por el cliente

    public MenuSemanal() {

    }

    public MenuSemanal(MenuDiario lunes, MenuDiario martes, MenuDiario miercoles, MenuDiario jueves, MenuDiario viernes) {

        menus_semana.put("lunes", lunes);
        menus_semana.put("martes", martes);
        menus_semana.put("miercoles", miercoles);
        menus_semana.put("jueves", jueves);
        menus_semana.put("viernes", viernes);

    }

    public Map<String,MenuDiario> getMenuSemanal() {return menus_semana;}
    public double getPresupuesto() {return presupuesto;}
    public void setPresupuesto(double presupuesto) {this.presupuesto = presupuesto;}
    public MenuDiario getLunes() {return menus_semana.get("lunes");}
    public MenuDiario getMartes() {return menus_semana.get("martes");}
    public MenuDiario getMiercoles() {return menus_semana.get("miercoles");}
    public MenuDiario getJueves() {return menus_semana.get("jueves");}
    public MenuDiario getViernes() {return menus_semana.get("viernes");}
}
