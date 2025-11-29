package icai.dtc.isw.ui.panels;

import icai.dtc.isw.domain.ListaCompra;
import icai.dtc.isw.ui.JVentana;
import icai.dtc.isw.ui.Refreshable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import static icai.dtc.isw.ui.UiUtils.*;

public class ListaCompraPanel extends JPanel implements Refreshable{

    private final JVentana app;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> lista = new JList<>(model);

    // constructor del panel que mostrará la lista de la compra
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
                _ -> new SwingWorker<Void, Void>() {
                    @Override protected Void doInBackground() {
                        app.setUsuario(app.cargarPerfilUsuario());
                        return null;
                    }
                    @Override protected void done() {
                        app.refreshCard("perfil");
                        app.showCard("perfil");
                    }
                }.execute()
        ), BorderLayout.SOUTH);

        refreshAsync();
    }

    // metodos de cálculo y repintado de la lista de la compra de hacen en el background
    // para que el resto de funcionalidades no se ven afectadas
    @Override
    public void refreshAsync() {
        model.clear();
        model.addElement("Cargando...");

        new SwingWorker<java.util.List<String>, Void>() {
            @Override
            protected java.util.List<String> doInBackground() {
                ListaCompra listaDom = app.getLista();
                ArrayList<String> productos = listaDom.verEntradas();
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
