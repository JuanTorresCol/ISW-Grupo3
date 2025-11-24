package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.ProductoControler;
import icai.dtc.isw.controler.SupermercadoControler;
import icai.dtc.isw.domain.Producto;
import icai.dtc.isw.domain.Supermercado;
import icai.dtc.isw.domain.Unidad;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static icai.dtc.isw.ui.UiUtils.*;

import javax.swing.SwingWorker;
import icai.dtc.isw.ui.Refreshable;

public class ProductosSupermercadoPanel extends JPanel implements Refreshable {

    private final JVentana app;
    private final JPanel lista;

    public ProductosSupermercadoPanel(JVentana app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = pillTitle("PRODUCTOS");

        lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.add(center(new JLabel("Cargando productos...")));

        JScrollPane scroll = new JScrollPane(wrapCentered(lista));
        scroll.getViewport().setBackground(BG);
        scroll.setOpaque(false);
        scroll.setBackground(BG);
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Barra de botones abajo
        JPanel barra = new JPanel();
        barra.setOpaque(false);
        barra.setLayout(new BoxLayout(barra, BoxLayout.X_AXIS));
        JButton nuevo = flatLink("Nuevo Producto >", _ -> app.showCard("anadirNuevoProducto"));
        JButton exit  = flatLink("Salir >", _ -> app.showCard("perfilSupermercado"));
        barra.add(Box.createHorizontalGlue());
        barra.add(nuevo);
        barra.add(Box.createHorizontalStrut(10));
        barra.add(exit);

        add(center(t), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(barra, BorderLayout.SOUTH);

        refreshAsync();
    }

    @Override
    public void refreshAsync() {
        lista.removeAll();
        lista.add(center(new JLabel("Cargando productos...")));
        lista.revalidate();
        lista.repaint();

        new SwingWorker<ArrayList<Producto>, Void>() {
            @Override
            protected ArrayList<Producto> doInBackground() {
                return app.getSupermercado().getProductos();
            }

            @Override
            protected void done() {
                try {
                    ArrayList<Producto> productos = get();
                    lista.removeAll();
                    if (productos == null || productos.isEmpty()) {
                        lista.add(center(new JLabel("No hay productos aún.")));
                    } else {
                        for (Producto p : productos) {
                            lista.add(similarCard(p, p.getNombre(), p.getPrecio(), p.getUnidadP()));
                            lista.add(Box.createVerticalStrut(12));
                        }
                    }
                } catch (Exception ex) {
                    lista.removeAll();
                    lista.add(center(new JLabel("No se pudieron cargar los productos.")));
                }
                lista.revalidate();
                lista.repaint();
            }
        }.execute();
    }

    private JPanel similarCard(Producto producto, String nombre, double precio, Unidad unidad) {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout(10, 0));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(nombre);
        name.setFont(H3);
        JLabel meta = new JLabel("1 " + unidad + " a " + precio + "$");
        meta.setFont(SMALL);
        JButton sel = outlineButton("ELIMINAR", _ -> {
            app.getSupermercado().eliminarProducto(producto);
            eliminarProducto(app.getSupermercado(),producto);
            JOptionPane.showMessageDialog(this, "Eliminado exitosamente");
            app.refreshCard("perfilSupermercado");
            app.refreshCard("productosSuper");
            app.showCard("productosSuper");
        });

        info.add(name);
        info.add(meta);
        info.add(Box.createVerticalStrut(6));
        info.add(sel);

        card.add(info, BorderLayout.CENTER);
        return card;
    }
    public void eliminarProducto(Supermercado supermercado, Producto producto) {
        ProductoControler.eliminarProducto(producto);
        SupermercadoControler.addProducto(supermercado);
    }
}
