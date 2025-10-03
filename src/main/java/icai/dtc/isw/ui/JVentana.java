package icai.dtc.isw.ui;

import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.dao.CustomerDAO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class JVentana extends JFrame {
    CustomerDAO customerDAO = new CustomerDAO();

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JPasswordField confirmarContrasenaField;
    private JComboBox<String> sexoComboBox;
    private JSpinner edadSpinner;
    private JCheckBox glutenCheckBox, lactosaCheckBox, huevoCheckBox, frutosSecosCheckBox,
    pescadoCheckBox, mariscoCheckBox;
    private JTextField alimentosNoComeField;

    public JVentana() {
        setTitle("Proyecto ISW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        inicializarComponentes();
        configurarInterfaz();
    }

    private void inicializarComponentes() {
        usuarioField = new JTextField(15);
        contrasenaField = new JPasswordField(15);
        confirmarContrasenaField = new JPasswordField(15);

        // ComboBox para sexo
        String[] opcionesSexo = {"HOMBRE", "MUJER", "OTRO"};
        sexoComboBox = new JComboBox<>(opcionesSexo);

        // Spinner para edad
        SpinnerModel edadModel = new SpinnerNumberModel(18, 1, 120, 1);
        edadSpinner = new JSpinner(edadModel);

        // Checkboxes para alergias
        glutenCheckBox = new JCheckBox("GLUTEN");
        lactosaCheckBox = new JCheckBox("LACTOSA");
        huevoCheckBox = new JCheckBox("HUEVO");
        frutosSecosCheckBox = new JCheckBox("FRUTOS SECOS");
        pescadoCheckBox = new JCheckBox("PESCADO");
        mariscoCheckBox = new JCheckBox("MARISCO");
        alimentosNoComeField = new JTextField(15);
    }

    private void configurarInterfaz() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Crear los paneles
        mainPanel.add(crearPanelInicio(), "inicio");
        mainPanel.add(new JScrollPane(crearPanelRegistro()), "registro");
        mainPanel.add(crearPanelLogin(), "login");
        mainPanel.add(crearVistaUsuario(), "pantalla principal");
        mainPanel.add(crearPanelMenuSemana(), "menu semanal");

        add(mainPanel);
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("MENU", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(50, 0, 100, 0));

        JPanel botonesPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 100, 50));
        botonesPanel.setBackground(Color.WHITE);

        JButton btnIniciarSesion = new JButton("Iniciar sesion");
        JButton btnRegistrarse = new JButton("Registrarse");
        estilizarBoton(btnIniciarSesion);
        estilizarBoton(btnRegistrarse);

        //conectar con métodos
        btnIniciarSesion.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        btnRegistrarse.addActionListener(e -> cardLayout.show(mainPanel, "registro"));

        botonesPanel.add(btnIniciarSesion);
        botonesPanel.add(btnRegistrarse);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(botonesPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel titulo = new JLabel("Crea tu cuenta", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Campos de formulario
        JPanel formularioPanel = new JPanel(new GridLayout(0, 1, 5, 10));
        formularioPanel.setBackground(Color.WHITE);

        // Usuario
        formularioPanel.add(new JLabel("USUARIO"));
        formularioPanel.add(usuarioField);

        // Contraseña
        formularioPanel.add(new JLabel("CONTRASEÑA"));
        formularioPanel.add(contrasenaField);

        // Confirmar contraseña
        formularioPanel.add(new JLabel("CONFIRMAR CONTRASEÑA"));
        formularioPanel.add(confirmarContrasenaField);

        // Sexo
        formularioPanel.add(new JLabel("SEXO"));
        formularioPanel.add(sexoComboBox);

        // Edad
        formularioPanel.add(new JLabel("EDAD"));
        formularioPanel.add(edadSpinner);

        // Alergias
        formularioPanel.add(new JLabel("ALERGIAS/INTOLERANCIAS"));
        JPanel alergiasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alergiasPanel.setBackground(Color.WHITE);
        alergiasPanel.add(glutenCheckBox);
        alergiasPanel.add(lactosaCheckBox);
        alergiasPanel.add(huevoCheckBox);
        alergiasPanel.add(frutosSecosCheckBox);
        alergiasPanel.add(pescadoCheckBox);
        alergiasPanel.add(mariscoCheckBox);
        formularioPanel.add(alergiasPanel);

        // Alimentos que no comes
        formularioPanel.add(new JLabel("ALIMENTOS QUE NO COMES"));
        formularioPanel.add(alimentosNoComeField);

        // Botón de registro
        JButton btnRegistrar = new JButton("REGISTRARSE");
        estilizarBoton(btnRegistrar);
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.addActionListener(e -> {
            String userName = usuarioField.getText();
            String pass = new String(contrasenaField.getPassword());
            String passCheck = new String(confirmarContrasenaField.getPassword());
            String sexo = sexoComboBox.getSelectedItem().toString();
            int edad = (int) edadSpinner.getValue();
            ArrayList<String> seleccionAlergia = new ArrayList<>();
            String alimentosNoCome = alimentosNoComeField.getText();

            if (glutenCheckBox.isSelected()) seleccionAlergia.add("Gluten");
            if (lactosaCheckBox.isSelected()) seleccionAlergia.add("Lactosa");
            if (huevoCheckBox.isSelected()) seleccionAlergia.add("Huevo");
            if (frutosSecosCheckBox.isSelected()) seleccionAlergia.add("Frutos secos");
            if (pescadoCheckBox.isSelected()) seleccionAlergia.add("Pescado");
            if (mariscoCheckBox.isSelected()) seleccionAlergia.add("Marisco");

            if (realizarRegistro(userName, pass, passCheck, sexo, edad, seleccionAlergia, alimentosNoCome)) {
                JOptionPane.showMessageDialog(this, "Registro completado");
                cardLayout.show(mainPanel, "pantalla principal");
            } else {
                JOptionPane.showMessageDialog(this, "El registro no se pudo completar");
                cardLayout.show(mainPanel, "inicio");
            }
        });

        // Botón para volver
        JButton btnVolver = new JButton("Volver al inicio");
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.addActionListener(e -> cardLayout.show(mainPanel, "inicio"));

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(formularioPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnRegistrar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnVolver);

        return panel;
    }

    // Lógica de verificación de registro adecuado e inserción de datos en la db
    private boolean realizarRegistro(String userName, String pass, String passCheck, String sexo, int edad, ArrayList<String> seleccionAlergia, String alimentosNoCome) {
        CustomerDAO customerDAO = new CustomerDAO();
        boolean flag = false;
        if(pass.equals(passCheck) && pass != null && userName != null && sexo != null){
            Customer customerEnter = new Customer(userName, pass, sexo, edad, seleccionAlergia, alimentosNoCome);
            CustomerDAO.registerCliente(customerEnter);
            flag = true;
        }
        return flag;
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Título
        JLabel titulo = new JLabel("Inicio de sesión", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        // Campos de login
        JPanel loginPanel = new JPanel(new GridLayout(0, 1, 10, 15));
        loginPanel.setBackground(Color.WHITE);

        loginPanel.add(new JLabel("USUARIO"));
        JTextField usuarioLoginField = new JTextField(15);
        loginPanel.add(usuarioLoginField);

        loginPanel.add(new JLabel("CONTRASEÑA"));
        JPasswordField contrasenaLoginField = new JPasswordField(15);
        loginPanel.add(contrasenaLoginField);

        // Botón de entrar
        JButton btnEntrar = new JButton("Entrar");
        estilizarBoton(btnEntrar);
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.addActionListener(e -> {
            String userName = usuarioLoginField.getText();
            String pass = new String(contrasenaLoginField.getPassword());

            Customer customerCheck = customerDAO.getCliente(userName);
            if(customerCheck != null ){
                if(customerCheck.getUserPass().equals(pass)){
                    JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
                    cardLayout.show(mainPanel, "pantalla principal");
                } else{
                    JOptionPane.showMessageDialog(this, "Inicio de sesión fallido");
                }
            } else{
                JOptionPane.showMessageDialog(this, "Inicio de sesión fallido");
            }
        });

        // Botón para volver
        JButton btnVolver = new JButton("Volver al inicio");
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.addActionListener(e -> cardLayout.show(mainPanel, "inicio"));

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loginPanel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnEntrar);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnVolver);

        return panel;
    }
    private JPanel crearVistaUsuario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Encabezado
        JLabel titulo = new JLabel("MI PLANIFICACIÓN SEMANAL", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Panel principal con botones
        JPanel botonesPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        botonesPanel.setBackground(Color.WHITE);
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton btnMenuSemanal = new JButton("Ver Menú Semanal");
        JButton btnPreferencias = new JButton("Mis Preferencias");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        estilizarBoton(btnMenuSemanal);
        estilizarBoton(btnPreferencias);

        // Cerrar sesión
        btnCerrarSesion.setBackground(new Color(220, 53, 69)); // Color rojo
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Acciones de los botones
        btnMenuSemanal.addActionListener(e -> cardLayout.show(mainPanel, "menu semanal"));
        btnPreferencias.addActionListener(e -> {
            // Aquí puedes añadir la funcionalidad para ver/editar preferencias
            JOptionPane.showMessageDialog(this, "En desarrollo...");
        });
        btnCerrarSesion.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que quieres cerrar sesión?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                cardLayout.show(mainPanel, "inicio");
            }
        });

        botonesPanel.add(btnMenuSemanal);
        botonesPanel.add(btnPreferencias);
        botonesPanel.add(btnCerrarSesion);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(botonesPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelMenuSemana() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Encabezado
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("MENÚ SEMANAL", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> cardLayout.show(mainPanel, "pantalla principal"));

        headerPanel.add(btnVolver, BorderLayout.WEST);
        headerPanel.add(titulo, BorderLayout.CENTER);
        headerPanel.add(Box.createHorizontalStrut(btnVolver.getPreferredSize().width), BorderLayout.EAST);

        // Panel para los días de la semana
        JPanel diasPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        diasPanel.setBackground(Color.WHITE);
        diasPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        String[] dias = {"LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO", "DOMINGO"};

        for (String dia : dias) {
            JPanel diaPanel = crearPanelDia(dia);
            diasPanel.add(diaPanel);
        }

        // Panel de botones adicionales
        JPanel accionesPanel = new JPanel(new FlowLayout());
        accionesPanel.setBackground(Color.WHITE);

        JButton btnGenerarMenu = new JButton("Generar Nuevo Menú");
        JButton btnGuardar = new JButton("Guardar Menú");

        estilizarBoton(btnGenerarMenu);
        estilizarBoton(btnGuardar);

        btnGenerarMenu.addActionListener(e -> {
            // Aquí iría la lógica para generar un nuevo menú automáticamente
            JOptionPane.showMessageDialog(this, "En desarrollo...");
        });

        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "En desarrollo");
        });

        accionesPanel.add(btnGenerarMenu);
        accionesPanel.add(btnGuardar);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(diasPanel, BorderLayout.CENTER);
        panel.add(accionesPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelDia(String dia) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Título del día
        JLabel labelDia = new JLabel(dia, SwingConstants.CENTER);
        labelDia.setFont(new Font("Arial", Font.BOLD, 14));
        labelDia.setBackground(new Color(70, 130, 180));
        labelDia.setOpaque(true);
        labelDia.setForeground(Color.WHITE);

        // Panel para las comidas
        JPanel comidasPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        comidasPanel.setBackground(new Color(240, 240, 240));
        comidasPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] comidas = {"Desayuno", "Almuerzo", "Cena"};

        for (String comida : comidas) {
            JPanel comidaPanel = new JPanel(new BorderLayout());
            comidaPanel.setBackground(Color.WHITE);
            comidaPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

            JLabel labelComida = new JLabel(comida);
            labelComida.setFont(new Font("Arial", Font.BOLD, 12));
            labelComida.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JTextArea textAreaComida = new JTextArea(2, 10);
            textAreaComida.setLineWrap(true);
            textAreaComida.setWrapStyleWord(true);
            textAreaComida.setFont(new Font("Arial", Font.PLAIN, 11));
            textAreaComida.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            textAreaComida.setText("Ejemplo de plato...");

            comidaPanel.add(labelComida, BorderLayout.NORTH);
            comidaPanel.add(new JScrollPane(textAreaComida), BorderLayout.CENTER);

            comidasPanel.add(comidaPanel);
        }

        panel.add(labelDia, BorderLayout.NORTH);
        panel.add(comidasPanel, BorderLayout.CENTER);

        return panel;
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(new Color(70, 130, 180)); // Color azul
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JVentana().setVisible(true);
        });
    }
}