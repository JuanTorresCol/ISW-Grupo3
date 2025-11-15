package icai.dtc.isw.utils;

import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.ui.JVentana;

import java.util.*;
import java.util.stream.Collectors;

public class CreaMenus {

    // compruebas si se contiene un ingrediente de la receta en la lista de no deseados
    private static boolean contieneAlguno(Collection<String> ingredientes, Collection<String> lista) {
        if (ingredientes == null || ingredientes.isEmpty() || lista == null || lista.isEmpty()) return false;
        Set<String> set = lista.stream().map(String::toLowerCase).collect(Collectors.toSet());
        for (String ing : ingredientes) {
            if (ing != null && set.contains(ing.toLowerCase())) return true;
        }
        return false;
    }

    // devuelve las diez recetas que mas se ajustan a un presupuesto / alergias / preferencias, en el caso de que no se pueda devuelve null
    public static ArrayList<Receta> creaMenuRes(ArrayList<Receta> recetas, int presupuestoEuros, JVentana app) {
        List<String> illegalFood = app.getUsuario().getIllegalFood();
        List<String> noCome = app.getUsuario().getAlimentosNoCome();

        final int K = 10;
        final int B = presupuestoEuros * 100;
        final int INF = 1_000_000;

        List<Receta> legales = new ArrayList<>();
        for (Receta r : recetas) {
            Collection<String> ings = r.getIngredientes().keySet();
            if (!contieneAlguno(ings, illegalFood)) {
                legales.add(r);
            }
        }
        if (legales.size() < K) return null;

        int n = legales.size();
        int[] precio = new int[n];
        boolean[] esNoCome = new boolean[n];

        for (int i = 0; i < n; i++) {
            Receta r = legales.get(i);
            precio[i] = (int) Math.round(r.getPrecio() * 100.0);
            esNoCome[i] = contieneAlguno(r.getIngredientes().keySet(), noCome);
        }

        List<Integer> preciosOrdenados = Arrays.stream(precio).boxed().sorted().collect(Collectors.toList());
        long suma10 = 0;
        for (int i = 0; i < Math.min(K, preciosOrdenados.size()); i++) suma10 += preciosOrdenados.get(i);
        if (suma10 > B) return null;

        int[][] dp = new int[K + 1][B + 1];
        for (int k = 0; k <= K; k++) Arrays.fill(dp[k], INF);
        dp[0][0] = 0;

        int[][] prevS = new int[K + 1][B + 1];
        int[][] prevIdx = new int[K + 1][B + 1];
        for (int k = 0; k <= K; k++) {
            Arrays.fill(prevS[k], -1);
            Arrays.fill(prevIdx[k], -1);
        }

        for (int i = 0; i < n; i++) {
            int w = precio[i];
            int bad = esNoCome[i] ? 1 : 0;

            for (int k = K; k >= 1; k--) {
                for (int s = B; s >= w; s--) {
                    if (dp[k - 1][s - w] != INF) {
                        int candBad = dp[k - 1][s - w] + bad;
                        if (candBad < dp[k][s]) {
                            dp[k][s] = candBad;
                            prevS[k][s] = s - w;
                            prevIdx[k][s] = i;
                        }
                    }
                }
            }
        }
        int minBad = INF;
        for (int s = 0; s <= B; s++) {
            if (dp[K][s] < minBad) minBad = dp[K][s];
        }
        if (minBad == INF) return null;

        int bestS = -1;
        for (int s = B; s >= 0; s--) {
            if (dp[K][s] == minBad) { bestS = s; break; }
        }
        if (bestS < 0) return null;

        ArrayList<Receta> resultado = new ArrayList<>(K);
        int k = K, s = bestS;
        boolean[] usado = new boolean[n];
        while (k > 0 && s >= 0) {
            int idx = prevIdx[k][s];
            if (idx < 0) break; // seguridad
            if (!usado[idx]) {
                resultado.add(legales.get(idx));
                usado[idx] = true;
            }
            int ps = prevS[k][s];
            s = ps;
            k--;
        }
        if (resultado.size() != K) return null;

        return resultado;
    }

    // metodo de prueba de interfaz
    public static ArrayList<Receta> prueba(ArrayList<Receta> lista){
        return new ArrayList<>(lista.subList(0,10));

    }

}

