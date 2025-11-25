package icai.dtc.isw.domain;

public class ContainerMenuCustomer {
    private MenuSemanal menu;
    private Customer customer;

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
