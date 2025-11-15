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
        System.out.println(ingredientes);
        for (String ing : ingredientes) {
            if (ing != null && set.contains(ing.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    // devuelve las diez recetas que mas se ajustan a un presupuesto / alergias / preferencias, en el caso de que no se pueda devuelve null
    public static ArrayList<Receta> creaMenuRes(ArrayList<Receta> recetas, int presupuestoEuros, JVentana app) {
        // Defensivo por si vienen null
        List<String> illegalFood = app.getUsuario() != null && app.getUsuario().getIllegalFood() != null
                ? app.getUsuario().getIllegalFood() : java.util.Collections.emptyList();
        List<String> noCome = app.getUsuario() != null && app.getUsuario().getAlimentosNoCome() != null
                ? app.getUsuario().getAlimentosNoCome() : java.util.Collections.emptyList();

        final int K = 10;
        final int B = Math.max(0, presupuestoEuros * 100);
        final int INF = 1_000_000;

        // 1) Filtrar recetas legales
        java.util.List<Receta> legales = new java.util.ArrayList<>();
        for (Receta r : recetas) {
            java.util.Collection<String> ings = r.getIngredientes().keySet();
            if (!contieneAlguno(ings, illegalFood)) {
                legales.add(r);
            }
        }
        if (legales.size() < K) return null;

        // 2) Prepara arrays de precio y “penalización” (noCome)
        int n = legales.size();
        int[] precio = new int[n];
        int[] bad = new int[n];
        for (int i = 0; i < n; i++) {
            Receta r = legales.get(i);
            precio[i] = (int) Math.round(r.getPrecio() * 100.0); // a céntimos
            bad[i] = contieneAlguno(r.getIngredientes().keySet(), noCome) ? 1 : 0;
        }

        // 3) Chequeo rápido de factibilidad: suma de las K más baratas
        java.util.List<Integer> preciosOrdenados =
                java.util.Arrays.stream(precio).boxed().sorted().collect(java.util.stream.Collectors.toList());
        long sumaK = 0;
        for (int i = 0; i < K; i++) sumaK += preciosOrdenados.get(i);
        if (sumaK > B) return null;

        // 4) DP: dp[k][s] = mínimo "bad"; path[k][s] = camino inmutable hasta (k,s)
        int[][] dp = new int[K + 1][B + 1];
        for (int k = 0; k <= K; k++) java.util.Arrays.fill(dp[k], INF);
        dp[0][0] = 0;

        Node[][] path = new Node[K + 1][B + 1];
        // path[0][0] = null; // ya es null por defecto

        for (int i = 0; i < n; i++) {
            int w = precio[i];
            int b = bad[i];
            for (int k = K; k >= 1; k--) {
                for (int s = B; s >= w; s--) {
                    if (dp[k - 1][s - w] != INF) {
                        int cand = dp[k - 1][s - w] + b;
                        if (cand < dp[k][s]) {
                            dp[k][s] = cand;
                            // Capturamos el camino *tal cual está ahora* en (k-1, s-w)
                            path[k][s] = new Node(i, path[k - 1][s - w]);
                        }
                        // Si quieres desempatar por presupuesto gastado, no hace falta aquí,
                        // porque abajo elegimos el mayor 's' con minBad recorriendo de B hacia 0.
                    }
                }
            }
        }

        // 5) Buscamos el número mínimo de "bad" posible y, con eso, el mayor gasto s<=B
        int minBad = INF;
        for (int s = 0; s <= B; s++) if (dp[K][s] < minBad) minBad = dp[K][s];
        if (minBad == INF) return null;

        int bestS = -1;
        for (int s = B; s >= 0; s--) {
            if (dp[K][s] == minBad) { bestS = s; break; } // preferimos gastar más
        }
        if (bestS < 0 || path[K][bestS] == null) return null;

        // 6) Reconstrucción: seguimos la lista enlazada
        java.util.ArrayList<Receta> res = new java.util.ArrayList<>(K);
        for (Node p = path[K][bestS]; p != null; p = p.prev) {
            res.add(legales.get(p.idx));
        }
        java.util.Collections.reverse(res); // opcional, para mantener orden “natural”
        return res;
    }

    // Nodo inmutable que guarda el índice del ítem añadido y el camino anterior
    private static final class Node {
        final int idx;
        final Node prev;
        Node(int idx, Node prev) { this.idx = idx; this.prev = prev; }
    }


    // metodo de prueba de interfaz
    public static ArrayList<Receta> prueba(ArrayList<Receta> lista){
        return new ArrayList<>(lista.subList(0,10));

    }

}

