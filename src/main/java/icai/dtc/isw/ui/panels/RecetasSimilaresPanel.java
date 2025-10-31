package icai.dtc.isw.ui.panels;

import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class RecetasSimilaresPanel extends JPanel {

    public RecetasSimilaresPanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = pillTitle("RECETAS SIMILARES");

        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        for (int i=0;i<4;i++) {
            lista.add(similarCard(app,"POLLO AL AJILLO CON VERDURAS", "30 mins", "FÁCIL"));
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

    private JPanel similarCard(JVentana app, String titulo, String tiempo, String dificultad) {
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
            JOptionPane.showMessageDialog(this, "Seleccionada receta similar");
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
