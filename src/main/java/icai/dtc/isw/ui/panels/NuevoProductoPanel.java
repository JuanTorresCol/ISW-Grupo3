package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.ProductoControler;
import icai.dtc.isw.domain.Producto;
import icai.dtc.isw.domain.Unidad;
import icai.dtc.isw.ui.JVentana;
import icai.dtc.isw.ui.UiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

import static icai.dtc.isw.ui.UiUtils.*;

public class NuevoProductoPanel extends JPanel {

    private final int ancho = 33;
    private final JTextField nombreField = textField(ancho);
    private final JTextField precioField = textField(ancho);
    private final JCheckBox kgCheckBox = new JCheckBox("KG");
    private final JCheckBox lCheckBox = new JCheckBox("L");
    private final JCheckBox uCheckBox = new JCheckBox("UNIDAD");
    private JLabel t;

    // constructor del panel que muestra las opciones para añadir un nuevo producto a los propios
    // de un supermercado
    public NuevoProductoPanel(JVentana app) {
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

        //Quitar scrollbar horizontal
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton btnBack = pillButton("Volver al perfil");
        btnBack.addActionListener(_ -> app.showCard("perfilSupermercado"));

        add(t, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        //add(center(btnBack) , BorderLayout.SOUTH);
        add(btnBack , BorderLayout.SOUTH);
    }

    // crea el panel en el que el supermercado introduce información sobre el nuevo producto
    public JPanel crearFormulario(JVentana app) {

        setBackground(BG);
        t = title("Añade un Producto");
        t.setBorder(new javax.swing.border.EmptyBorder(20, 5, 20, 5));
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.setBorder(new EmptyBorder(10, 25, 20, 25));

        form.add(Box.createVerticalStrut(20));
        form.add(labels("NOMBRE"));
        form.add(fieldWrap(nombreField,new Dimension(400,30)));
        form.add(labels("PRECIO"));
        form.add(fieldWrap(precioField,new Dimension(400,30)));

        form.add(labels("UNIDAD DE VENTA"));
        JPanel unidades = gridLayout();
        for (JCheckBox cb : Arrays.asList(kgCheckBox,lCheckBox,uCheckBox)) {
            estilizarCheck(cb);
            unidades.add(cb);
        }
        form.add(unidades);

        form.add(Box.createVerticalStrut(30));
        JButton btnGuardar = (pillButton("GUARDAR"));

        btnGuardar.addActionListener(_ -> onNuevoProducto(app));
        form.add(btnGuardar);

        return form;
    }

    // elimina el texto el las celdas
    private void resetFields() {
        UiUtils.clearTextBoxes(this);
        revalidate();
        repaint();
    }

    // obtiene los datos del GUI y los manda al backend para que lleve a cabo la lógica
    private void onNuevoProducto(JVentana app) {
        String name = nombreField.getText().trim();
        double precio;
        try {
            String ptxt = precioField.getText().trim().replace(",", ".");
            precio = Double.parseDouble(ptxt);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Introduzca un precio numérico válido");
            return;
        }
        if (precio <= 0) {
            JOptionPane.showMessageDialog(this, "Introduzca un precio mayor que 0");
            return;
        }

        Unidad unidad = unidadSeleccionada();

        Producto prod = new Producto(name, unidad, precio);
        boolean ok = ProductoControler.registerProducto(prod);
        if (ok) {
            app.onProdInsertSuccess(prod);
        } else {
            JOptionPane.showMessageDialog(this, "El registro del producto no se pudo completar");
        }
    }

    // devuelve la unidad que ha sido seleccionada de los JCheckBox
    private Unidad unidadSeleccionada() {
        if (kgCheckBox.isSelected()) return Unidad.kg;
        else if (lCheckBox.isSelected())  return Unidad.l;
        else if (uCheckBox.isSelected())  return Unidad.u;
        JOptionPane.showMessageDialog(this, "Seleccione una unidad de venta");
        return null;
    }
}

