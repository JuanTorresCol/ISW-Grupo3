package icai.dtc.isw.ui.panels;

import icai.dtc.isw.domain.Lista;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static icai.dtc.isw.ui.UiUtils.*;

public class ListaCompraPanel extends JPanel {

    public ListaCompraPanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel t = pillTitle("LISTA DE LA COMPRA");
        JPanel titulo = basePanel(new BorderLayout());
        titulo.add(t,BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        Lista lisa = new Lista(0,app.getMenuSemanal());

        ArrayList<String> excep = lisa.visualizarProductos();
        if(excep!=null){
            for(String prod : excep) {
                model.addElement(prod);
            }
        } else{model.addElement("Primero inserte un presupuesto");}
        JList<String> lista = new JList<>(model);
        lista.setFont(BODY);
        lista.setBackground(BG);

        //setBorder(BorderFactory.createEmptyBorder(0, , 0, 5));

        add(titulo, BorderLayout.NORTH);
        JScrollPane scroll_lista = new JScrollPane(lista);
        scroll_lista.setBorder(BorderFactory.createEmptyBorder(0,15,0,5));
        scroll_lista.setBackground(BG);
        add(scroll_lista, BorderLayout.CENTER);
        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);
    }
}
