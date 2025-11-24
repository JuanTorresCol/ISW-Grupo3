package icai.dtc.isw.ui.panels;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.domain.MenuSemanal;
import icai.dtc.isw.domain.MenuDiario;
import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.ui.JVentana;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static icai.dtc.isw.ui.UiUtils.*;

public class MenuDiaPanel extends JPanel {

    private final String[] diasSemana = {"LUNES","MARTES","MIÉRCOLES","JUEVES","VIERNES"};
    private int idxDia = 0;
    private final JLabel tituloDiaLabel;
    private MenuDiario menuDia;
    private final MenuSemanal menuSemanal;

    private final JVentana app;
    private final JPanel cards;

    private final JPanel tiraDias;
    private final List<JComponent> dayChips = new ArrayList<>();

    public MenuDiaPanel(JVentana app) {
        this.app = app;
        this.menuSemanal = app.getMenuSemanal();
        this.menuDia = this.menuSemanal.getLunes();

        setLayout(new BorderLayout());
        setBackground(BG);
        //setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        // Header con flechas
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 5, 0, 5));
        JButton prev = navArrow("<", _ -> cambiarDia(-1));
        JButton next = navArrow(">", _ -> cambiarDia(1));

        tituloDiaLabel = new JLabel(dayTitle(), SwingConstants.CENTER);
        tituloDiaLabel.setFont(H2);
        tituloDiaLabel.setForeground(TITLE);

        header.add(prev, BorderLayout.WEST);
        header.add(tituloDiaLabel, BorderLayout.CENTER);
        header.add(next, BorderLayout.EAST);


        tiraDias = flowCenter();
        String[] labels = {"L","M","X","J","V"};
        for (int i = 0; i < labels.length; i++) {
            final int dayIndex = i;
            JComponent chip = pillButton(labels[i]);
            chip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            chip.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    goToDay(dayIndex);
                }
            });
            dayChips.add(chip);
            tiraDias.add(chip);
        }

        // Cards de recetas
        cards = new JPanel();
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(10,10,10,10));
        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
        renderCards();
        updateChipSelection();

        add(header, BorderLayout.NORTH);
        add(stack(tiraDias, cards), BorderLayout.CENTER);
        add(bottomNav(
                _ -> app.showCard("menuDia"),
                _ -> app.showCard("listaCompra"),
                _ -> { app.setUsuario(app.cargarPerfilUsuario()); app.refreshCard("perfil"); app.showCard("perfil"); }
        ), BorderLayout.SOUTH);

    }

    private void cambiarDia(int delta) {
        int newIndex = (idxDia + delta + diasSemana.length) % diasSemana.length;
        goToDay(newIndex);
    }

    private void goToDay(int newIndex) {
        idxDia = newIndex;
        tituloDiaLabel.setText(dayTitle());

        switch (idxDia) {
            case 0 -> menuDia = menuSemanal.getLunes();
            case 1 -> menuDia = menuSemanal.getMartes();
            case 2 -> menuDia = menuSemanal.getMiercoles();
            case 3 -> menuDia = menuSemanal.getJueves();
            case 4 -> menuDia = menuSemanal.getViernes();
            default -> menuDia = null;
        }

        renderCards();
        updateChipSelection();
    }

    private void updateChipSelection() {
        for (int i = 0; i < dayChips.size(); i++) {
            JComponent c = dayChips.get(i);
            boolean selected = (i == idxDia);
            // subrayado inferior en el seleccionado
            c.setBorder(selected
                    ? BorderFactory.createMatteBorder(0, 0, 2, 0, TITLE)
                    : BorderFactory.createEmptyBorder(0, 0, 2, 0));
        }
        tiraDias.revalidate();
        tiraDias.repaint();
    }

    private String dayTitle() {
        return diasSemana[idxDia];
    }

    private void renderCards() {
        cards.removeAll();

        if (menuDia != null) {
            if (menuDia.getComida() != null) {
                cards.add(menuCard(
                        app,
                        menuDia.getComida(),
                        "Comida",
                        menuDia.getComida().getNombre(),
                        menuDia.getComida().getDuracion() + " mins",
                        String.valueOf(menuDia.getComida().getDificultad())
                ));
                cards.add(Box.createVerticalStrut(10));
            }

            if (menuDia.getCena() != null) {
                cards.add(menuCard(
                        app,
                        menuDia.getCena(),
                        "Cena",
                        menuDia.getCena().getNombre(),
                        menuDia.getCena().getDuracion() + " mins",
                        String.valueOf(menuDia.getCena().getDificultad())
                ));
            }
        } else {
            cards.add(new JLabel("No hay menú para este día."));
        }

        cards.revalidate();
        cards.repaint();
    }

    private JPanel menuCard(JVentana app, Receta receta, String bloque, String titulo, String tiempo, String dificultad) {
        JPanel card = roundedCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_OSCURO);

        JLabel cab = label(bloque);
        cab.setFont(H2);
        cab.setForeground(TITLE);
        card.add(center(cab));
        card.add(Box.createVerticalStrut(3));

        JPanel img = new JPanel();
        Dimension d = new Dimension(240,85);
        img.setPreferredSize(d);
        img.setMinimumSize(d);
        //img.setMaximumSize(d);
        img.setBackground(new Color(170, 187, 197));
        img.add(new JLabel("Imagen"));
        card.add(img);
        card.add(Box.createVerticalStrut(6));

        // borde aplicado a la card
        card.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JLabel name = new JLabel(titulo);
        name.setFont(H3);
        card.add(center(name));

        //card.add(Box.createVerticalStrut(3));
        JLabel meta = new JLabel("⏱ " + tiempo + "    🧾 " + dificultad);
        meta.setFont(SMALL);
        card.add(center(meta));
        //card.add(Box.createVerticalStrut(1));

        JPanel acciones = flowLeft();
        JButton ver = outlineButton("VER RECETA", _ -> app.showCard("recetaDetalle"));
        JButton cambiar = outlineButton("CAMBIAR", _ -> {
            app.setBloque(bloque);
            app.setDia(idxDia);
            app.refreshCard("recetasSimilares");
            app.showCard("recetasSimilares");
        });
        JButton guardar = outlineButton("GUARDAR", _ -> {
            app.getUsuario().anadirRecetaFav(receta);
            CustomerControler.refreshFavoritos(app.getUsuario());
            JOptionPane.showMessageDialog(this, "Receta guardada con éxito");
        });
        acciones.add(ver);
        acciones.add(cambiar);
        acciones.add(guardar);
        card.add(center(acciones));

        return card;
    }
}
