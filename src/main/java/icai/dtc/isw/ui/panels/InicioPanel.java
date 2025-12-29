package icai.dtc.isw.ui.panels;

import icai.dtc.isw.ui.JVentana;
import icai.dtc.isw.ui.UiUtils;

import javax.swing.*;
import java.awt.*;

public class InicioPanel extends JPanel {

    // constructor del panel de inicio que da opciones de login y registro
    public InicioPanel(JVentana app) {

        setLayout(new BorderLayout());
        setBackground(UiUtils.BG);

        JLabel labelLogo = new JLabel(UiUtils.cargarIcono(InicioPanel.class, "logotipo", 550, 400), SwingConstants.CENTER);
        labelLogo.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JPanel botones = new JPanel(new GridLayout(2,1,0,30));
        botones.setOpaque(false);

        JButton btnLogin = UiUtils.pillButton("Iniciar sesión");
        JButton btnRegistro = UiUtils.pillButton("Registrarse");
        btnLogin.addActionListener(_ -> app.showCard("login"));
        btnRegistro.addActionListener(_ -> app.showCard("registro"));
        botones.add(btnLogin);
        botones.add(btnRegistro);

        add(labelLogo, BorderLayout.NORTH);
        add(UiUtils.wrapCentered(botones), BorderLayout.CENTER);
    }

}
