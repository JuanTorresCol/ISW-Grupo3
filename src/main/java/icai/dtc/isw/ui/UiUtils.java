package icai.dtc.isw.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;

public final class UiUtils {
    private UiUtils() {}

    // Paleta
    public static final Color BG = new Color(207, 224, 234);
    public static final Color BG_OSCURO = new Color(187, 204, 214);
    public static final Color ACCENT = new Color(186, 151, 154);
    public static final Color TITLE = new Color(35, 78, 69);
    public static final Color CARD_BG = new Color(235, 241, 246);
    public static final Color TEXT = new Color(40, 40, 40);

    // Fuentes
    public static final Font H = new Font("Arial", Font.BOLD, 40);
    public static final Font H1 = new Font("Arial", Font.BOLD, 32);
    public static final Font H2 = new Font("Arial", Font.BOLD, 24);
    public static final Font H3 = new Font("Arial", Font.BOLD, 18);
    public static final Font BODY = new Font("Arial", Font.PLAIN, 14);
    public static final Font SMALL = new Font("Arial", Font.PLAIN, 12);

    public static JPanel basePanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(20, 10, 20, 10));
        return p;
    }
    public static JPanel margen(){
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(0, 5, 0, 5));
        return p;
    }

    public static void clearTextBoxes(Container root) {
        for (Component c : root.getComponents()) {
            if (c instanceof JSpinner spinner) {

                spinner.setValue(18);
                continue;
            }
            if (c instanceof JFormattedTextField f) {
                f.setValue(null);      // mejor para formateados
                f.setText("");
            } else if (c instanceof JTextComponent t) {
                t.setText("");
            } else if (c instanceof Container child) {
                clearTextBoxes(child); // recursivo para limpiar todo el árbol
            }
        }
    }

    // utiles: botones, cajas de texto y colocación de elementos
    public static JLabel title(String s) {
        JLabel l = new JLabel(s, SwingConstants.CENTER);
        l.setFont(H1);
        l.setForeground(TITLE);
        l.setBorder(new EmptyBorder(10,0,10,0));
        return l;
    }

    public static JLabel label(String s) {
        JLabel l = new JLabel(s);
        l.setFont(H3);
        l.setForeground(TITLE);
        l.setBorder(new EmptyBorder(5,2,0,2));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }



    public static JLabel labels(String s) {
        JLabel l = new JLabel(s);
        l.setFont(H3);
        l.setForeground(TITLE);
        l.setBorder(new EmptyBorder(30,2,8,2));
        return l;
    }

    public static JLabel labelBold(String s) {
        JLabel l = new JLabel(s);
        l.setFont(H3);
        l.setForeground(TITLE);
        return l;
    }

    public static JLabel labelBig(String s) {
        JLabel l = new JLabel(s);
        l.setFont(H);
        l.setForeground(Color.DARK_GRAY);
        return l;
    }

    public static Component fieldWrap(Component c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        c.setFont(BODY);
        p.add(c, BorderLayout.CENTER);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    public static Component fieldWrapWest(Component c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        c.setFont(BODY);
        p.add(c, BorderLayout.WEST);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    public static Component fieldWrap(Component c, Dimension size) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        c.setFont(BODY);
        p.add(c, BorderLayout.CENTER);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (size != null) {
            p.setPreferredSize(size);
            p.setMaximumSize(size);
            p.setMinimumSize(size);
        }
        return p;
    }

    public static JPanel gridLayout(){
        JPanel p = new JPanel(new GridLayout(0,3,3,3));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;

    }

    public static JPanel flowLeft() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        p.setOpaque(false);
        return p;
    }

    public static JPanel flowCenter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        p.setOpaque(false);
        return p;
    }

    public static Component center(Component c) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    public static JPanel wrapCentered(Component c) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    public static JPanel stack(Component top, Component mid) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        if (top instanceof JComponent t) t.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (mid instanceof JComponent m) m.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(top);
        p.add(Box.createVerticalStrut(10));
        p.add(mid);
        return p;
    }

    // Controles
    public static JButton pillButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setForeground(Color.DARK_GRAY);
        b.setBackground(ACCENT);
        b.setUI(new BasicButtonUI());
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JButton flatLink(String text, ActionListener al) {
        JButton b = new JButton(text);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setForeground(TITLE.darker());
        b.setFont(BODY);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(al);
        return b;
    }

    public static JButton navArrow(String text, ActionListener al) {
        JButton b = new JButton(text);
        b.setFont(H2);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setForeground(TITLE);
        b.addActionListener(al);
        return b;
    }

    public static JLabel pillLabel(String text) {
        JLabel l = new JLabel(text + " ");
        l.setOpaque(true);
        l.setBackground(ACCENT);
        l.setForeground(Color.DARK_GRAY);
        l.setFont(H3);
        l.setBorder(new EmptyBorder(6,12,6,12));
        return l;
    }

    public static JPanel roundedCard() {
        JPanel p = new JPanel();
        p.setBackground(CARD_BG);
        p.setBorder(new EmptyBorder(12, 14, 12, 14));
        p.setLayout(new BorderLayout());
        return p;
    }

    public static JLabel pillTitle(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(H2);
        l.setOpaque(true);
        l.setBackground(ACCENT);
        l.setForeground(Color.DARK_GRAY);
        l.setBorder(new EmptyBorder(8,12,8,12));
        return l;
    }

    public static JButton chip(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setBackground(new Color(136, 156, 166));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        return b;
    }

    public static void estilizarCheck(JCheckBox cb) {
        cb.setOpaque(false);
        cb.setFont(BODY);
        cb.setForeground(TEXT);
    }

    public static JPanel keyValue(String key, String value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        JLabel k = labelBold(key + " ");
        JLabel v = new JLabel(value);
        v.setFont(BODY);
        p.add(k); p.add(v);
        return p;
    }

    public static JButton outlineButton(String text, ActionListener al) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TITLE, 1),
                new EmptyBorder(6, 8, 6, 8)
        ));
        b.setContentAreaFilled(false);
        b.addActionListener(al);
        return b;
    }

    public static JPanel section(String titulo, String cuerpo) {
        JPanel cont = new JPanel();
        cont.setOpaque(false);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        JLabel t = labelBold(titulo);
        t.setFont(H2);
        JTextArea area = new JTextArea(cuerpo);
        area.setEditable(false);
        area.setFont(BODY);
        area.setBackground(BG);
        area.setBorder(new EmptyBorder(10,20,10,20));
        cont.add(t);
        cont.add(Box.createVerticalStrut(4));
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(BorderFactory.createEmptyBorder());
        cont.add(sp);
        return cont;
    }

    // Barra inferior MENÚ / LISTA / PERFIL
    public static JPanel bottomNav(ActionListener onMenu, ActionListener onLista, ActionListener onPerfil) {
        JPanel nav = new JPanel(new GridLayout(1,3,12,0));
        nav.setOpaque(true);
        nav.setBackground(ACCENT);
        nav.setBorder(new EmptyBorder(10, 0, 10, 0));
        nav.add(navItem("MENÚ", onMenu));
        nav.add(navItem("LISTA", onLista));
        nav.add(navItem("PERFIL", onPerfil));
        return nav;
    }

    //creacion JTextField
    public static JTextField textField(int colums){
        JTextField textField = new JTextField(colums);
        textField.setBorder(new javax.swing.border.LineBorder(TITLE));
        //textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return textField;
    }

    //creacion passwordField
    public static JPasswordField passwordField(int colums){
        JPasswordField passwordField = new JPasswordField(colums);
        passwordField.setBorder(new javax.swing.border.LineBorder(TITLE));
        return passwordField;
    }

    //creacion comboBox
    public static JComboBox<String> comboBox(String[] string){
        JComboBox<String> comboBox = new JComboBox<>(string);
        comboBox.setBorder(new javax.swing.border.LineBorder(TITLE));
        return comboBox;
    }

    //creacion spinner
    public static JSpinner spinner(int inicial, int minimo, int maximo, int salto){
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(inicial, minimo, maximo, salto));
        spinner.setBorder(new javax.swing.border.LineBorder(TITLE));
        return spinner;
    }

    public static void fuenteSpinner(JSpinner spinner, Font fuente){

        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setFont(fuente);
        editor.getTextField().setForeground(Color.DARK_GRAY);
    }

    public static void fondoSpinner(JSpinner spinner, Color color){
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setBackground(color);
    }



    // Botones de navegacion
    private static JButton navItem(String text, ActionListener al) {
        JButton b = new JButton(text);
        b.setFont(H3);
        b.setForeground(Color.DARK_GRAY);
        b.setBackground(ACCENT);
        b.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
        b.setFocusPainted(false);
        b.addActionListener(al);
        return b;
    }



    // Recursos
    // Carga una imagen desde resources
    public static ImageIcon cargarIcono(Class<?> clazz, String nombreImagen, int ancho, int alto){
        String ruta = "/imageResources/"+nombreImagen+".png";
        java.net.URL url = clazz.getResource(ruta);
        ImageIcon icono = url != null ? new ImageIcon(url) : null;
        Image iconoEscalado = icono != null ? icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH) : null;
        System.out.println("IconoEscalado: "+iconoEscalado);
        return iconoEscalado != null ? new ImageIcon(iconoEscalado): null;
    }
}
