package icai.dtc.isw.ui.panels;

import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.ui.JVentana;

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
        content.setBorder(new EmptyBorder(10, 30, 30, 30));

        Customer usuario = app.getUsuario();

        // Header de perfil
        JPanel cab = roundedCard();
        cab.setLayout(new BoxLayout(cab, BoxLayout.Y_AXIS));
        JLabel user = new JLabel(usuario.getUserName().toUpperCase(), SwingConstants.CENTER);
        JLabel icono = new JLabel(cargarIcono(PerfilPanel.class,"usuario",80,80));
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

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
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
        content.add(keyValue("ALIMENTOS QUE NO COMES:", noCome));

        content.add(Box.createVerticalStrut(10));

        // Histórico (placeholder)
        JLabel prev = labelBold("OTROS");
        JButton s1 = flatLink("Recetas Guardadas", _ -> {
            app.refreshCard("recetasGuardadas");
            app.showCard("recetasGuardadas");
        });
        JButton exit = flatLink("Cerrar Sesión >", _ -> app.logout());
        content.add(center(prev));
        content.add(center(s1));
        content.add(center(exit));

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

