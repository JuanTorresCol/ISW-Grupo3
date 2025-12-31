package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.controler.SupermercadoControler;
import icai.dtc.isw.domain.*;
import icai.dtc.isw.ui.JVentana;
import icai.dtc.isw.ui.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static icai.dtc.isw.ui.UiUtils.*;

public class LoginPanel extends JPanel {

    CustomerControler controler = new CustomerControler();

    // constructor del panel que permite hace rlogin tanto a supermercados como a customers
    public LoginPanel(JVentana app) {
        this.addComponentListener(new ComponentAdapter() {
            @Override public void componentShown(ComponentEvent e) {
                resetFields();
            }
        });
        setLayout(new BorderLayout());
        setBackground(BG);


        JLabel t = title("Inicio de sesión");
        t.setBorder(new javax.swing.border.EmptyBorder(120, 40, 20, 40));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new javax.swing.border.EmptyBorder(40, 25, 10, 25));

        JTextField usuarioLoginField = textField(20);
        JPasswordField contrasenaLoginField = new JPasswordField(20);
        contrasenaLoginField.setBorder(new javax.swing.border.LineBorder(TITLE));

        form.add(labels("USUARIO"));
        form.add(fieldWrap(usuarioLoginField,new Dimension(400,30)));
        form.add(labels("CONTRASEÑA"));
        form.add(fieldWrap(contrasenaLoginField,new Dimension(400,30)));

        JButton btnEntrar = pillButton("Entrar");
        btnEntrar.addActionListener(_ -> {
            String userName = usuarioLoginField.getText().trim();
            String pass = new String(contrasenaLoginField.getPassword());

            ContainerMenuCustomer container = controler.getCustomerMenu(userName);
            Customer customer = (container != null) ? container.getCustomer() : null;

            Supermercado supermercado = SupermercadoControler.loginSupermercado(userName);

            boolean esCustomer = (customer != null);
            boolean esSupermercado = (supermercado != null);

            //si no existe customer ni supermercado en la tabla
            if (!esCustomer && !esSupermercado) {
                JOptionPane.showMessageDialog(this,
                        "Usuario no existente.");
                return;
            }

            // separamos la lógica de iniciar como super o como customer
            if (esCustomer) {
                if (pass.equals(customer.getUserPass())) {
                    MenuSemanal menuSemanal = container.getMenu();
                    app.setMenu(menuSemanal);
                    app.setPresupuesto(customer.getPresupuesto());
                    loginSuccess(menuSemanal, app);
                    app.onLoginSuccess(customer);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Contraseña incorrecta.");
                }
                return;
            }
            if (esSupermercado) {
                if (pass.equals(supermercado.getUserPass())) {
                    app.onLoginSuccessSupermercado(supermercado);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Contraseña incorrecta.");
                }
            }
        });


        JButton btnBack = pillButton("Volver al inicio");
        btnBack.addActionListener(_ -> app.showCard("inicio"));

        setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));
        add(t, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(stack(center(btnEntrar), btnBack), BorderLayout.SOUTH);
    }

    // vacia las celdas de la pestaña del login por si se cierra sesión y se desea volver a iniciar sesión
    private void resetFields() {
        UiUtils.clearTextBoxes(this);
        revalidate();
        repaint();
    }

    // extrae los datos de la GUI y los manda al backend para que lleve a cabo la lógica del login
    public void loginSuccess(MenuSemanal menu, JVentana app){
        SwingWorker<ListaCompra, Void> worker = new SwingWorker<>() {
            @Override
            protected ListaCompra doInBackground() {

                if (menu.getLunes() == null) {
                    return null;
                }
                return menu.generarListaCompra();
            }

            @Override
            protected void done() {
                try {
                    ListaCompra lista = get();
                    if (lista != null && menu.getLunes() != null) {
                        app.setLista(lista);
                        app.refreshCard("listaCompra");
                        app.refreshCard("menuDia");
                        app.showCard("menuDia");
                    } else {
                        JOptionPane.showMessageDialog(
                                LoginPanel.this,
                                "No se ha podido hacer login"
                        );
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            LoginPanel.this,
                            "Error al generar la lista: " + ex.getMessage()
                    );
                }
            }
        };worker.execute();
    }
}
