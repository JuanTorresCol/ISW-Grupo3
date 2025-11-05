package icai.dtc.isw.utils;

import icai.dtc.isw.dao.RecetaDAO;
import icai.dtc.isw.domain.Dificultad;
import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.domain.Ingrediente;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Lector de recetas desde un CSV con columnas:
 * nombre (String), duracion (int), descripcion (String),
 * dificultad (FACIL|MEDIO|DIFICIL), alimentos (String con items "nombre;cantidad;precio" separados por "|")
 * Uso:
 *  Leer recetas del csv y cargarlas a la db con RecetasDAO
 * EJECUTAR UNA SOLA VEZ !!!!!!!!!!!!!!!!!
 */
public final class RecetasCsv {

    private RecetasCsv() {}
    /** Lee el CSV desde un Path (UTF-8). */
    public static List<Receta> leer(Path csvPath) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            return leer(br);
        }
    }

    /** Lee el CSV desde un InputStream (UTF-8). */
    public static List<Receta> leer(InputStream in) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return leer(br);
        }
    }

    // === Implementación ===

    private static List<Receta> leer(BufferedReader br) throws IOException {
        List<Receta> out = new ArrayList<>();
        String line;
        int lineNum = 0;
        boolean headerHandled = false;

        while ((line = br.readLine()) != null) {
            lineNum++;
            // Saltar líneas vacías
            if (line.trim().isEmpty()) continue;

            // Detectar header en la primera línea
            if (!headerHandled) {
                List<String> cols0 = splitCsvLine(line);
                if (!cols0.isEmpty() && cols0.get(0).trim().equalsIgnoreCase("nombre")) {
                    headerHandled = true;
                    continue; // saltamos encabezado
                } else {
                    // no había encabezado; seguimos procesando como fila de datos
                    headerHandled = true;
                }
            }

            List<String> cols = splitCsvLine(line);
            try {
                Receta r = parseReceta(cols, lineNum);
                out.add(r);
            } catch (Exception ex) {
                throw new IOException("Error en línea " + lineNum + ": " + ex.getMessage(), ex);
            }
        }
        return out;
    }

    private static Receta parseReceta(List<String> cols, int lineNum) {
        if (cols.size() < 5) {
            throw new IllegalArgumentException("Se esperaban 5 columnas, encontradas " + cols.size());
        }
        String nombre = cols.get(0).trim();
        String durStr = cols.get(1).trim();
        String descripcion = cols.get(2).trim();
        String difStr = cols.get(3).trim();
        String alimentosStr = cols.get(4).trim();

        int duracion;
        try {
            duracion = Integer.parseInt(durStr);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("duracion no es un int válido: '" + durStr + "'");
        }

        Dificultad dificultad;
        try {
            dificultad = Dificultad.valueOf(difStr.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException("dificultad inválida (use FACIL, MEDIO o DIFICIL): '" + difStr + "'");
        }

        Map<String,Ingrediente> alimentos = parseAlimentos(alimentosStr, lineNum);
        return new Receta(nombre, dificultad,duracion, descripcion, alimentos);
    }

    private static Map<String, Ingrediente> parseAlimentos(String s, int lineNum) {
        Map<String, Ingrediente> mapa = new LinkedHashMap<>();
        if (s == null || s.isEmpty()) return mapa;

        String[] items = s.split("\\|", -1);
        for (int idx = 0; idx < items.length; idx++) {
            String item = items[idx];
            String[] parts = item.split(";", -1);
            if (parts.length != 3) {
                throw new IllegalArgumentException(
                        "alimentos mal formateado en línea " + lineNum +
                                " (item " + (idx + 1) + ", esperado 'nombre;cantidad;precio')"
                );
            }

            String nombre = parts[0].trim();
            String cantidad = parts[1].trim();
            String precioStr = parts[2].trim();

            if (nombre.isEmpty() || cantidad.isEmpty() || precioStr.isEmpty()) {
                throw new IllegalArgumentException(
                        "alimentos contiene campos vacíos en línea " + lineNum +
                                " (item " + (idx + 1) + ")"
                );
            }

            double precio;
            try {
                // El CSV usa punto decimal (ej: 0.40). Si viniera con coma, la normalizamos.
                String normalized = precioStr.replace(',', '.');
                precio = Double.parseDouble(normalized);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException(
                        "precio inválido en línea " + lineNum +
                                " (item " + (idx + 1) + "): '" + precioStr + "'"
                );
            }

            if (mapa.containsKey(nombre)) {
                throw new IllegalArgumentException(
                        "Ingrediente duplicado '" + nombre + "' en línea " + lineNum
                );
            }

            mapa.put(nombre, new Ingrediente(nombre, cantidad, precio));
        }
        return mapa;
    }

    private static List<String> splitCsvLine(String line) {
        List<String> cols = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // comilla escapada
                    sb.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                cols.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(ch);
            }
        }
        cols.add(sb.toString());
        return cols;
    }

    // Solo ejecutar una vez
    static void main(String[] args) throws Exception {
        Path path;
        if (args.length == 0) {

            path = Path.of("src/main/resources/recetas.csv");
            System.err.println("No se pasó ruta por argumento. Usando por defecto: " + path.toAbsolutePath());
        } else {
            path = Path.of(args[0]);
        }
        List<Receta> recetas = leer(path);
        RecetaDAO recetaDAO = new RecetaDAO();
        for (Receta receta : recetas) {
            recetaDAO.registerReceta(receta);
        }
    }
}

