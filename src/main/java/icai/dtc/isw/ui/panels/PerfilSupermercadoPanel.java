package icai.dtc.isw.ui.panels;

import icai.dtc.isw.domain.Supermercado;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class PerfilSupermercadoPanel extends JPanel {

    public PerfilSupermercadoPanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = pillTitle("PERFIL");
        JPanel titulo = basePanel(new BorderLayout());
        titulo.add(t,BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 30, 30, 30));

        Supermercado usuario = app.getSupermercado();

        // Header de perfil
        JPanel cab = roundedCard();
        cab.setLayout(new BoxLayout(cab, BoxLayout.Y_AXIS));
        JLabel user = new JLabel(usuario.getUserName().toUpperCase(), SwingConstants.CENTER);
        user.setAlignmentX(Component.CENTER_ALIGNMENT);
        user.setFont(H3);

        cab.add(center(user));
        cab.add(Box.createVerticalStrut(6));

        // Datos básicos
        content.add(center(t));
        content.add(Box.createVerticalStrut(10));
        content.add(cab);
        content.add(Box.createVerticalStrut(14));

        content.add(keyValue("ID: ", usuario.getUserId()));
        content.add(keyValue("NOMBRE: ", String.valueOf(usuario.getUserName())));

        content.add(keyValue("NÚMERO DE PRODUCTOS: ", String.valueOf(usuario.getNumProd())));

        JLabel prev = labelBold("OTROS");
        JButton s1 = flatLink("Productos en Stock", _ -> {
            app.refreshCard("productosSuper");
            app.showCard("productosSuper");
        });
        JButton exit = flatLink("Cerrar Sesión >", _ -> app.logoutSuper());
        content.add(center(prev));
        content.add(center(s1));
        content.add(center(exit));
        add(content);

    }
}

