package icai.dtc.isw.utils;

import icai.dtc.isw.domain.Receta;

import java.lang.reflect.Array;
import java.util.*;

public class Knapsack {

    public static ArrayList<Receta> seleccionar10(ArrayList<Receta> recetas, int presupuesto_cliente) {

        int presupuesto = presupuesto_cliente * 100;
        int n = recetas.size();
        int K = 10;  // NUMERO EXACTO DE RECETAS

        // dp[j][c] = índice de la receta usada para lograr j recetas y costo c
        int[][] dp = new int[K + 1][presupuesto + 1];
        for (int[] fila : dp) Arrays.fill(fila, -1);
        dp[0][0] = -2; // estado inicial válido

        // para reconstruir
        int[][] parentCost = new int[K + 1][presupuesto + 1];

        // procesar recetas
        for (int i = 0; i < n; i++) {
            Receta r = recetas.get(i);

            // recorrer hacia atrás para no sobreescribir
            for (int j = K; j >= 1; j--) {
                for (int c = presupuesto; c >= r.getPrecio(); c--) {
                    if (dp[j - 1][c - r.getPrecioInt()] != -1 && dp[j][c] == -1) {
                        dp[j][c] = i;                        // usar receta i
                        parentCost[j][c] = c - r.getPrecioInt();      // de dónde venimos
                    }
                }
            }
        }

        // buscar el costo válido
        int costoFinal = -1;
        for (int c = presupuesto; c >= 0; c--) {
            if (dp[10][c] != -1) {
                costoFinal = c;
                break;
            }
        }

        if (costoFinal == -1) {
            throw new RuntimeException("No existen 10 recetas dentro del presupuesto.");
        }

        // reconstrucción
        ArrayList<Receta> seleccion = new ArrayList<>();
        int j = K;
        int c = costoFinal;

        while (j > 0) {
            int idx = dp[j][c];
            Receta r = recetas.get(idx);
            seleccion.add(r);
            int prevCost = parentCost[j][c];
            c = prevCost;
            j--;
        }

        return seleccion;
    }
}

