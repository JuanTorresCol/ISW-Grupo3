package icai.dtc.isw.ui;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.dao.CustomerDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Map;

public class JVentana extends JFrame {

    // --- Controlador / estado ---
    private final CustomerControler controler = new CustomerControler();
    private String customerId;

    // --- Layout raíz ---
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // --- Paleta  ---
    private final Color BG = new Color(207, 224, 234);
    private final Color ACCENT = new Color(186, 151, 154);
    private final Color TITLE = new Color(35, 78, 69);
    private final Color CARD_BG = new Color(235, 241, 246);
    private final Color TEXT = new Color(40, 40, 40);

    // --- Fuentes ---
    private final Font H1 = new Font("Arial", Font.BOLD, 32);
    private final Font H2 = new Font("Arial", Font.BOLD, 24);
    private final Font H3 = new Font("Arial", Font.BOLD, 18);
    private final Font BODY = new Font("Arial", Font.PLAIN, 14);
    private final Font SMALL = new Font("Arial", Font.PLAIN, 12);

    // --- Componentes usados en varias pantallas ---
    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JPasswordField confirmarContrasenaField;
    private JComboBox<String> sexoComboBox;
    private JSpinner edadSpinner;
    private JCheckBox glutenCheckBox, lactosaCheckBox, huevoCheckBox, frutosSecosCheckBox, pescadoCheckBox, mariscoCheckBox, otroCheckBox;
    private JTextField otroAlergiaField;
    private JTextField alimentosNoComeArea;

    // Presupuesto
    private JSpinner presupuestoSpinner;

    // Menú por día
    private final String[] diasSemana = {"LUNES","MARTES","MIÉRCOLES","JUEVES","VIERNES"};
    private int idxDia = 0;
    private JLabel tituloDiaLabel;

    public JVentana() {
        setTitle("MENUMASTER");
        //setSize(450,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        getContentPane().setBackground(BG);

        inicializarComponentes();
        configurarInterfaz();
    }

    // ---------- Inicialización de widgets base ----------
    private void inicializarComponentes() {
        usuarioField = new JTextField(20);
        contrasenaField = new JPasswordField(20);
        confirmarContrasenaField = new JPasswordField(20);

        sexoComboBox = new JComboBox<>(new String[]{"HOMBRE","MUJER","OTRO"});
        edadSpinner = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));

        // Checkboxes para alergias
        glutenCheckBox = new JCheckBox("GLUTEN");
        lactosaCheckBox = new JCheckBox("LACTOSA");
        huevoCheckBox = new JCheckBox("HUEVO");
        frutosSecosCheckBox = new JCheckBox("FRUTOS SECOS");
        pescadoCheckBox = new JCheckBox("PESCADO");
        mariscoCheckBox = new JCheckBox("MARISCO");
        otroCheckBox = new JCheckBox("OTRO");
        otroAlergiaField = new JTextField(10);

        alimentosNoComeArea = new JTextField(20);

        presupuestoSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 5000, 5));
        ((JSpinner.DefaultEditor) presupuestoSpinner.getEditor()).getTextField().setColumns(6);
    }

    // ---------- Construcción de pantallas ----------
    private void configurarInterfaz() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BG);

        // ------------ Paneles a usar -------------------

        mainPanel.add(crearPanelInicio(), "inicio");
        mainPanel.add(new JScrollPane(crearPanelRegistro()), "registro");
        mainPanel.add(crearPanelLogin(), "login");
        mainPanel.add(crearPanelPresupuesto(), "presupuesto");
        mainPanel.add(crearPanelMenuDia(), "menuDia");
        mainPanel.add(new JScrollPane(crearPanelRecetaDetalle()), "recetaDetalle");
        mainPanel.add(new JScrollPane(crearPanelRecetasSimilares()), "recetasSimilares");
        mainPanel.add(new JScrollPane(crearPanelListaCompra()), "listaCompra");
        mainPanel.add(new JScrollPane(crearPanelPerfil()), "perfil");

        add(mainPanel);
    }

    // --- Pantalla 1: Master MENU ---
    private JPanel crearPanelInicio() {
        JPanel panel = basePanel(new BorderLayout());


        JLabel labelLogo = new JLabel(cargarIcono("logotipo",500,300),SwingConstants.CENTER);
        //titulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelLogo.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        JPanel botonesPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 100, 50));
        botonesPanel.setBackground(Color.WHITE);



        //conectar con métodos
        JPanel botones = new JPanel(new GridLayout(2,1,0,30));
        botones.setOpaque(false);
        JButton btnLogin = pillButton("Iniciar sesión");
        JButton btnRegistro = pillButton("Registrarse");
        btnLogin.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        btnRegistro.addActionListener(e -> cardLayout.show(mainPanel, "registro"));
        botones.add(btnLogin);
        botones.add(btnRegistro);

        panel.add(labelLogo, BorderLayout.NORTH);
        panel.add(wrapCentered(botones), BorderLayout.CENTER);

        return panel;
    }

    // --- Pantalla 2: Registro ---
    private JPanel crearPanelRegistro() {
        JPanel panel = basePanel(new BorderLayout());

        JLabel t = title("Crea tu cuenta");
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(10, 10, 20, 10));

        form.add(label("USUARIO"));
        form.add(usuarioField);

        form.add(label("CONTRASEÑA"));
        form.add(fieldWrap(contrasenaField));

        form.add(label("CONFIRMAR CONTRASEÑA"));
        form.add(fieldWrap(confirmarContrasenaField));

        form.add(label("SEXO"));
        form.add(fieldWrap(sexoComboBox));

        form.add(label("EDAD"));
        form.add(fieldWrap(edadSpinner));

        form.add(label("ALERGIAS/INTOLERANCIAS"));
        JPanel alergias = flowLeft();
        for (JCheckBox cb : Arrays.asList(glutenCheckBox,lactosaCheckBox,huevoCheckBox,frutosSecosCheckBox,pescadoCheckBox,mariscoCheckBox,otroCheckBox)) {
            estilizarCheck(cb);
            alergias.add(cb);
        }
        alergias.add(otroAlergiaField);
        form.add(alergias);

        form.add(label("ALIMENTOS QUE NO COMES"));
        form.add(fieldWrap(alimentosNoComeArea));

        JButton btnRegistrar = pillButton("REGISTRARSE");
        btnRegistrar.addActionListener(e -> onRegistrar());

        JButton btnBack = pillButton("Volver al inicio");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel,"inicio"));
        JPanel actions = flowCenter();
        actions.add(btnRegistrar);

        panel.setBorder(BorderFactory.createEmptyBorder(0, 250, 0, 250));
        panel.add(t, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(stack(actions, btnBack), BorderLayout.SOUTH);
        return panel;
    }

    // --- Pantalla 3: Login ---
    private JPanel crearPanelLogin() {
        JPanel panel = basePanel(new BorderLayout());
        JLabel t = title("Inicio de sesión");
        t.setBorder(new EmptyBorder(120, 40, 20, 40));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(40, 330, 10, 200));

        JTextField usuarioLoginField = new JTextField(20);
        JPasswordField contrasenaLoginField = new JPasswordField(20);

        form.add(labels("USUARIO"));
        form.add(fieldWrap(usuarioLoginField,new Dimension(600,50)));
        form.add(labels("CONTRASEÑA"));
        form.add(fieldWrap(contrasenaLoginField,new Dimension(600,50)));

        JButton btnEntrar = pillButton("Entrar");
        btnEntrar.addActionListener(e -> {
            String userName = usuarioLoginField.getText().trim();
            String pass = new String(contrasenaLoginField.getPassword());
            Customer customerCheck = CustomerDAO.getCliente(userName);
            if (customerCheck != null && pass.equals(customerCheck.getUserPass())) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
                customerId = customerCheck.getUserId();
                cardLayout.show(mainPanel, "presupuesto");
            } else {
                JOptionPane.showMessageDialog(this, "Inicio de sesión fallido");
            }
        });

        JButton btnBack = pillButton("Volver al inicio");
        btnBack.addActionListener(e -> cardLayout.show(mainPanel,"inicio"));

        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));
        panel.add(t, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(stack(center(btnEntrar), btnBack), BorderLayout.SOUTH);
        return panel;
    }

    // --- Pantalla 4: Presupuesto ---
    private JPanel crearPanelPresupuesto() {
        JPanel panel = basePanel(new BorderLayout());
        JLabel t = title("Introducir presupuesto semanal");

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JPanel dinero = flowCenter();
        dinero.add(pillLabel("€"));
        dinero.add(presupuestoSpinner);

        JButton generar = pillButton("Generar menú");
        generar.addActionListener(e -> cardLayout.show(mainPanel, "menuDia"));

        center.add(Box.createVerticalStrut(10));
        center.add(dinero);
        center.add(Box.createVerticalStrut(20));
        center.add(center(generar));

        panel.add(t, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(bottomNav(), BorderLayout.SOUTH);
        return panel;
    }

    // --- Pantalla 5: Menú por día ---
    private JPanel crearPanelMenuDia() {
        JPanel panel = basePanel(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JButton prev = navArrow("<", e -> cambiarDia(-1));
        JButton next = navArrow(">", e -> cambiarDia(1));
        tituloDiaLabel = new JLabel(dayTitle(), SwingConstants.CENTER);
        tituloDiaLabel.setFont(H2);
        tituloDiaLabel.setForeground(TITLE);
        header.add(prev, BorderLayout.WEST);
        header.add(tituloDiaLabel, BorderLayout.CENTER);
        header.add(next, BorderLayout.EAST);

        JPanel tiraDias = flowCenter();
        for (String c : new String[]{"L","M","X","J","V"}) {
            tiraDias.add(chip(c));
        }

        JPanel cards = new JPanel();
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(10,20,10,20));
        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
        cards.add(menuCard("Comida", "PASTA CON POLLO", "30 mins", "FÁCIL"));
        cards.add(Box.createVerticalStrut(16));
        cards.add(menuCard("Cena", "PASTA CON POLLO", "30 mins", "FÁCIL"));

        panel.add(header, BorderLayout.NORTH);
        panel.add(stack(tiraDias, cards), BorderLayout.CENTER);
        panel.add(bottomNav(), BorderLayout.SOUTH);
        return panel;
    }

    // --- Pantalla 6: Detalle de receta ---
    private JPanel crearPanelRecetaDetalle() {
        JPanel panel = basePanel(new BorderLayout());

        JPanel header = flowLeft();
        JButton back = flatLink("< ATRÁS", e -> cardLayout.show(mainPanel, "menuDia"));
        header.add(back);

        JLabel t = pillTitle("RECETA");
        JPanel hero = roundedCard();
        hero.setLayout(new BorderLayout());
        hero.add(pillTitle("PASTA CON POLLO"), BorderLayout.CENTER);

        // Ingredientes
        JPanel ing = section("INGREDIENTES",
                "- 80-100 g de pasta\n- 1 pechuga de pollo pequeña\n- 1 diente de ajo\n- 1/4 de cebolla\n- 100 ml de nata (opcional)\n- Aceite de oliva, sal, pimienta\n- Queso rallado (opcional)\n- Verduras al gusto");

        // Instrucciones
        JPanel steps = section("INSTRUCCIONES",
                "1. Cocer la pasta.\n2. Saltear el pollo.\n3. Añadir verduras.\n4. Unir todo con la pasta y servir.");

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(t);
        center.add(Box.createVerticalStrut(10));
        center.add(hero);
        center.add(Box.createVerticalStrut(10));
        center.add(ing);
        center.add(Box.createVerticalStrut(10));
        center.add(steps);

        panel.add(header, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(bottomNav(), BorderLayout.SOUTH);
        return panel;
    }

    // --- Pantalla 7: Recetas similares  ---
    private JPanel crearPanelRecetasSimilares() {
        JPanel panel = basePanel(new BorderLayout());
        JLabel t = pillTitle("RECETAS SIMILARES");

        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        for (int i=0;i<4;i++) {
            lista.add(similarCard("POLLO AL AJILLO CON VERDURAS", "30 mins", "FÁCIL"));
            lista.add(Box.createVerticalStrut(12));
        }

        panel.add(t, BorderLayout.NORTH);
        panel.add(wrapCentered(lista), BorderLayout.CENTER);
        panel.add(bottomNav(), BorderLayout.SOUTH);
        return panel;
    }

    // --- Pantalla 8: Lista de la compra  ---
    private JPanel crearPanelListaCompra() {
        JPanel panel = basePanel(new BorderLayout());
        JLabel t = pillTitle("LISTA DE LA COMPRA");

        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("Pasta (200 g)");
        model.addElement("Pechuga de pollo (2 uds)");
        model.addElement("Ajo (2 dientes)");
        model.addElement("Cebolla (1/2)");
        model.addElement("Verduras variadas");
        JList<String> lista = new JList<>(model);
        lista.setFont(BODY);

        panel.add(t, BorderLayout.NORTH);
        panel.add(new JScrollPane(lista), BorderLayout.CENTER);
        panel.add(bottomNav(), BorderLayout.SOUTH);
        return panel;
    }

    // --- Pantalla 9: Perfil  ---
    private JPanel crearPanelPerfil() {
        JPanel panel = basePanel(new BorderLayout());
        JLabel t = pillTitle("PERFIL");

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 30, 30, 30));

        // Header de perfil
        JPanel cab = roundedCard();
        cab.setLayout(new BoxLayout(cab, BoxLayout.Y_AXIS));
        JLabel user = new JLabel("MARIA123", SwingConstants.CENTER);
        user.setAlignmentX(Component.CENTER_ALIGNMENT);
        user.setFont(H3);
        JButton editar = pillButton("Editar perfil");
        editar.setAlignmentX(Component.CENTER_ALIGNMENT);
        editar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de edición en desarrollo"));
        cab.add(center(user));
        cab.add(Box.createVerticalStrut(6));
        cab.add(center(editar));

        // Datos
        content.add(t);
        content.add(Box.createVerticalStrut(10));
        content.add(cab);
        content.add(Box.createVerticalStrut(14));

        content.add(keyValue("SEXO:", "MUJER"));
        content.add(keyValue("EDAD:", "21"));
        content.add(keyValue("ALERGIAS/INTOLERANCIAS:", "LACTOSA, FRUTOS SECOS, MAÍZ, HUEVO"));
        content.add(keyValue("ALIMENTOS QUE NO COMES:", "ALUBIAS, PLÁTANOS"));

        content.add(Box.createVerticalStrut(12));
        JLabel prev = labelBold("VER MENÚS ANTERIORES");
        JButton s1 = flatLink("Hace 1 semana >", e -> JOptionPane.showMessageDialog(this, "Histórico en desarrollo"));
        JButton s2 = flatLink("Hace 2 semanas >", e -> JOptionPane.showMessageDialog(this, "Histórico en desarrollo"));
        content.add(prev);
        content.add(center(s1));
        content.add(center(s2));

        panel.add(content, BorderLayout.CENTER);
        panel.add(bottomNav(), BorderLayout.SOUTH);
        return panel;
    }

    // ---------- Acciones ----------
    private void onRegistrar() {
        String userName = usuarioField.getText();
        String pass = new String(contrasenaField.getPassword());
        String passCheck = new String(confirmarContrasenaField.getPassword());
        String sexo = sexoComboBox.getSelectedItem().toString();
        int edad = (int) edadSpinner.getValue();
        ArrayList<String> seleccionAlergia = new ArrayList<>();
        String alimentosNoCome = alimentosNoComeArea.getText();

        if (glutenCheckBox.isSelected()) seleccionAlergia.add("Gluten");
        if (lactosaCheckBox.isSelected()) seleccionAlergia.add("Lactosa");
        if (huevoCheckBox.isSelected()) seleccionAlergia.add("Huevo");
        if (frutosSecosCheckBox.isSelected()) seleccionAlergia.add("Frutos secos");
        if (pescadoCheckBox.isSelected()) seleccionAlergia.add("Pescado");
        if (mariscoCheckBox.isSelected()) seleccionAlergia.add("Marisco");

        Map.Entry<Customer, String> resultado = controler.realizarRegistro(userName, pass, passCheck, sexo, edad, seleccionAlergia, alimentosNoCome);
        if (resultado.getValue().equals("b")) {
            JOptionPane.showMessageDialog(this, "Registro completado");
            customerId = resultado.getKey().getUserId();
            cardLayout.show(mainPanel, "pantalla principal");
        } else {
            JOptionPane.showMessageDialog(this, "El registro no se pudo completar");
            cardLayout.show(mainPanel, "inicio");
        }
    }

    private void cambiarDia(int delta) {
        idxDia = (idxDia + delta + diasSemana.length) % diasSemana.length;
        tituloDiaLabel.setText(dayTitle());
        // Aquí podrías recargar tarjetas con recetas del día idxDia
    }

    private String dayTitle() {
        return "< " + diasSemana[idxDia] + " >";
    }

    // ---------- Componentes reutilizables de estilo ----------
    private JPanel basePanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        return p;
    }

    private JLabel title(String s) {
        JLabel l = new JLabel(s, SwingConstants.CENTER);
        l.setFont(H1);
        l.setForeground(TITLE);
        l.setBorder(new EmptyBorder(10,0,10,0));
        return l;
    }

    private JLabel label(String s) {
        JLabel l = new JLabel(s);
        l.setFont(H3);
        l.setForeground(TITLE);
        l.setBorder(new EmptyBorder(8,2,4,2));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private JLabel labels(String s) {
        JLabel l = new JLabel(s);
        l.setFont(H3);
        l.setForeground(TITLE);
        l.setBorder(new EmptyBorder(8,2,4,2));
        return l;
    }

    private JLabel labelBold(String s) {
        JLabel l = new JLabel(s);
        l.setFont(H3);
        l.setForeground(TITLE);
        return l;
    }

    private Component fieldWrap(Component c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        c.setFont(BODY);
        p.add(c, BorderLayout.CENTER);
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        return p;
    }
    private Component fieldWrap(Component c, Dimension size) {
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

    private JPanel flowLeft() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        p.setOpaque(false);
        return p;
    }

    private JPanel flowCenter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        p.setOpaque(false);
        return p;
    }

    private Component center(Component c) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    private JPanel wrapCentered(Component c) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    private JPanel stack(Component top, Component mid) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        if (top instanceof JComponent t) {
            t.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        if (mid instanceof JComponent m) {
            m.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        p.add(top);
        p.add(Box.createVerticalStrut(10));
        p.add(mid);
        return p;
    }

    private JPanel stack(Component top, Component mid, Component bottom) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(top);
        p.add(Box.createVerticalStrut(10));
        p.add(mid);
        p.add(Box.createVerticalStrut(10));
        p.add(bottom);
        return p;
    }

    private JButton pillButton(String text) {
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

    private JButton flatLink(String text, ActionListener al) {
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

    private JButton navArrow(String text, ActionListener al) {
        JButton b = new JButton(text);
        b.setFont(H2);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setForeground(TITLE);
        b.addActionListener(al);
        return b;
    }

    private JLabel pillLabel(String text) {
        JLabel l = new JLabel(text + " ");
        l.setOpaque(true);
        l.setBackground(ACCENT);
        l.setForeground(Color.DARK_GRAY);
        l.setFont(H3);
        l.setBorder(new EmptyBorder(6,12,6,12));
        return l;
    }

    private JPanel roundedCard() {
        JPanel p = new JPanel();
        p.setBackground(CARD_BG);
        p.setBorder(new EmptyBorder(12, 14, 12, 14));
        p.setLayout(new BorderLayout());
        return p;
    }

    private JLabel pillTitle(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(H2);
        l.setOpaque(true);
        l.setBackground(ACCENT);
        l.setForeground(Color.DARK_GRAY);
        l.setBorder(new EmptyBorder(8,12,8,12));
        return l;
    }

    private JButton chip(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setBackground(new Color(136, 156, 166));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        return b;
    }

    private void estilizarCheck(JCheckBox cb) {
        cb.setOpaque(false);
        cb.setFont(BODY);
        cb.setForeground(TEXT);
    }

    private JPanel keyValue(String key, String value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        JLabel k = labelBold(key + " ");
        JLabel v = new JLabel(value);
        v.setFont(BODY);
        p.add(k); p.add(v);
        return p;
    }

    // Tarjeta de menú
    private JPanel menuCard(String bloque, String titulo, String tiempo, String dificultad) {
        JPanel card = roundedCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel cab = new JLabel(bloque);
        cab.setFont(H2);
        cab.setForeground(TITLE);
        card.add(cab);
        card.add(Box.createVerticalStrut(6));

        // “Imagen” placeholder
        JPanel img = new JPanel();
        img.setPreferredSize(new Dimension(240, 120));
        img.setBackground(new Color(170, 187, 197));
        img.add(new JLabel("Imagen"));
        card.add(img);
        card.add(Box.createVerticalStrut(6));

        JLabel name = new JLabel(titulo);
        name.setFont(H3);
        card.add(name);

        JLabel meta = new JLabel("⏱ " + tiempo + "    🧾 " + dificultad);
        meta.setFont(SMALL);
        card.add(meta);
        card.add(Box.createVerticalStrut(6));

        JPanel acciones = flowLeft();
        JButton ver = outlineButton("VER RECETA", e -> cardLayout.show(mainPanel, "recetaDetalle"));
        JButton cambiar = outlineButton("CAMBIAR", e -> cardLayout.show(mainPanel, "recetasSimilares"));
        acciones.add(ver);
        acciones.add(cambiar);
        card.add(acciones);

        return card;
    }

    private JButton outlineButton(String text, ActionListener al) {
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

    // Tarjeta de “receta similar”
    private JPanel similarCard(String titulo, String tiempo, String dificultad) {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout(10,0));

        JPanel img = new JPanel();
        img.setPreferredSize(new Dimension(140, 80));
        img.setBackground(new Color(170, 187, 197));
        img.add(new JLabel("Img"));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(titulo);
        name.setFont(H3);
        JLabel meta = new JLabel("⏱ " + tiempo + "    🧾 " + dificultad);
        meta.setFont(SMALL);
        JButton sel = outlineButton("SELECCIONAR", e -> {
            JOptionPane.showMessageDialog(this, "Seleccionada receta similar");
            cardLayout.show(mainPanel, "menuDia");
        });

        info.add(name);
        info.add(meta);
        info.add(Box.createVerticalStrut(6));
        info.add(sel);

        card.add(img, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    // Sección con título y texto (ingredientes / instrucciones)
    private JPanel section(String titulo, String cuerpo) {
        JPanel cont = new JPanel();
        cont.setOpaque(false);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        JLabel t = labelBold(titulo);
        t.setFont(H2);
        JTextArea area = new JTextArea(cuerpo);
        area.setEditable(false);
        area.setFont(BODY);
        area.setBackground(Color.WHITE);
        area.setBorder(new EmptyBorder(10,10,10,10));
        cont.add(t);
        cont.add(Box.createVerticalStrut(4));
        cont.add(new JScrollPane(area));
        return cont;
    }

    // Barra inferior MENÚ / LISTA / PERFIL
    private JPanel bottomNav() {
        JPanel nav = new JPanel(new GridLayout(1,3,12,0));
        nav.setOpaque(true);
        nav.setBackground(ACCENT);
        nav.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton menu = navItem("MENÚ", e -> cardLayout.show(mainPanel, "menuDia"));
        JButton lista = navItem("LISTA", e -> cardLayout.show(mainPanel, "listaCompra"));
        JButton perfil = navItem("PERFIL", e -> {
            cargarPerfilUsuario();
            cardLayout.show(mainPanel, "perfil");
        });

        nav.add(menu); nav.add(lista); nav.add(perfil);
        return nav;
    }

    private JButton navItem(String text, ActionListener al) {
        JButton b = new JButton(text);
        b.setFont(H3);
        b.setForeground(Color.DARK_GRAY);
        b.setBackground(ACCENT);
        b.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
        b.setFocusPainted(false);
        b.addActionListener(al);
        return b;
    }

    private void cargarPerfilUsuario() {
        if (customerId == null) return;
        Customer usuario = CustomerDAO.getClienteId(customerId);


    }

    private ImageIcon cargarIcono(String nombreImagen, int ancho, int alto){
        String ruta = '/'+nombreImagen+".png";
        java.net.URL url = getClass().getResource(ruta);
        ImageIcon icono = url != null ? new ImageIcon(url) : null;
        Image iconoEscalado = icono != null ? icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH) : null;
        return iconoEscalado != null ? new ImageIcon(iconoEscalado): null;
        //System.out.println(icono);

    }

    // ---------- Main ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JVentana().setVisible(true));
    }
}
