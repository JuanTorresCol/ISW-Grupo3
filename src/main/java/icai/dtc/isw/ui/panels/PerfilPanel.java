package icai.dtc.isw.ui.panels;

import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.ui.JVentana;
import icai.dtc.isw.ui.UiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class PerfilPanel extends JPanel {

    // constructor del panel que muestra la información del perfil del customer
    public PerfilPanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = pillTitle("PERFIL");
        JPanel titulo = basePanel(new BorderLayout());
        titulo.add(t,BorderLayout.NORTH);


        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));

        Customer usuario = app.getUsuario();

        // Header de perfil
        JPanel cab = roundedCard();
        cab.setLayout(new BoxLayout(cab, BoxLayout.Y_AXIS));
        JLabel user = new JLabel(usuario.getUserName().toUpperCase(), SwingConstants.CENTER);
        JLabel icono = new JLabel(cargarIcono(PerfilPanel.class,"usuario",80,90));
        user.setAlignmentX(Component.CENTER_ALIGNMENT);
        user.setFont(H3);
        JPanel user_logo = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        user_logo.setOpaque(false);
        user_logo.add(icono);
        user_logo.add(user);

        JButton editar = pillButton("Editar Perfil");
        JButton crearNuevoMenu = pillButton("Crear Nuevo Menú");

        editar.addActionListener(_ -> app.showCard("editarPerfil"));

        crearNuevoMenu.addActionListener(_ -> {
            app.ensurePanel("presupuesto");
            app.showCard("presupuesto");
        });

        editar.setAlignmentX(Component.CENTER_ALIGNMENT);
        crearNuevoMenu.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 0));
        botones.setOpaque(false);
        botones.add(editar);
        botones.add(crearNuevoMenu);

        cab.add(center(user_logo));
        cab.add(Box.createVerticalStrut(6));
        cab.add(botones);

        // Datos básicos
        content.add(center(t));
        content.add(Box.createVerticalStrut(10));
        content.add(cab);
        content.add(Box.createVerticalStrut(14));

        content.add(keyValue("SEXO:", usuario.getUserGender()));
        content.add(keyValue("EDAD:", String.valueOf(usuario.getUserAge())));

        // Alergias / intolerancias
        String illegal = usuario.illegalFoodToString();
        if (illegal == null || illegal.isBlank()) {
            illegal = "NINGUNO";
        } else {
            illegal = illegal.toUpperCase();
        }
        content.add(keyValue("ALERGIAS:", illegal));

        // Alimentos que no come (ArrayList<String> -> String)
        String noCome;
        if (usuario.getAlimentosNoCome() == null || usuario.getAlimentosNoCome().isEmpty()) {
            noCome = "NINGUNO";
        } else {
            noCome = String.join(", ", usuario.getAlimentosNoCome())
                    .replace("{", "")
                    .replace("}", "")
                    .toUpperCase();
        }

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(labelBold( " ALIMENTOS QUE NO COMES:"+ " "));
        panel.add(Box.createVerticalStrut(3));
        panel.add(body(noCome));
        JScrollPane scroll = new JScrollPane(panel);
        scroll.getViewport().setBackground(BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        content.add(scroll);

        //content.add(Box.createVerticalStrut(3));

        // Histórico (placeholder)
        JLabel prev = labelBold(" OTROS ");
        prev.setBorder(BorderFactory.createLineBorder(TITLE,1));
        JButton s1 = flatLink("Recetas Guardadas", _ -> {
            app.refreshCard("recetasGuardadas");
            app.showCard("recetasGuardadas");
        });
        JButton exit = flatLink("Cerrar Sesión >", _ -> app.logout());

        JPanel otros = new JPanel();
        otros.setLayout(new BoxLayout(otros, BoxLayout.Y_AXIS));
        JPanel otros_left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        otros_left.setOpaque(false);
        otros.setOpaque(false);
        prev.setAlignmentX(Component.LEFT_ALIGNMENT);
        s1.setAlignmentX(Component.LEFT_ALIGNMENT);
        exit.setAlignmentX(Component.LEFT_ALIGNMENT);
        otros.add(prev);
        otros.add(Box.createVerticalStrut(8));
        otros.add(s1);
        otros.add(Box.createVerticalStrut(4));
        otros.add(exit);
        otros_left.add(otros);

        content.add(Box.createVerticalStrut(8));
        content.add(otros_left);


        add(content, BorderLayout.CENTER);
        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> {
                    app.setUsuario(app.cargarPerfilUsuario());
                    app.refreshCard("perfil");
                    app.showCard("perfil");
                }
        ), BorderLayout.SOUTH);
    }
}

