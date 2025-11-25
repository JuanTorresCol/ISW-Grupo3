package icai.dtc.isw.domain;

import icai.dtc.isw.controler.ProductoControler;
import icai.dtc.isw.controler.RecetaControler;
import icai.dtc.isw.utils.CreaMenus;
import icai.dtc.isw.ui.JVentana;

import java.util.*;

public class MenuSemanal {

    private final int size = 5;
    private static int presupuesto;
    private Map<String, MenuDiario> menus_semana = new HashMap<>(size);

    public MenuSemanal() {
    }
    public MenuSemanal(ArrayList<Receta> s){
        this.menus_semana.put("lunes", new MenuDiario(s.get(0), s.get(1)));
        this.menus_semana.put("martes", new MenuDiario(s.get(2), s.get(3)));
        this.menus_semana.put("miercoles", new MenuDiario(s.get(4), s.get(5)));
        this.menus_semana.put("jueves", new MenuDiario(s.get(6), s.get(7)));
        this.menus_semana.put("viernes", new MenuDiario(s.get(8), s.get(9)));
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

    public ListaCompra generarListaCompra() {
        ListaCompra lista = new ListaCompra();
        ProductoControler productoControler =  new ProductoControler();
        ArrayList<Producto> productos = productoControler.getProductos();
        for(Producto p: productos) {
            for (MenuDiario menuDiario : this.menus_semana.values()) {
                for (Receta receta : menuDiario.getRecetas().values()) {
                    for(Ingrediente ing : receta.getIngredientes().values()) {
                        if (ing.getNombre().equals(p.getNombre())) {
                            boolean flag = true;
                            for (EntryLista entry : lista.getEntries()) {
                                if (entry.getNombreEntrada().equals(p.getNombre())) {
                                    entry.otroMas();
                                    flag = false;
                                }
                            }
                            if(flag) {lista.insertarEntry(new EntryLista(p.getNombre(), p.getPrecio(), 1, p.getUnidadP()));}
                        }
                    }
                }
            }
        }
        return lista;
    }

    public ListaCompra updateLista(Receta recetaVieja, Receta recetaNueva, ListaCompra listaCompra) {
        if (recetaVieja != null) {
            for (Ingrediente ing : recetaVieja.getIngredientes().values()) {
                Iterator<EntryLista> it = listaCompra.getEntries().iterator();
                while (it.hasNext()) {
                    EntryLista entry = it.next();
                    if (entry.getNombreEntrada().equals(ing.getNombre())) {
                        if (entry.getCantidad() == 1) {
                            it.remove();
                        } else {
                            entry.unoMenos();
                        }
                        break;
                    }
                }
            }
        }

        if (recetaNueva != null) {
            for (Ingrediente ing : recetaNueva.getIngredientes().values()) {
                EntryLista encontrada = null;
                for (EntryLista entry : listaCompra.getEntries()) {
                    if (entry.getNombreEntrada().equals(ing.getNombre())) {
                        encontrada = entry;
                        break;
                    }
                }

                if (encontrada == null) {
                    listaCompra.insertarEntry(
                            new EntryLista(ing.getNombre(), 1));
                } else {
                    encontrada.otroMas();
                }
            }
        }

        return listaCompra;
    }

    @Override
    public String toString() {
        for (MenuDiario menu : menus_semana.values()){
             return menu.toString();
        }
        return "";
    }

    public ArrayList<Receta> getRecetasSimilares(ArrayList<Receta> recetas, Customer customer, JVentana app){
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
                if(menuDia.getPrecioMenu(app.getLista())< recetaTry.getPrecio(app.getLista())*2){
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

    public Receta cambioReceta(Receta nueva, String bloque, int dia) {
        Receta anterior = null;

        if (nueva != null) {
            if ("Comida".equals(bloque)) {
                if (dia == 0) {
                    anterior = getLunes().getComida();
                    getLunes().setComida(nueva);
                } else if (dia == 1) {
                    anterior = getMartes().getComida();
                    getMartes().setComida(nueva);
                } else if (dia == 2) {
                    anterior = getMiercoles().getComida();
                    getMiercoles().setComida(nueva);
                } else if (dia == 3) {
                    anterior = getJueves().getComida();
                    getJueves().setComida(nueva);
                } else {
                    anterior = getViernes().getComida();
                    getViernes().setComida(nueva);
                }
            } else if ("Cena".equals(bloque)) {
                if (dia == 0) {
                    anterior = getLunes().getCena();
                    getLunes().setCena(nueva);
                } else if (dia == 1) {
                    anterior = getMartes().getCena();
                    getMartes().setCena(nueva);
                } else if (dia == 2) {
                    anterior = getMiercoles().getCena();
                    getMiercoles().setCena(nueva);
                } else if (dia == 3) {
                    anterior = getJueves().getCena();
                    getJueves().setCena(nueva);
                } else {
                    anterior = getViernes().getCena();
                    getViernes().setCena(nueva);
                }
            }
        }

        return anterior;
    }

    public ArrayList<String> getRecetasId(){
        ArrayList<String> recetasId = new ArrayList<>();
        recetasId.addAll(menus_semana.get("lunes").getRecetasId());
        recetasId.addAll(menus_semana.get("martes").getRecetasId());
        recetasId.addAll(menus_semana.get("miercoles").getRecetasId());
        recetasId.addAll(menus_semana.get("jueves").getRecetasId());
        recetasId.addAll(menus_semana.get("viernes").getRecetasId());
        return recetasId;
    }

}
