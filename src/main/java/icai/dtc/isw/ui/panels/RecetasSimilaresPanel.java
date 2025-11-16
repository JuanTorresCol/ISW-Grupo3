package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.RecetaControler;
import icai.dtc.isw.domain.MenuSemanal;
import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static icai.dtc.isw.ui.UiUtils.*;

public class RecetasSimilaresPanel extends JPanel {

    private JVentana app;

    public RecetasSimilaresPanel(JVentana app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(0,250,0,250));

        JLabel t = pillTitle("CAMBIAR RECETA");

        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        ArrayList<Receta> recetasCambio = app.getMenuSemanal().getRecetasSimilares(RecetaControler.getRecetas(), app.getUsuario());
        for (Receta recetaCambio : recetasCambio) {
            lista.add(similarCard(recetaCambio.getNombre(), recetaCambio.getDuracion()+" mins", recetaCambio.getDificultad().toString()));
            lista.add(Box.createVerticalStrut(12));
        }

        add(t, BorderLayout.NORTH);
        add(wrapCentered(lista), BorderLayout.CENTER);
        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);
    }

    private JPanel similarCard(String titulo, String tiempo, String dificultad) {
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
            JOptionPane.showMessageDialog(this, "Seleccionada receta similar (fakke)");
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
