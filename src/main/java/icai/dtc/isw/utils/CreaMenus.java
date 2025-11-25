package icai.dtc.isw.utils;

import icai.dtc.isw.controler.ProductoControler;
import icai.dtc.isw.domain.Ingrediente;
import icai.dtc.isw.domain.Producto;
import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.ui.JVentana;

import java.util.*;
import java.util.stream.Collectors;

public class CreaMenus {

    // compruebas si se contiene un ingrediente de la receta en la lista de no deseados
    private static boolean contieneAlguno(Collection<String> ingredientes, Collection<String> lista) {
        if (ingredientes == null || ingredientes.isEmpty() || lista == null || lista.isEmpty()) return false;
        Set<String> set = lista.stream().map(String::toLowerCase).collect(Collectors.toSet());
        for (String ing : ingredientes) {
            if (ing != null && set.contains(ing.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    // devuelve el precio de un menu sin contar con la lista de la compra o los productos
    private static int getPrecioSinLista(Receta receta) {
        ArrayList<Producto> productos = ProductoControler.getProductos();
        double calculo = 0.00;
        for(Ingrediente ing : receta.getIngredientes().values()){
            for(Producto p : productos){
                if(ing.getNombre().equals(p.getNombre())){
                    calculo += p.getPrecio();
                    break;
                }
            }
        }
        return (int) Math.round(calculo * 100.0);
    }

    // devuelve las diez recetas que mas se ajustan a un presupuesto / alergias / preferencias, en el caso de que no se pueda devuelve null
    // hace uso de un algorítmo knapsack con los pesos ajustados a las necesidades del programa
    // además incluye otros pesos a tener en cuenta debido a preferencias del usuario
    public static ArrayList<Receta> creaMenuRes(ArrayList<Receta> recetas, int presupuestoEuros, JVentana app) {
        List<String> illegalFood = app.getUsuario() != null && app.getUsuario().getIllegalFood() != null
                ? app.getUsuario().getIllegalFood() : java.util.Collections.emptyList();
        List<String> noCome = app.getUsuario() != null && app.getUsuario().getAlimentosNoCome() != null
                ? app.getUsuario().getAlimentosNoCome() : java.util.Collections.emptyList();

        final int K = 10;
        final int B = Math.max(0, presupuestoEuros * 100);
        final int INF = 1_000_000;
        Collections.shuffle(recetas);
        java.util.List<Receta> legales = new java.util.ArrayList<>();
        for (Receta r : recetas) {
            java.util.Collection<String> ings = r.getIngredientes().keySet();
            if (!contieneAlguno(ings, illegalFood)) {
                legales.add(r);
            }
        }
        if (legales.size() < K) return null;
        int n = legales.size();
        int[] precio = new int[n];
        int[] bad = new int[n];
        for (int i = 0; i < n; i++) {
            Receta r = legales.get(i);
            precio[i] = getPrecioSinLista(r);
            bad[i] = contieneAlguno(r.getIngredientes().keySet(), noCome) ? 1 : 0;
        }

        java.util.List<Integer> preciosOrdenados =
                java.util.Arrays.stream(precio).boxed().sorted().toList();
        long sumaK = 0;
        for (int i = 0; i < K; i++) sumaK += preciosOrdenados.get(i);
        if (sumaK > B) return null;
        int[][] dp = new int[K + 1][B + 1];
        for (int k = 0; k <= K; k++) java.util.Arrays.fill(dp[k], INF);
        dp[0][0] = 0;
        Node[][] path = new Node[K + 1][B + 1];
        for (int i = 0; i < n; i++) {
            int w = precio[i];
            int b = bad[i];
            for (int k = K; k >= 1; k--) {
                for (int s = B; s >= w; s--) {
                    if (dp[k - 1][s - w] != INF) {
                        int cand = dp[k - 1][s - w] + b;
                        if (cand < dp[k][s]) {
                            dp[k][s] = cand;
                            path[k][s] = new Node(i, path[k - 1][s - w]);
                        }
                    }
                }
            }
        }
        int minBad = INF;
        for (int s = 0; s <= B; s++) if (dp[K][s] < minBad) minBad = dp[K][s];
        if (minBad == INF) return null;
        int bestS = -1;
        for (int s = B; s >= 0; s--) {
            if (dp[K][s] == minBad) { bestS = s; break; }
        }
        if (bestS < 0 || path[K][bestS] == null) return null;
        java.util.ArrayList<Receta> res = new java.util.ArrayList<>(K);
        for (Node p = path[K][bestS]; p != null; p = p.prev) {
            res.add(legales.get(p.idx));
        }
        java.util.Collections.reverse(res);
        return res;
    }

    private static final class Node {
        final int idx;
        final Node prev;
        Node(int idx, Node prev) { this.idx = idx; this.prev = prev; }
    }

    // metodo de prueba de interfaz
//    public static ArrayList<Receta> prueba(ArrayList<Receta> lista){
//        return new ArrayList<>(lista.subList(0,10));
//
//    }

}

