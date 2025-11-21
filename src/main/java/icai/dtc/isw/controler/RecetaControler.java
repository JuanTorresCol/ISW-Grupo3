package icai.dtc.isw.controler;

import icai.dtc.isw.dao.RecetaDAO;
import icai.dtc.isw.domain.Receta;

import java.util.ArrayList;

// clase que conecta el DAO de receta a la GUI
public class RecetaControler {

    // devuelve una lista con todas las recetas
    public static ArrayList<Receta> getRecetas(){
        return RecetaDAO.getRecetas();}

    // devuelve todas las recetas con ese ID
    public static Receta getRecetaId(String recetaId){
        return RecetaDAO.getRecetaId(recetaId);
    }

    // devuelve todas las recetas con ese nombre
    public static Receta getRecetaName(String recetaName){
        return RecetaDAO.getRecetaName(recetaName);
    }

    // ingresa una nueva receta a la base de datos
    public static void registerReceta(Receta receta){
        RecetaDAO.registerReceta(receta);
    }
}
