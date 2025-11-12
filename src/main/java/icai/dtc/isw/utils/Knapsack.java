package icai.dtc.isw.utils;

import icai.dtc.isw.domain.Receta;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Knapsack {

    public static ArrayList<Receta> selecciona10(ArrayList<Receta> lista,int presupuesto){

        ArrayList<Receta> copia;
        while(true) {
            if (lista == null || lista.size() < 10)
                throw new IllegalArgumentException("Se necesitan al menos 10 elementos.");
            copia = new ArrayList<>(lista);
            Collections.shuffle(copia, ThreadLocalRandom.current());
            copia = new ArrayList<>(copia.subList(0, 10));
            double suma = 0.00;
            for(Receta receta: copia){
                suma += receta.getPrecio();
            }
            if(suma <= presupuesto){
                break;
            }
        } return copia;
    }

    public static ArrayList<Receta> prueba(ArrayList<Receta> lista){
        return new ArrayList<>(lista.subList(0,10));

    }

}

