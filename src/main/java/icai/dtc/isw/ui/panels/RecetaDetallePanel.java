package icai.dtc.isw.ui.panels;

import icai.dtc.isw.domain.MenuSemanal;
import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import java.awt.*;

import static icai.dtc.isw.ui.UiUtils.*;

public class RecetaDetallePanel extends JPanel {

    public RecetaDetallePanel(JVentana app) {
        setLayout(new BorderLayout());
        setBackground(BG);

        Receta receta = findReceta(app);

        JPanel header = flowLeft();
        JButton back = flatLink("< ATRÁS", _ -> app.showCard("menuDia"));
        header.add(back);

        JLabel t = pillTitle(receta.getNombre().toUpperCase());


        JPanel ing = section("INGREDIENTES",
                receta.ingredientesToString());

        JPanel steps = section("INSTRUCCIONES",
                Receta.formatearDescripcion(receta.getDescripcion()));

        JPanel center = new JPanel();
        center.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(t);
        center.add(Box.createVerticalStrut(10));
        center.add(ing);
        center.add(Box.createVerticalStrut(10));
        center.add(steps);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);
    }

    public Receta findReceta(JVentana app){
        int dia = app.getDia();
        String bloque = app.getBloque();
        MenuSemanal menuS = app.getMenuSemanal();
        if(dia == 0){
            if(bloque.equals("Comida")){
                return menuS.getLunes().getComida();
            } else if(bloque.equals("Cena")){
                return menuS.getLunes().getCena();
            }
        }
        if(dia == 1){
            if(bloque.equals("Comida")){
                return menuS.getMartes().getComida();
            } else if(bloque.equals("Cena")){
                return menuS.getMartes().getCena();
            }
        }
        if(dia == 2){
            if(bloque.equals("Comida")){
                return menuS.getMiercoles().getComida();
            } else if(bloque.equals("Cena")){
                return menuS.getMiercoles().getCena();
            }
        }
        if(dia == 3){
            if(bloque.equals("Comida")){
                return menuS.getJueves().getComida();
            } else if(bloque.equals("Cena")){
                return menuS.getJueves().getCena();
            }
        }
        if(dia == 4){
            if(bloque.equals("Comida")){
                return menuS.getViernes().getComida();
            } else if(bloque.equals("Cena")){
                return menuS.getViernes().getCena();
            }
        }
        return null;
    }
}
