package icai.dtc.isw.domain;

public class ContainerMenuCustomer {
    private final MenuSemanal menu;
    private final Customer customer;

    // Clase usada para almacenar los valores de retorno del login de usuario con receta ya creada

    public ContainerMenuCustomer(MenuSemanal menu, Customer customer) {
        this.menu=menu;
        this.customer=customer;
    }

    public MenuSemanal getMenu(){
        return this.menu;
    }

    public Customer getCustomer(){
        return this.customer;
    }
}
