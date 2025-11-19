package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.RecetaControler;
import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static icai.dtc.isw.ui.UiUtils.*;

import javax.swing.SwingWorker;
import icai.dtc.isw.ui.Refreshable;

public class RecetasSimilaresPanel extends JPanel implements Refreshable {

    private final JVentana app;
    private final JPanel lista;

    public RecetasSimilaresPanel(JVentana app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(BG);
        //setBorder(BorderFactory.createEmptyBorder(0,250,0,250));

        JLabel t = pillTitle("CAMBIAR RECETA");

        lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.add(center(new JLabel("Cargando recetas similares...")));

        JScrollPane scroll = new JScrollPane(wrapCentered(lista));
        scroll.getViewport().setBackground(BG);
        scroll.setOpaque(false);
        scroll.setBackground(BG);
        scroll.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));

        add(center(t), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);

        refreshAsync();
    }

    @Override
    public void refreshAsync() {
        lista.removeAll();
        lista.add(center(new JLabel("Cargando recetas similares...")));
        lista.revalidate();
        lista.repaint();

        new SwingWorker<ArrayList<Receta>, Void>() {
            @Override
            protected ArrayList<Receta> doInBackground() {
                return app.getMenuSemanal()
                        .getRecetasSimilares(RecetaControler.getRecetas(), app.getUsuario(), app);
            }

            @Override
            protected void done() {
                try {
                    ArrayList<Receta> recetasCambio = get();
                    lista.removeAll();
                    for (Receta r : recetasCambio) {
                        lista.add(similarCard(r, r.getNombre(), r.getDuracion()+" mins", r.getDificultad().toString()));
                        lista.add(Box.createVerticalStrut(12));
                    }
                } catch (Exception ex) {
                    lista.removeAll();
                    lista.add(center(new JLabel("No se pudieron cargar las recetas.")));
                }
                lista.revalidate();
                lista.repaint();
            }
        }.execute();
    }

    private JPanel similarCard(Receta receta, String titulo, String tiempo, String dificultad) {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout(10,0));

        JPanel img = new JPanel();
        img.setPreferredSize(new Dimension(140, 80));
        img.setBackground(new Color(170, 187, 197));
        img.add(new JLabel("Img"));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(titulo);
        name.setFont(H3);
        JLabel meta = new JLabel("⏱ " + tiempo + "    🧾 " + dificultad);
        meta.setFont(SMALL);
        JButton sel = outlineButton("SELECCIONAR", _ -> {
            app.getMenuSemanal().cambioReceta(receta, app.getBloque(),app.getDia());
            app.refreshCard("menuDia");
            app.showCard("menuDia");
        });

        info.add(name);
        info.add(meta);
        info.add(Box.createVerticalStrut(6));
        info.add(sel);

        card.add(img, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        return card;
    }
}
