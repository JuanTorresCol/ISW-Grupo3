package icai.dtc.isw.ui.panels;

import icai.dtc.isw.dao.CustomerDAO;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class LoginPanel extends JPanel {

    public LoginPanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = title("Inicio de sesión");
        t.setBorder(new javax.swing.border.EmptyBorder(120, 40, 20, 40));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new javax.swing.border.EmptyBorder(40, 330, 10, 200));

        JTextField usuarioLoginField = new JTextField(20);
        JPasswordField contrasenaLoginField = new JPasswordField(20);

        form.add(labels("USUARIO"));
        form.add(fieldWrap(usuarioLoginField,new Dimension(600,50)));
        form.add(labels("CONTRASEÑA"));
        form.add(fieldWrap(contrasenaLoginField,new Dimension(600,50)));

        JButton btnEntrar = pillButton("Entrar");
        btnEntrar.addActionListener(_ -> {
            String userName = usuarioLoginField.getText().trim();
            String pass = new String(contrasenaLoginField.getPassword());
            Customer customerCheck = CustomerDAO.getCliente(userName);
            if (customerCheck != null && pass.equals(customerCheck.getUserPass())) {
                app.onLoginSuccess(customerCheck);
            } else {
                JOptionPane.showMessageDialog(this, "Inicio de sesión fallido");
            }
        });

        JButton btnBack = pillButton("Volver al inicio");
        btnBack.addActionListener(_ -> app.showCard("inicio"));

        setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));
        add(t, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(stack(center(btnEntrar), btnBack), BorderLayout.SOUTH);
    }
}
