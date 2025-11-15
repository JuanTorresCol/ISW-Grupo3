package icai.dtc.isw.utils;

import icai.dtc.isw.domain.Receta;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CreaMenus {

    // devuelve las diez recetas que mas se ajustan a un presupuesto, en el caso de que no se pueda devuelve null
    public static ArrayList<Receta> creaMenuOptimo(ArrayList<Receta> recetas, int presupuestoEuros) {
        int n = recetas.size();
        int B = presupuestoEuros * 100;

        int[] precio = new int[n];
        for (int i = 0; i < n; i++) {
            precio[i] = (int) Math.round(recetas.get(i).getPrecio() * 100.0);
        }

        List<Integer> baratos = new ArrayList<>();
        for (int p : precio) baratos.add(p);
        baratos.sort(Integer::compare);
        if (baratos.size() < 10) return null;
        long suma10 = 0;
        for (int i = 0; i < 10; i++) suma10 += baratos.get(i);
        if (suma10 > B) return null;

        int K = 10;
        int[][] dp = new int[K + 1][B + 1];
        for (int k = 0; k <= K; k++) {
            Arrays.fill(dp[k], -1);
        }
        dp[0][0] = 0;

        int[][] prevS = new int[K + 1][B + 1];
        int[][] prevIdx = new int[K + 1][B + 1];
        for (int k = 0; k <= K; k++) {
            Arrays.fill(prevS[k], -1);
            Arrays.fill(prevIdx[k], -1);
        }

        for (int i = 0; i < n; i++) {
            int w = precio[i];
            for (int k = K; k >= 1; k--) {
                for (int s = B; s >= w; s--) {
                    if (dp[k - 1][s - w] != -1) {
                        int cand = s;
                        if (cand > dp[k][s]) {
                            dp[k][s] = cand;
                            prevS[k][s] = s - w;
                            prevIdx[k][s] = i;
                        }
                    }
                }
            }
        }

        int bestS = -1;
        for (int s = B; s >= 0; s--) {
            if (dp[K][s] != -1) { bestS = s; break; }
        }
        if (bestS == -1) return null;

        ArrayList<Receta> resultado = new ArrayList<>();
        int k = K, s = bestS;
        while (k > 0) {
            int idx = prevIdx[k][s];
            if (idx < 0) break;
            resultado.add(recetas.get(idx));
            s = prevS[k][s];
            k--;
        }
        return resultado.size() == 10 ? resultado : null;
    }

    // metodo de prueba de interfaz
    public static ArrayList<Receta> prueba(ArrayList<Receta> lista){
        return new ArrayList<>(lista.subList(0,10));

    }

}

