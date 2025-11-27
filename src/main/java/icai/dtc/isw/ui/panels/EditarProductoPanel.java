package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.ProductoControler;
import icai.dtc.isw.domain.Producto;
import icai.dtc.isw.domain.Unidad;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

import static icai.dtc.isw.ui.UiUtils.*;

public class EditarProductoPanel extends JPanel {

    private final JTextField codigoField = textField(20);
    private final JTextField nombreField = textField(20);
    private final JCheckBox kgCheckBox = new JCheckBox("KG");
    private final JCheckBox lCheckBox = new JCheckBox("L");
    private final JCheckBox uCheckBox = new JCheckBox("UNIDAD");
    private final JTextField precioField = textField(20);
    private JLabel t;

    // constructor del panel de edicion de usuario
    public EditarProductoPanel(JVentana app) {

        JScrollPane scrollPane = new JScrollPane(crearFormulario(app));

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {

                scrollPane.getVerticalScrollBar().setValue(0);

            }
        });

        setLayout(new BorderLayout());
        setBackground(BG);

        //Quitar scrollbar horizontal
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton btnBack = pillButton("Volver");
        btnBack.addActionListener(_ -> app.showCard("productosSuper"));


        add(t, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnBack, BorderLayout.SOUTH);

    }

    // obtiene la información del producto
    public JPanel crearFormulario(JVentana app) {

        setBackground(BG);
        t = title("Editar Producto");
        t.setBorder(new javax.swing.border.EmptyBorder(20, 5, 20, 5));
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(10, 25, 20, 25));

        codigoField.setText(app.getProducto().getId());
        nombreField.setText(app.getProducto().getNombre());
        precioField.setText(String.valueOf(app.getProducto().getPrecio()));

        if(app.getProducto().getUnidadP().equals(Unidad.u)){uCheckBox.setSelected(true);}
        else if(app.getProducto().getUnidadP().equals(Unidad.l)){lCheckBox.setSelected(true);}
        else if(app.getProducto().getUnidadP().equals(Unidad.kg)){kgCheckBox.setSelected(true);}

        form.add(labels("CÓDIGO DE IDENTIFICACIÓN"));
        form.add(fieldWrapWest(codigoField));

        form.add(labels("NOMBRE"));
        form.add(fieldWrapWest(nombreField));

        form.add(labels("PRECIO"));
        form.add(fieldWrapWest(precioField));

        form.add(labels("UNIDAD DE VENTA"));
        JPanel unidades = gridLayout();
        for (JCheckBox cb : Arrays.asList(kgCheckBox,lCheckBox,uCheckBox)) {
            estilizarCheck(cb);
            unidades.add(cb);
        }
        form.add(unidades);

        form.add(Box.createVerticalStrut(16));
        JButton btnRegistrar = pillButton("GUARDAR");
        btnRegistrar.addActionListener(_ -> onEditarProducto(app));

        form.add(Box.createVerticalStrut(16));
        form.add(btnRegistrar);

        return form;
    }

    // extrae los datos de GUI y se los manda al backend para llevar a cabo la lógica de edición de producto
    private void onEditarProducto(JVentana app) {
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
        boolean ok = ProductoControler.updateProducto(prod);
        if (ok) {
            app.onProdEditSuccess(prod);
        } else {
            JOptionPane.showMessageDialog(this, "La edición del producto no se pudo completar");
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
