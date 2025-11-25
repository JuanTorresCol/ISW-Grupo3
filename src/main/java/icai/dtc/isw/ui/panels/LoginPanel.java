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
            if (container != null) {
                if (pass.equals(container.getCustomer().getUserPass())) {
                    Customer customerCheck = container.getCustomer();
                    MenuSemanal menuSemanal = container.getMenu();
                    app.setMenu(menuSemanal);
                    loginSuccess(menuSemanal, app);
                    app.onLoginSuccess(customerCheck);
                }else{JOptionPane.showMessageDialog(this, "Contraseña incorrecta, pruebe otra vez.");}
            } else {
                Supermercado supermercado = SupermercadoControler.loginSupermercado(userName, pass);
                if(supermercado != null) {
                    app.onLoginSuccessSupermercado(supermercado);
                    System.out.println(app.getSupermercado().getProductosId());
                }else{JOptionPane.showMessageDialog(this, "Inicio de sesión fallido");}
            }
        });

        JButton btnBack = pillButton("Volver al inicio");
        btnBack.addActionListener(_ -> app.showCard("inicio"));

        setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));
        add(t, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(stack(center(btnEntrar), btnBack), BorderLayout.SOUTH);
    }
    private void resetFields() {
        UiUtils.clearTextBoxes(this);
        revalidate();
        repaint();
    }

    public void loginSuccess(MenuSemanal menu, JVentana app){
        SwingWorker<ListaCompra, Void> worker = new SwingWorker<>() {
            @Override
            protected ListaCompra doInBackground() throws Exception {

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
