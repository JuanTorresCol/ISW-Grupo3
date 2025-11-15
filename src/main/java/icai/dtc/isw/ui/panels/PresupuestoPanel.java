package icai.dtc.isw.ui.panels;

import icai.dtc.isw.domain.MenuSemanal;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;

import static icai.dtc.isw.ui.UiUtils.*;

public class PresupuestoPanel extends JPanel {

    private final JSpinner presupuestoSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5000, 5));

    public PresupuestoPanel(JVentana app) {

        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = title("<html><div style='text-align: center;'>Introducir<br> presupuesto<br>semanal<html>");
        t.setBorder(BorderFactory.createEmptyBorder(150,0,90,0));
        add(t, BorderLayout.NORTH);
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JPanel dinero = flowCenter();
        presupuestoSpinner.setPreferredSize(new Dimension(100, 50));
        presupuestoSpinner.setBorder(new EmptyBorder(0,0,0,0));
        fuenteSpinner(presupuestoSpinner,H);
        fondoSpinner(presupuestoSpinner,BG);
        dinero.add(presupuestoSpinner);
        JLabel euro = labelBig("€");
        euro.setBorder(new EmptyBorder(0,0,0,30));
        dinero.add(euro);

        ((JSpinner.DefaultEditor) presupuestoSpinner.getEditor()).getTextField().setColumns(6);

        JButton generar = pillButton("Generar menú");
        generar.addActionListener(_ -> {
            // Obtener el valor del spinner
            int presupuesto = ((Number) presupuestoSpinner.getValue()).intValue();

            MenuSemanal menu = app.getMenuSemanal();
            menu.setPresupuesto(presupuesto);
            menu.generarMenu();
            app.refreshCard("listaCompra");
            app.refreshCard("menuDia");
            if(menu.getLunes()!= null) {
                // Mostrar la siguiente pantalla
                app.showCard("menuDia");
            }else{
                // Mostrar que ha ocurrido un error
                JOptionPane.showMessageDialog(this, "No se ha podido crear un menu con el presupuesto proporcionado");
            }
        });
        //setBorder(BorderFactory.createEmptyBorder(100, 250, 0, 250));

        //center.add(Box.createVerticalStrut(10));


        center.add(dinero);
        //center.add(Box.createVerticalStrut(20));
        center.add(center(generar));

        add(center, BorderLayout.CENTER);
        /*add(bottomNav(
                _ -> {app.showCard("menuDia");app.refreshCard("menuDia");},
                _ -> {app.showCard("listaCompra");app.refreshCard("listaCompra");},
                _ -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);*/

    }

    public int getPresupuesto() {
        return (int) presupuestoSpinner.getValue();
    }
}
