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
    private JPanel form;

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

    public JPanel crearFormulario(JVentana app) {

        setBackground(BG);
        t = title("Añade un Producto");
        t.setBorder(new javax.swing.border.EmptyBorder(20, 5, 20, 5));
        form = new JPanel();
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


    private void resetFields() {
        UiUtils.clearTextBoxes(this);
        revalidate();
        repaint();
    }
    private void onNuevoProducto(JVentana app) {
        String name = nombreField.getText();
        int precio;
        if(precioField.getText() == null){
            precio = 0;
        } else if(Integer.parseInt(precioField.getText())<0){
            precio = 0;
        }else{
            precio = Integer.parseInt(precioField.getText());
        }

        Unidad unidad = null;
        if (kgCheckBox.isSelected()) unidad = Unidad.kg;
        if (lCheckBox.isSelected()) unidad = Unidad.l;
        if (uCheckBox.isSelected()) unidad = Unidad.u;

        boolean b = ProductoControler.registerProducto(
                new Producto(name, unidad, precio)
        );

        if (b) {
            app.onProdInsertSuccess(new Producto(name, unidad, precio));
        } else {
            if(name==null){JOptionPane.showMessageDialog(this,"Introduzca un nombre");
            } else if(precio==0){
                JOptionPane.showMessageDialog(this,"Introduzca un precio válido");
            } else if(unidad == null){
                JOptionPane.showMessageDialog(this,"Introduzca un unidad válida");
            } else{
                JOptionPane.showMessageDialog(this,"El registro del producto no se pudo completar");
            }
        }
    }
}

