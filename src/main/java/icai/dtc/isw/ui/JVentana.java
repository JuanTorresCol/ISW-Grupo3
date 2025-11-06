package icai.dtc.isw.ui;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.domain.Customer;

import icai.dtc.isw.ui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static icai.dtc.isw.ui.UiUtils.BG;

public class JVentana extends JFrame {

    // --- Controlador / estado ---
    private final CustomerControler controler = new CustomerControler();
    private String customerId;
    private Customer usuario = new Customer();

    // --- Layout raíz ---
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Paneles post-auth (creación diferida)
    private final Map<String, Supplier<JComponent>> postAuthFactories = new HashMap<>();
    private final Map<String, JComponent> createdCards = new HashMap<>();
    private boolean postAuthPanelsCreated = false;

    // ventana principal
    public JVentana() {
        setTitle("MENUMASTER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(BG);

        configurarInterfaz();
    }

    // instanciado de paneles
    private void configurarInterfaz() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BG);

        // PRE-AUTH
        mainPanel.add(new InicioPanel(this), "inicio");
        mainPanel.add(new RegistroPanel(this, controler), "registro");
        mainPanel.add(new LoginPanel(this), "login");

        // POST-AUTH (lazy)
        postAuthFactories.put("presupuesto", () -> new PresupuestoPanel(this));
        postAuthFactories.put("menuDia", () -> new MenuDiaPanel(this));
        postAuthFactories.put("recetaDetalle", () -> new JScrollPane(new RecetaDetallePanel(this)));
        postAuthFactories.put("recetasSimilares", () -> new JScrollPane(new RecetasSimilaresPanel(this)));
        postAuthFactories.put("listaCompra", () -> new JScrollPane(new ListaCompraPanel(this)));
        postAuthFactories.put("perfil", () -> new JScrollPane(new PerfilPanel(this)));
        postAuthFactories.put("editarPerfil", () -> new EditarPanel(this, controler));

        add(mainPanel);
        showCard("inicio");
    }

    // API usada por los paneles
    public void showCard(String key) {
        cardLayout.show(mainPanel, key);
    }

    // mantiene los paneles de creación diferida
    public void ensurePostAuthPanels() {
        if (postAuthPanelsCreated) return;

        if (customerId != null) {
            usuario = cargarPerfilUsuario();
        }
        for (Map.Entry<String, Supplier<JComponent>> e : postAuthFactories.entrySet()) {
            JComponent comp = e.getValue().get();
            mainPanel.add(comp, e.getKey());
            createdCards.put(e.getKey(), comp);
        }
        postAuthPanelsCreated = true;
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // repinta el panel que se especifica
    public void refreshCard(String key) {
        Supplier<JComponent> f = postAuthFactories.get(key);
        if (f == null) return;
        JComponent old = createdCards.get(key);
        if (old != null) mainPanel.remove(old);
        JComponent comp = f.get();
        mainPanel.add(comp, key);
        createdCards.put(key, comp);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // metodo logica de cuando se aprueba el login
    public void onLoginSuccess(Customer c) {
        JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
        this.customerId = c.getUserId();
        this.usuario = cargarPerfilUsuario();
        ensurePostAuthPanels();
        showCard("presupuesto");
    }

    // lógica de cuando se aprueba el registro
    public void onRegisterSuccess(Customer c) {
        JOptionPane.showMessageDialog(this, "Registro completado");
        this.customerId = c.getUserId();
        this.usuario = cargarPerfilUsuario();
        ensurePostAuthPanels();
        showCard("presupuesto");
    }

    // lógica de cuando la edición ha sido aprobada
    public void onEditSuccess() {
        JOptionPane.showMessageDialog(this, "Edición completada");
        showCard("menuDia");
    }

    // lógica de cuando no se aprueba el login
    public void onAuthFailed(String msg) {
        JOptionPane.showMessageDialog(this, msg);
        showCard("inicio");
    }

    // lógica de cuando no se aprueba el registro
    public void onAuthFailed2(String msg) {
        JOptionPane.showMessageDialog(this, msg);
        showCard("perfil");
    }

    // carga los datos del usuario que esta actualmente conectado
    public Customer cargarPerfilUsuario() {
        if (customerId == null) return new Customer();
        return CustomerControler.getClienteId(customerId);
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String id) { this.customerId = id; }
    public Customer getUsuario() { return usuario; }
    public void setUsuario(Customer u) { this.usuario = u; }

    // ---------- GUI Main ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JVentana().setVisible(true));
    }
}