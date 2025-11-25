package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.domain.ListaCompra;
import icai.dtc.isw.domain.MenuSemanal;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class PresupuestoPanel extends JPanel {

    private final JSpinner presupuestoSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5000, 5));

    // panel del que se obtiene el presupuesto para calcular el menú semanal
    public PresupuestoPanel(JVentana app) {

        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = title("<html><div style='text-align: center;'>Introducir<br> presupuesto<br>semanal<html>");
        t.setBorder(BorderFactory.createEmptyBorder(150, 0, 90, 0));
        add(t, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JPanel dinero = flowCenter();
        presupuestoSpinner.setPreferredSize(new Dimension(100, 50));
        presupuestoSpinner.setBorder(new EmptyBorder(0, 0, 0, 0));
        fuenteSpinner(presupuestoSpinner, H);
        fondoSpinner(presupuestoSpinner, BG);
        dinero.add(presupuestoSpinner);
        JLabel euro = labelBig("€");
        euro.setBorder(new EmptyBorder(0, 0, 0, 30));
        dinero.add(euro);

        ((JSpinner.DefaultEditor) presupuestoSpinner.getEditor()).getTextField().setColumns(6);

        JProgressBar loading = new JProgressBar();
        loading.setIndeterminate(true);
        loading.setBorder(new EmptyBorder(20, 0, 0, 0));
        loading.setVisible(false);

        JButton generar = pillButton("Generar menú");
        generar.addActionListener(_ -> {
            int presupuesto = ((Number) presupuestoSpinner.getValue()).intValue();
            MenuSemanal menu = app.getMenuSemanal();
            menu.setPresupuesto(presupuesto);

            generar.setEnabled(false);
            presupuestoSpinner.setEnabled(false);
            loading.setVisible(true);

            // la lógica es pesada por lo que se lleva a cabo en el background
            SwingWorker<ListaCompra, Void> worker = new SwingWorker<>() {
                @Override
                protected ListaCompra doInBackground() {
                    menu.generarMenu(app);

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
                            guardarMenu(menu, app);
                            app.refreshCard("listaCompra");
                            app.refreshCard("menuDia");
                            app.showCard("menuDia");
                        } else {
                            JOptionPane.showMessageDialog(
                                    PresupuestoPanel.this,
                                    "No se ha podido crear un menú con el presupuesto proporcionado"
                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                PresupuestoPanel.this,
                                "Error al generar el menú/lista: " + ex.getMessage()
                        );
                    } finally {
                        generar.setEnabled(true);
                        presupuestoSpinner.setEnabled(true);
                        loading.setVisible(false);
                    }
                }
            };

            worker.execute();
        });

        center.add(dinero);
        center.add(center(generar));
        center.add(loading);

        add(center, BorderLayout.CENTER);
    }

    // guarda el menu dentro de los parámetros de la aplicación
    public void guardarMenu(MenuSemanal menu, JVentana app){
        CustomerControler.guardaMenu(app.getUsuario(), menu);
    }

}
