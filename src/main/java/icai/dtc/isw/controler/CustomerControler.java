package icai.dtc.isw.controler;

import java.util.ArrayList;
import java.util.Map;

import icai.dtc.isw.dao.CustomerDAO;
import icai.dtc.isw.domain.Customer;

public class CustomerControler {
	CustomerDAO customerDAO=new CustomerDAO();
	public void getCustomers(ArrayList<Customer> lista) {
		customerDAO.getClientes(lista);
	}
	public Customer getCustomer(String id) {return(customerDAO.getCliente(id));}

    public Map.Entry<Customer, String> realizarRegistro(String userName, String pass, String passCheck, String sexo, int edad, ArrayList<String> seleccionAlergia, String alimentosNoCome) {
        Customer customerEnter = null;
        String flag = "a";
        if(pass.equals(passCheck) && userName != null && sexo != null){
            customerEnter = new Customer(userName, pass, sexo, edad, seleccionAlergia, alimentosNoCome);
            CustomerDAO.registerCliente(customerEnter);
            flag = "b";
        }
        else{
            customerEnter = new Customer();
        }

        return Map.entry(customerEnter, flag);
    }

    // método para cambiar las preferencias del usuario, a editar
    public void editarPreferencias(Customer usuario){

    }
}
