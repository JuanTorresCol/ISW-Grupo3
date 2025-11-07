package icai.dtc.isw.ui.panels;

import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class PerfilPanel extends JPanel {

    public PerfilPanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = pillTitle("PERFIL");

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 30, 30, 30));

        Customer usuario = app.getUsuario();

        // Header de perfil
        JPanel cab = roundedCard();
        cab.setLayout(new BoxLayout(cab, BoxLayout.Y_AXIS));
        JLabel user = new JLabel(usuario.getUserName().toUpperCase(), SwingConstants.CENTER);
        user.setAlignmentX(Component.CENTER_ALIGNMENT);
        user.setFont(H3);
        JButton editar = pillButton("Editar perfil");
        editar.setAlignmentX(Component.CENTER_ALIGNMENT);
        editar.addActionListener(_ -> app.showCard("editarPerfil"));
        cab.add(center(user));
        cab.add(Box.createVerticalStrut(6));
        cab.add(center(editar));

        // Datos básicos
        content.add(t);
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
        content.add(keyValue("ALERGIAS/INTOLERANCIAS:", illegal));

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

        content.add(Box.createVerticalStrut(12));

        // Histórico (placeholder)
        JLabel prev = labelBold("VER MENÚS ANTERIORES");
        JButton s1 = flatLink("Hace 1 semana >", _ -> JOptionPane.showMessageDialog(this, "Histórico en desarrollo"));
        JButton s2 = flatLink("Hace 2 semanas >", _ -> JOptionPane.showMessageDialog(this, "Histórico en desarrollo"));
        content.add(prev);
        content.add(center(s1));
        content.add(center(s2));

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

