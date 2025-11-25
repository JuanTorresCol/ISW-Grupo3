package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static icai.dtc.isw.ui.UiUtils.*;

public class EditarPanel extends JPanel {

    private final CustomerControler controler;
    private final JTextField usuarioField = textField(20);
    private final JPasswordField contrasenaField = passwordField(20);
    private final JPasswordField confirmarContrasenaField = passwordField(20);
    private final JComboBox<String> sexoComboBox = comboBox(new String[]{"HOMBRE","MUJER","OTRO"});
    private final JSpinner edadSpinner = spinner(18, 1, 120, 1);
    private final JCheckBox glutenCheckBox = new JCheckBox("GLUTEN");
    private final JCheckBox lactosaCheckBox = new JCheckBox("LACTOSA");
    private final JCheckBox huevoCheckBox = new JCheckBox("HUEVO");
    private final JCheckBox frutosSecosCheckBox = new JCheckBox("FRUTOS SECOS");
    private final JCheckBox pescadoCheckBox = new JCheckBox("PESCADO");
    private final JCheckBox mariscoCheckBox = new JCheckBox("MARISCO");
    private final JCheckBox otroCheckBox = new JCheckBox("OTRO");
    private final JTextField otroAlergiaField = textField(  10);
    private final JTextField alimentosNoComeField = textField(20);
    private JLabel t;

    // constructor del panel de edicion de usuario
    public EditarPanel(JVentana app, CustomerControler controler) {
        this.controler = controler;

        JScrollPane scrollPane = new JScrollPane(crearFormulario(app));

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {

                scrollPane.getVerticalScrollBar().setValue(0); // subir al inicio al refrescar

            }
        });

        setLayout(new BorderLayout());
        setBackground(BG);

        //Quitar scrollbar horizontal
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton btnBack = pillButton("Volver");
        btnBack.addActionListener(_ -> app.showCard("perfil"));


        add(t, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnBack, BorderLayout.SOUTH);

    }

    // obtiene la información del usuario
    public JPanel crearFormulario(JVentana app) {

        setBackground(BG);
        t = title("Editar Perfil");
        t.setBorder(new javax.swing.border.EmptyBorder(20, 5, 20, 5));
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
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
        for (JCheckBox cb : Arrays.asList(glutenCheckBox, lactosaCheckBox, huevoCheckBox, frutosSecosCheckBox, pescadoCheckBox, mariscoCheckBox, otroCheckBox)) {
            estilizarCheck(cb);
            alergias.add(cb);
        }
        alergias.add(otroAlergiaField);
        form.add(alergias);

        form.add(labels("ALIMENTOS QUE NO COMES"));
        form.add(fieldWrapWest(alimentosNoComeField));

        form.add(Box.createVerticalStrut(16));
        JButton btnRegistrar = pillButton("GUARDAR");
        btnRegistrar.addActionListener(_ -> onEditar(app));

        form.add(Box.createVerticalStrut(16));
        form.add(btnRegistrar);


        return form;
    }

    // extrae los datos de GUI y se los manda al backend para llevar a cabo la lógica de edición de usuario
    private void onEditar(JVentana app) {
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

        Customer usuario = app.getUsuario();
        String resultado = controler.editarPreferencias(
                usuario, userName, pass, passCheck, sexo, edad, seleccionAlergia, alimentosNoCome
        );
        if ("b".equals(resultado)){
            app.refreshCard("perfil");
            app.onEditSuccess();
        } else {
            if(!pass.equals(passCheck)){
                app.onAuthFailed2("Las nuevas contraseñas no coinciden");
            } else {
                app.onAuthFailed2("La edición de perfil no se pudo completar");
            }
        }
    }
}
