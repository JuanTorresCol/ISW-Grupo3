package icai.dtc.isw.ui.panels;

import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class GuardadasDetallePanel extends JPanel {

    public GuardadasDetallePanel(JVentana app, Receta receta) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JPanel header = flowLeft();
        JButton back = flatLink("< ATRÁS", _ -> app.showCard("recetasGuardadas"));
        header.add(back);

        JLabel t = pillTitle(receta.getNombre().toUpperCase());


        JPanel ing = section("INGREDIENTES",
                receta.ingredientesToString());

        JPanel steps = section("INSTRUCCIONES",
                receta.formatearDescripcion(receta.getDescripcion()));

        JPanel center = new JPanel();
        center.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(t);
        center.add(Box.createVerticalStrut(10));
        center.add(ing);
        center.add(Box.createVerticalStrut(10));
        center.add(steps);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);
    }
}
