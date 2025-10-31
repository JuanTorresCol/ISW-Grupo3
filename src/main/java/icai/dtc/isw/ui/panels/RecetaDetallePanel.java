package icai.dtc.isw.ui.panels;

import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class RecetaDetallePanel extends JPanel {

    public RecetaDetallePanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JPanel header = flowLeft();
        JButton back = flatLink("< ATRÁS", _ -> app.showCard("menuDia"));
        header.add(back);

        JLabel t = pillTitle("RECETA");
        JPanel hero = roundedCard();
        hero.setLayout(new BorderLayout());
        hero.add(pillTitle("PASTA CON POLLO"), BorderLayout.CENTER);

        JPanel ing = section("INGREDIENTES",
                "- 80-100 g de pasta\n- 1 pechuga de pollo pequeña\n- 1 diente de ajo\n- 1/4 de cebolla\n- 100 ml de nata (opcional)\n- Aceite de oliva, sal, pimienta\n- Queso rallado (opcional)\n- Verduras al gusto");

        JPanel steps = section("INSTRUCCIONES",
                "1. Cocer la pasta.\n2. Saltear el pollo.\n3. Añadir verduras.\n4. Unir todo con la pasta y servir.");

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(t);
        center.add(Box.createVerticalStrut(10));
        center.add(hero);
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
