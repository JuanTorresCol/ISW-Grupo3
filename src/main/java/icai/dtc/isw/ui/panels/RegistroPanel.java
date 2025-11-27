package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.ui.JVentana;
import icai.dtc.isw.ui.UiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static icai.dtc.isw.ui.UiUtils.*;

public class RegistroPanel extends JPanel {

    private final int ancho = 33;
    private final CustomerControler controler;
    private final JTextField usuarioField = textField(ancho);
    private final JPasswordField contrasenaField = passwordField(ancho);
    private final JPasswordField confirmarContrasenaField = passwordField(ancho);
    private final JComboBox<String> sexoComboBox = comboBox(new String[]{"HOMBRE","MUJER","OTRO"});
    private final JSpinner edadSpinner = spinner(18, 1, 120, 1);
    private final JCheckBox glutenCheckBox = new JCheckBox("GLUTEN");
    private final JCheckBox lactosaCheckBox = new JCheckBox("LACTOSA");
    private final JCheckBox huevoCheckBox = new JCheckBox("HUEVO");
    private final JCheckBox frutosSecosCheckBox = new JCheckBox("FRUTOS SECOS");
    private final JCheckBox pescadoCheckBox = new JCheckBox("PESCADO");
    private final JCheckBox mariscoCheckBox = new JCheckBox("MARISCO");
    private final JCheckBox otroCheckBox = new JCheckBox("OTRO");
    private final JTextField otroAlergiaField = textField(10);
    private final JTextField alimentosNoComeField = textField(ancho);
    private JLabel t;

    // constructor del panel que lleva a cabo el registro de un customer nuevo
    public RegistroPanel(JVentana app, CustomerControler controler) {
        this.controler = controler;

        JScrollPane scrollPane = new JScrollPane(crearFormulario(app));

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                resetFields();
                scrollPane.getVerticalScrollBar().setValue(0);

            }
        });

        setLayout(new BorderLayout());
        setBackground(BG);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton btnBack = pillButton("Volver al inicio");
        btnBack.addActionListener(_ -> app.showCard("inicio"));

        add(t, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnBack , BorderLayout.SOUTH);
    }

    // crea los formularios en los cuales el user insertará la información del nuevo registro
    public JPanel crearFormulario(JVentana app) {

        setBackground(BG);
        t = title("Crea tu cuenta");
        t.setBorder(new javax.swing.border.EmptyBorder(20, 5, 20, 5));
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.setBorder(new EmptyBorder(10, 25, 20, 25));


        form.add(labels("USUARIO"));
        form.add(fieldWrapWest(usuarioField));

        form.add(labels("CONTRASEÑA"));
        form.add(fieldWrapWest(contrasenaField));

        form.add(labels("CONFIRMAR CONTRASEÑA"));
        form.add(fieldWrapWest(confirmarContrasenaField));

        form.add(labels("SEXO"));
        form.add(fieldWrapWest(sexoComboBox));

        form.add(labels("EDAD"));
        form.add(fieldWrapWest(edadSpinner));

        form.add(labels("ALERGIAS/INTOLERANCIAS"));
        JPanel alergias = gridLayout();
        for (JCheckBox cb : Arrays.asList(glutenCheckBox,lactosaCheckBox,huevoCheckBox,frutosSecosCheckBox,pescadoCheckBox,mariscoCheckBox,otroCheckBox)) {
            estilizarCheck(cb);
            alergias.add(cb);
        }
        alergias.add(otroAlergiaField);
        form.add(alergias);


        form.add(labels("ALIMENTOS QUE NO COMES"));
        form.add(fieldWrapWest(alimentosNoComeField));


        form.add(Box.createVerticalStrut(16));
        JButton btnRegistrar = pillButton("REGISTRARSE");

        btnRegistrar.addActionListener(_ -> onRegistrar(app));
        form.add(btnRegistrar);

        return form;

    }

    // elimina los datos de los formularos del registro
    private void resetFields() {
        UiUtils.clearTextBoxes(this);
        revalidate();
        repaint();
    }

    // extrae los datos de los formularios en el GUI y se lo manda al backend
    private void onRegistrar(JVentana app) {
        String userName = usuarioField.getText();
        String pass = new String(contrasenaField.getPassword());
        String passCheck = new String(confirmarContrasenaField.getPassword());
        String sexo = Objects.requireNonNull(sexoComboBox.getSelectedItem()).toString();
        int edad = (int) edadSpinner.getValue();
        ArrayList<String> seleccionAlergia = new ArrayList<>();
        String alimentosNoCome = alimentosNoComeField.getText();

        if (glutenCheckBox.isSelected()) seleccionAlergia.add("Gluten");
        if (lactosaCheckBox.isSelected()) seleccionAlergia.add("Lactosa");
        if (huevoCheckBox.isSelected()) seleccionAlergia.add("Huevo");
        if (frutosSecosCheckBox.isSelected()) seleccionAlergia.add("Frutos secos");
        if (pescadoCheckBox.isSelected()) seleccionAlergia.add("Pescado");
        if (mariscoCheckBox.isSelected()) seleccionAlergia.add("Marisco");
        if (otroCheckBox.isSelected() && !otroAlergiaField.getText().isBlank()) {
            seleccionAlergia.add(otroAlergiaField.getText().trim());
        }

        Map.Entry<Customer, String> resultado = controler.realizarRegistro(
                userName, pass, passCheck, sexo, edad, seleccionAlergia, alimentosNoCome
        );

        if ("b".equals(resultado.getValue())) {
            app.onRegisterSuccess(resultado.getKey());
        } else {
            if(userName==null){app.onAuthFailed("Introduzca un nombre de usuario", "registro");
            } else if(!pass.equals(passCheck)){
                app.onAuthFailed("Ambas contraseñas no coinciden", "registro");
            } else{
                app.onAuthFailed("El registro no se pudo completar", "registro");
            }
        }
    }
}
