package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static icai.dtc.isw.ui.UiUtils.*;

public class EditarPanel extends JPanel {

    private final CustomerControler controler;
    private final JTextField usuarioField = new JTextField(20);
    private final JPasswordField contrasenaField = new JPasswordField(20);
    private final JPasswordField confirmarContrasenaField = new JPasswordField(20);
    private final JComboBox<String> sexoComboBox = new JComboBox<>(new String[]{"HOMBRE","MUJER","OTRO"});
    private final JSpinner edadSpinner = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));
    private final JCheckBox glutenCheckBox = new JCheckBox("GLUTEN");
    private final JCheckBox lactosaCheckBox = new JCheckBox("LACTOSA");
    private final JCheckBox huevoCheckBox = new JCheckBox("HUEVO");
    private final JCheckBox frutosSecosCheckBox = new JCheckBox("FRUTOS SECOS");
    private final JCheckBox pescadoCheckBox = new JCheckBox("PESCADO");
    private final JCheckBox mariscoCheckBox = new JCheckBox("MARISCO");
    private final JCheckBox otroCheckBox = new JCheckBox("OTRO");
    private final JTextField otroAlergiaField = new JTextField(10);
    private final JTextField alimentosNoComeField = new JTextField(20);

    public EditarPanel(JVentana app, CustomerControler controler) {
        this.controler = controler;

        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = title("Editar Perfil");
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(10, 10, 20, 10));

        form.add(label("USUARIO"));
        form.add(usuarioField);

        form.add(label("CONTRASEÑA"));
        form.add(fieldWrap(contrasenaField));

        form.add(label("CONFIRMAR CONTRASEÑA"));
        form.add(fieldWrap(confirmarContrasenaField));

        form.add(label("SEXO"));
        form.add(fieldWrap(sexoComboBox));

        form.add(label("EDAD"));
        form.add(fieldWrap(edadSpinner));

        form.add(label("ALERGIAS/INTOLERANCIAS"));
        JPanel alergias = flowLeft();
        for (JCheckBox cb : Arrays.asList(glutenCheckBox,lactosaCheckBox,huevoCheckBox,frutosSecosCheckBox,pescadoCheckBox,mariscoCheckBox,otroCheckBox)) {
            estilizarCheck(cb);
            alergias.add(cb);
        }
        alergias.add(otroAlergiaField);
        form.add(alergias);

        form.add(label("ALIMENTOS QUE NO COMES"));
        form.add(fieldWrap(alimentosNoComeField));

        JButton btnRegistrar = pillButton("GUARDAR");
        btnRegistrar.addActionListener(_ -> onEditar(app));

        JButton btnBack = pillButton("Volver");
        btnBack.addActionListener(_ -> app.showCard("perfil"));

        JPanel actions = flowCenter();
        actions.add(btnRegistrar);

        setBorder(BorderFactory.createEmptyBorder(0, 250, 0, 250));
        add(t, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(stack(actions, btnBack), BorderLayout.SOUTH);
    }

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
            app.onAuthFailed2("La edición de perfil no se pudo completar");
        }
    }
}
