package icai.dtc.isw.ui.panels;

import icai.dtc.isw.ui.JVentana;
import icai.dtc.isw.ui.UiUtils;

import javax.swing.*;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class InicioPanel extends JPanel {

    public InicioPanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(UiUtils.BG);

        JLabel labelLogo = new JLabel(UiUtils.cargarIcono(InicioPanel.class, "logotipo", 500, 300), SwingConstants.CENTER);
        labelLogo.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        JPanel botones = new JPanel(new GridLayout(2,1,0,30));
        botones.setOpaque(false);

        JButton btnLogin = UiUtils.pillButton("Iniciar sesión");
        JButton btnRegistro = UiUtils.pillButton("Registrarse");
        btnLogin.addActionListener(e -> app.showCard("login"));
        btnRegistro.addActionListener(e -> app.showCard("registro"));
        botones.add(btnLogin);
        botones.add(btnRegistro);

        add(labelLogo, BorderLayout.NORTH);
        add(UiUtils.wrapCentered(botones), BorderLayout.CENTER);
    }
}
