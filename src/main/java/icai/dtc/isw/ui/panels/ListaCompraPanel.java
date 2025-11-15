package icai.dtc.isw.ui.panels;

import icai.dtc.isw.domain.Lista;
import icai.dtc.isw.ui.JVentana;
import icai.dtc.isw.ui.Refreshable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import static icai.dtc.isw.ui.UiUtils.*;

public class ListaCompraPanel extends JPanel implements Refreshable {

    private final JVentana app;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> lista = new JList<>(model);

    public ListaCompraPanel(JVentana app) {
        this.app = app;

        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = pillTitle("LISTA DE LA COMPRA");
        JPanel titulo = basePanel(new BorderLayout());
        titulo.add(t, BorderLayout.NORTH);

        lista.setFont(BODY);
        lista.setBackground(BG);

        add(titulo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 5));
        scroll.setBackground(BG);
        add(scroll, BorderLayout.CENTER);

        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> {
                    new SwingWorker<Void, Void>() {
                        @Override protected Void doInBackground() {
                            app.setUsuario(app.cargarPerfilUsuario());
                            return null;
                        }
                        @Override protected void done() {
                            app.refreshCard("perfil");
                            app.showCard("perfil");
                        }
                    }.execute();
                }
        ), BorderLayout.SOUTH);

        refreshAsync();
    }

    @Override
    public void refreshAsync() {
        model.clear();
        model.addElement("Cargando...");

        new SwingWorker<java.util.List<String>, Void>() {
            @Override
            protected java.util.List<String> doInBackground() {
                Lista listaDom = new Lista(0, app.getMenuSemanal());
                ArrayList<String> productos = listaDom.visualizarProductos();
                return productos != null ? productos : Collections.emptyList();
            }

            @Override
            protected void done() {
                try {
                    java.util.List<String> productos = get();
                    model.clear();
                    if (productos.isEmpty()) {
                        model.addElement("Primero inserte un presupuesto");
                    } else {
                        for (String p : productos) model.addElement(p);
                    }
                } catch (Exception e) {
                    model.clear();
                    model.addElement("Error cargando la lista: " + e.getMessage());
                }
            }
        }.execute();
    }
}
