package icai.dtc.isw.ui.panels;

import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class MenuDiaPanel extends JPanel {

    private final String[] diasSemana = {"LUNES","MARTES","MIÉRCOLES","JUEVES","VIERNES"};
    private int idxDia = 0;
    private final JLabel tituloDiaLabel;

    public MenuDiaPanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JButton prev = navArrow("<", _ -> cambiarDia(-1));
        JButton next = navArrow(">", _ -> cambiarDia(1));
        tituloDiaLabel = new JLabel(dayTitle(), SwingConstants.CENTER);
        tituloDiaLabel.setFont(H2);
        tituloDiaLabel.setForeground(TITLE);
        header.add(prev, BorderLayout.WEST);
        header.add(tituloDiaLabel, BorderLayout.CENTER);
        header.add(next, BorderLayout.EAST);

        JPanel tiraDias = flowCenter();
        for (String c : new String[]{"L","M","X","J","V"}) {
            tiraDias.add(chip(c));
        }

        JPanel cards = new JPanel();
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(10,20,10,20));
        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
        cards.add(menuCard(app, "Comida", "PASTA CON POLLO", "30 mins", "FÁCIL"));
        cards.add(Box.createVerticalStrut(16));
        cards.add(menuCard(app, "Cena", "PASTA CON POLLO", "30 mins", "FÁCIL"));

        add(header, BorderLayout.NORTH);
        add(stack(tiraDias, cards), BorderLayout.CENTER);
        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);
    }

    private void cambiarDia(int delta) {
        idxDia = (idxDia + delta + diasSemana.length) % diasSemana.length;
        tituloDiaLabel.setText(dayTitle());
        // TODO: recargar recetas asociadas a idxDia si procede
    }

    private String dayTitle() {
        return "< " + diasSemana[idxDia] + " >";
    }

    private JPanel menuCard(JVentana app, String bloque, String titulo, String tiempo, String dificultad) {
        JPanel card = roundedCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel cab = new JLabel(bloque);
        cab.setFont(H2);
        cab.setForeground(TITLE);
        card.add(cab);
        card.add(Box.createVerticalStrut(6));

        JPanel img = new JPanel();
        img.setPreferredSize(new Dimension(240, 120));
        img.setBackground(new Color(170, 187, 197));
        img.add(new JLabel("Imagen"));
        card.add(img);
        card.add(Box.createVerticalStrut(6));
        setBorder(BorderFactory.createEmptyBorder(0, 250, 0, 250));
        JLabel name = new JLabel(titulo);
        name.setFont(H3);
        card.add(name);

        JLabel meta = new JLabel("⏱ " + tiempo + "    🧾 " + dificultad);
        meta.setFont(SMALL);
        card.add(meta);
        card.add(Box.createVerticalStrut(6));

        JPanel acciones = flowLeft();
        JButton ver = outlineButton("VER RECETA", _ -> app.showCard("recetaDetalle"));
        JButton cambiar = outlineButton("CAMBIAR", _ -> app.showCard("recetasSimilares"));
        acciones.add(ver);
        acciones.add(cambiar);
        card.add(acciones);

        return card;
    }
}
