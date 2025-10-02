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
        setSize(400, 600);
        setLocationRelativeTo(null);

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
        // He puesto un scroll panel porque el layout actual no cabe en la ventana
        mainPanel.add(new JScrollPane(crearPanelRegistro()), "registro");
        mainPanel.add(crearPanelLogin(), "login");

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

            String userId = usuarioField.getText();
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
            if (realizarRegistro(userId, pass, passCheck, sexo, edad, seleccionAlergia, alimentosNoCome)) {
                JOptionPane.showMessageDialog(this, "Registro completado");
            }   else{
                JOptionPane.showMessageDialog(this, "El registro no se pudo completar");
            }
            cardLayout.show(mainPanel, "inicio");
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
    private boolean realizarRegistro(String userId, String pass, String passCheck, String sexo, int edad, ArrayList<String> seleccionAlergia, String alimentosNoCome) {
        CustomerDAO customerDAO = new CustomerDAO();
        boolean flag = false;
        if(pass.equals(passCheck) && pass != null && userId != null && sexo != null){
            Customer customerEnter = new Customer(userId, pass, sexo, edad, seleccionAlergia, alimentosNoCome);
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
            cardLayout.show(mainPanel, "menuPrincipal"); // Para ir al menú principal
            String userId = usuarioLoginField.getText();
            String pass = new String(contrasenaLoginField.getPassword());

            Customer customerCheck = customerDAO.getCliente(userId);
            if(customerCheck != null){
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
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