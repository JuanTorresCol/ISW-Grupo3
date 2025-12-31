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

    // constructor del panel que alberga las cards de los productos del supermercado
    public ProductosSupermercadoPanel(JVentana app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = pillTitle("PRODUCTOS");
        JPanel titulo = new JPanel(new FlowLayout());
        titulo.setOpaque(false);
        titulo.add(Box.createVerticalStrut(20));
        titulo.add(t);

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


        add(center(titulo), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(barra, BorderLayout.SOUTH);

        refreshAsync();
    }

    // el proceso de carga de los productos se lleva a cabo en el background ya que es pesado
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

    // contiene la información de un producto
    private JPanel similarCard(Producto producto, String nombre, double precio, Unidad unidad) {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout(10, 0));

        int CARD_WIDTH = 350;
        int CARD_HEIGHT = 60;
        Dimension cardSize = new Dimension(CARD_WIDTH, CARD_HEIGHT);

        card.setPreferredSize(cardSize);
        card.setMaximumSize(cardSize);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(nombre);
        name.setFont(H3);

        JLabel meta = new JLabel("1 " + unidad + " a " + precio + "$");
        meta.setFont(SMALL);

        textPanel.add(name);
        textPanel.add(meta);

        JButton edit = outlineButton("EDITAR", _ ->{
            app.setProducto(producto);
            app.refreshCard("editarProducto");
            app.showCard("editarProducto");
        });

        JButton sel = outlineButton("ELIMINAR", _ -> {
            app.getSupermercado().eliminarProducto(producto);
            eliminarProducto(app.getSupermercado(), producto);
            JOptionPane.showMessageDialog(this, "Eliminado exitosamente");
            app.refreshCard("perfilSupermercado");
            app.refreshCard("productosSuper");
            app.showCard("productosSuper");
        });

        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setOpaque(false);
        buttonWrapper.setLayout(new BoxLayout(buttonWrapper, BoxLayout.X_AXIS));
        buttonWrapper.add(edit);
        buttonWrapper.add(Box.createHorizontalStrut(8));
        buttonWrapper.add(sel);

        info.add(textPanel, BorderLayout.CENTER);
        info.add(buttonWrapper, BorderLayout.EAST);

        card.add(info, BorderLayout.CENTER);

        return card;
    }


    // opción para eliminar un producto de los que tiene el supermercado en stock
    public void eliminarProducto(Supermercado supermercado, Producto producto) {
        ProductoControler.eliminarProducto(producto);
        SupermercadoControler.addProducto(supermercado);
    }
}
