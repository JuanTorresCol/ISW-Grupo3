package icai.dtc.isw.domain;

import icai.dtc.isw.controler.RecetaControler;
import icai.dtc.isw.utils.CreaMenus;
import icai.dtc.isw.ui.JVentana;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MenuSemanal {

    private final int size = 5;
    private static int presupuesto;
    private Map<String, MenuDiario> menus_semana = new HashMap<>(size);

    public MenuSemanal() {
    }

    public Map<String,MenuDiario> getMenuSemanal() {return menus_semana;}
    // public double getPresupuesto() {return presupuesto;}
    public void setPresupuesto(int presupuesto) {this.presupuesto = presupuesto;}
    public MenuDiario getLunes() {return menus_semana.get("lunes");}
    public MenuDiario getMartes() {return menus_semana.get("martes");}
    public MenuDiario getMiercoles() {return menus_semana.get("miercoles");}
    public MenuDiario getJueves() {return menus_semana.get("jueves");}
    public MenuDiario getViernes() {return menus_semana.get("viernes");}

    // rellena la semana con recetas, dos por dia
    public void generarMenu(JVentana app) {
        ArrayList<Receta> recetas = RecetaControler.getRecetas();
        // genera el menu semanal, null si no se puede
        ArrayList<Receta> s = CreaMenus.creaMenuRes(recetas, presupuesto, app);
        if(s!= null) {
            this.menus_semana.put("lunes", new MenuDiario(s.get(0), s.get(1)));
            this.menus_semana.put("martes", new MenuDiario(s.get(2), s.get(3)));
            this.menus_semana.put("miercoles", new MenuDiario(s.get(4), s.get(5)));
            this.menus_semana.put("jueves", new MenuDiario(s.get(6), s.get(7)));
            this.menus_semana.put("viernes", new MenuDiario(s.get(8), s.get(9)));
        } else{
            this.menus_semana.put("lunes", null);
        }
    }

    public ArrayList<Receta> getRecetasMenu() {
        ArrayList<Receta> recetas = new ArrayList<>();
        for(MenuDiario dia :menus_semana.values()){
            recetas.add(dia.getComida());
            recetas.add(dia.getCena());
        }
        return recetas;
    }

    @Override
    public String toString() {
        for (MenuDiario menu : menus_semana.values()){
             return menu.toString();
        }
        return "";
    }

    public ArrayList<Receta> getRecetasSimilares(ArrayList<Receta> recetas, Customer customer){
        ArrayList<Receta> recetasSimilares = new ArrayList<>();
        Collections.shuffle(recetas);
        for(Receta recetaTry : recetas){
            if(recetasSimilares.size()>=4){break;}
            boolean flag = true;
            for(MenuDiario menuDia :menus_semana.values()){
                if(menuDia.getRecetas().values().contains(recetaTry)){
                    flag = false;
                    break;
                }
                if(menuDia.getComida().getPrecio()< recetaTry.getPrecio()|menuDia.getCena().getPrecio()<recetaTry.getPrecio()){
                    flag = false;
                    break;
                }
            }
            for(Ingrediente ingTry :recetaTry.getIngredientes().values()){
                if(customer.getIllegalFood().contains(ingTry.getNombre())){
                    flag=false;
                    break;
                }
            }

            if(flag){
                recetasSimilares.add(recetaTry);
            }
        }
        return recetasSimilares;
    }
    public void cambioReceta(Receta receta, String bloque, int dia){
        if(receta!=null) {
            if (bloque.equals("Comida")) {
                if (dia == 0) {
                    getLunes().setComida(receta);
                } else if (dia == 1) {
                    getMartes().setComida(receta);
                } else if (dia == 2) {
                    getMiercoles().setComida(receta);
                } else if (dia == 3) {
                    getJueves().setComida(receta);
                } else {
                    getViernes().setComida(receta);
                }
            } else if (bloque.equals("Cena")) {
                if (dia == 0) {
                    getLunes().setCena(receta);
                } else if (dia == 1) {
                    getMartes().setCena(receta);
                } else if (dia == 2) {
                    getMiercoles().setCena(receta);
                } else if (dia == 3) {
                    getJueves().setCena(receta);
                } else {
                    getViernes().setCena(receta);
                }
            }
        }
    }
}
