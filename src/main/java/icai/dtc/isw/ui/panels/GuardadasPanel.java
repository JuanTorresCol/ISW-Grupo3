package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.ui.JVentana;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import static icai.dtc.isw.ui.UiUtils.*;
import javax.swing.SwingWorker;

import icai.dtc.isw.ui.Refreshable;

public class GuardadasPanel extends JPanel implements Refreshable {

    private final JVentana app;
    private final JPanel lista;

    // constructor del panel que muestra las recetas guardadas por el usuario
    public GuardadasPanel(JVentana app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = pillTitle("FAVORITOS");

        JPanel header = flowLeft();
        JButton back = flatLink("< ATRÁS", _ -> app.showCard("perfil"));
        header.add(back);

        lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.add(center(new JLabel("Cargando recetas guardadas...")));

        JScrollPane scroll = new JScrollPane(wrapCentered(lista));
        scroll.getViewport().setBackground(BG);
        scroll.setOpaque(false);
        scroll.setBackground(BG);
        scroll.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));

        header.add(center(t));
        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);

        refreshAsync();
    }

    // lleva a cabo la lógica de la extracción y display de las recetas favoritas en el background
    // esto se hace para mejorar el rendimiento del programa ya que este proceso es costoso
    @Override
    public void refreshAsync() {
        lista.removeAll();
        lista.add(center(new JLabel("Cargando recetas guardadas...")));
        lista.revalidate();
        lista.repaint();

        new SwingWorker<ArrayList<Receta>, Void>() {
            @Override
            protected ArrayList<Receta> doInBackground() {
                return app.getUsuario().getRecetasFav();
            }

            @Override
            protected void done() {
                try {
                    ArrayList<Receta> recetasFavoritas = get();
                    lista.removeAll();
                    for (Receta r : recetasFavoritas) {
                        lista.add(similarCard(r, r.getNombre(), r.getDuracion()+" mins", r.getDificultad().toString()));
                        lista.add(Box.createVerticalStrut(12));
                    }
                } catch (Exception ex) {
                    lista.removeAll();
                    lista.add(center(new JLabel("Aún no hay recetas guardadas.")));
                }
                lista.revalidate();
                lista.repaint();
            }
        }.execute();
    }

    // tarjetas que contienen la información de una receta de las guardadas por el usuario
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
        JButton infor = outlineButton("VER INFORMACION", _ -> {
            app.setReceta(receta);
            app.refreshCard("recetasGuardadasDetalle");
            app.showCard("recetasGuardadasDetalle");
        });
        JButton del = outlineButton("ELIMINAR", _ -> {
            CustomerControler.eliminarReceta(app.getUsuario(),receta);
            app.showCard("perfil");
            JOptionPane.showMessageDialog(this, "Receta eliminada con éxito");
        });

        info.add(name);
        info.add(meta);
        info.add(Box.createVerticalStrut(6));
        info.add(infor);
        info.add(del);

        card.add(img, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        return card;
    }
}