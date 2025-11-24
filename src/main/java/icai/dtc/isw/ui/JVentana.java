package icai.dtc.isw.ui;

import icai.dtc.isw.controler.CustomerControler;
import icai.dtc.isw.domain.*;

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
    private Customer customer;
    private Supermercado supermercado;
    private MenuSemanal menuSemanal = new MenuSemanal();
    private String bloque;
    private int dia;
    private ListaCompra lista;

    // --- Layout raíz ---
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Paneles post-auth (lazy loading)
    private final Map<String, Supplier<JComponent>> postAuthFactories = new HashMap<>();
    private final Map<String, JComponent> createdCards = new HashMap<>();

    // ventana principal
    public JVentana() {
        setTitle("MENUMASTER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 680);
        setLocationRelativeTo(null);
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
        mainPanel.add(new PresupuestoPanel(this), "lista");   // si lo necesitas antes

        // POST-AUTH (lazy)
        postAuthFactories.put("presupuesto", () -> new PresupuestoPanel(this));
        postAuthFactories.put("menuDia", () -> new MenuDiaPanel(this));
        postAuthFactories.put("recetaDetalle", () -> new JScrollPane(new RecetaDetallePanel(this)));
        postAuthFactories.put("recetasSimilares", () -> new JScrollPane(new RecetasSimilaresPanel(this)));
        postAuthFactories.put("listaCompra", () -> new ListaCompraPanel(this));
        postAuthFactories.put("perfil", () -> new PerfilPanel(this));
        postAuthFactories.put("perfilSupermercado", () -> new PerfilSupermercadoPanel(this));
        postAuthFactories.put("productosSuper", () -> new ProductosSupermercadoPanel(this));
        postAuthFactories.put("editarPerfil", () -> new EditarPanel(this, controler));
        postAuthFactories.put("recetasGuardadas", () -> new JScrollPane(new GuardadasPanel(this)));
        postAuthFactories.put("anadirNuevoProducto", () -> new NuevoProductoPanel(this));

        add(mainPanel);

        // Pantalla inicial
        showCard("inicio");
    }

    public void ensurePanel(String key) {
        if (!createdCards.containsKey(key)) {

            Supplier<JComponent> f = postAuthFactories.get(key);
            if (f != null) {
                JComponent comp = f.get();
                createdCards.put(key, comp);
                mainPanel.add(comp, key);
            }
        }
    }

    // Navegación entre pantallas
    public void showCard(String key) {
        ensurePanel(key);  // carga el panel solo cuando se necesita
        cardLayout.show(mainPanel, key);
    }

    // Repinta el panel que se especifica
    public void refreshCard(String key) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> refreshCard(key));
            return;
        }

        Supplier<JComponent> f = postAuthFactories.get(key);
        if (f == null) return;

        JComponent comp = createdCards.get(key);
        if (comp == null) {
            comp = f.get();
            createdCards.put(key, comp);
            mainPanel.add(comp, key);
        } else {
            if (comp instanceof icai.dtc.isw.ui.Refreshable) {
                ((icai.dtc.isw.ui.Refreshable) comp).refreshAsync();
            } else {
                mainPanel.remove(comp);
                comp = f.get();
                createdCards.put(key, comp);
                mainPanel.add(comp, key);
            }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void onLoginSuccess(Customer c) {
        JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
        this.customerId = c.getUserId();
        this.customer = cargarPerfilUsuario();

        ensurePanel("presupuesto");
        showCard("presupuesto");
    }

    public void onLoginSuccessSupermercado(Supermercado supermercado) {
        JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
        this.customerId = supermercado.getUserId();
        this.supermercado = supermercado;

        ensurePanel("perfilSupermercado");
        refreshCard("perfilSupermercado");
        showCard("perfilSupermercado");
    }

    public void onRegisterSuccess(Customer c) {
        JOptionPane.showMessageDialog(this, "Registro completado");
        this.customerId = c.getUserId();
        this.customer = cargarPerfilUsuario();

        ensurePanel("presupuesto");
        showCard("presupuesto");
    }

    public void onEditSuccess() {
        JOptionPane.showMessageDialog(this, "Edición completada");
        showCard("menuDia");
    }

    public void onAuthFailed(String msg) {
        JOptionPane.showMessageDialog(this, msg);
        showCard("inicio");
    }

    public void onAuthFailed2(String msg) {
        JOptionPane.showMessageDialog(this, msg);
        showCard("perfil");
    }

    public Customer cargarPerfilUsuario() {
        if (customerId == null) return new Customer();
        return CustomerControler.getClienteId(customerId);
    }

//    public String getCustomerId() { return customerId; }
//    public void setCustomerId(String id) { this.customerId = id; }

    public Customer getUsuario() { return customer; }

    public Supermercado getSupermercado(){return supermercado;}

    public void setUsuario(Customer u) { this.customer = u; }

    public MenuSemanal getMenuSemanal() {
        return menuSemanal;
    }

    public void logout() {
        Runnable r = () -> {

            this.customerId = null;
            this.customer = new Customer();
            this.menuSemanal = new MenuSemanal();

            for (JComponent comp : createdCards.values()) {
                mainPanel.remove(comp);
            }
            createdCards.clear();

            showCard("inicio");

            mainPanel.revalidate();
            mainPanel.repaint();
        };
        if (SwingUtilities.isEventDispatchThread()) r.run();
        else SwingUtilities.invokeLater(r);
    }

    public void logoutSuper() {
        Runnable r = () -> {

            this.customerId = null;
            this.supermercado = new Supermercado();

            for (JComponent comp : createdCards.values()) {
                mainPanel.remove(comp);
            }
            createdCards.clear();

            showCard("inicio");

            mainPanel.revalidate();
            mainPanel.repaint();
        };
        if (SwingUtilities.isEventDispatchThread()) r.run();
        else SwingUtilities.invokeLater(r);
    }
    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public String getBloque() { return this.bloque; }
    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getDia() { return this.dia; }
    public void setLista(ListaCompra lista){
        this.lista = lista;
    }
    public ListaCompra getLista(){return this.lista;}

    // ---------- GUI Main ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JVentana().setVisible(true));
    }
}