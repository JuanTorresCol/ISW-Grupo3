package icai.dtc.isw.ui.panels;

import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class ListaCompraPanel extends JPanel {

    public ListaCompraPanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = pillTitle("LISTA DE LA COMPRA");

        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("Pasta (200 g)");
        model.addElement("Pechuga de pollo (2 uds)");
        model.addElement("Ajo (2 dientes)");
        model.addElement("Cebolla (1/2)");
        model.addElement("Verduras variadas");
        JList<String> lista = new JList<>(model);
        lista.setFont(BODY);

        add(t, BorderLayout.NORTH);
        add(new JScrollPane(lista), BorderLayout.CENTER);
        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);
    }
}
