package icai.dtc.isw.ui.panels;

import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class PresupuestoPanel extends JPanel {

    private final JSpinner presupuestoSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 5000, 5));

    public PresupuestoPanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = title("Introducir presupuesto semanal");

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JPanel dinero = flowCenter();
        dinero.add(pillLabel("€"));
        dinero.add(presupuestoSpinner);
        ((JSpinner.DefaultEditor) presupuestoSpinner.getEditor()).getTextField().setColumns(6);

        JButton generar = pillButton("Generar menú");
        generar.addActionListener(e -> app.showCard("menuDia"));

        center.add(Box.createVerticalStrut(10));
        center.add(dinero);
        center.add(Box.createVerticalStrut(20));
        center.add(center(generar));

        add(t, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottomNav(
                e -> app.showCard("menuDia"),
                e -> app.showCard("listaCompra"),
                e -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);
    }

    public int getPresupuesto() {
        return (int) presupuestoSpinner.getValue();
    }
}
