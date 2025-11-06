package icai.dtc.isw.controler;

import java.util.ArrayList;
import java.util.Map;

import icai.dtc.isw.dao.CustomerDAO;
import icai.dtc.isw.domain.Customer;

// clase que conecta el DAO de customer con la GUI
public class CustomerControler {
	CustomerDAO customerDAO=new CustomerDAO();

    // devuelve una lista con todos los customers de la db
	public void getCustomers(ArrayList<Customer> lista) {
		customerDAO.getClientes(lista);
	}

    // devuelve un cliente que tenga el nombre pasado como parametro
	public Customer getCustomer(String id) {return(customerDAO.getCliente(id));}

    // realiza el registro de un nuevo cliente a la base de datos
    public Map.Entry<Customer, String> realizarRegistro(String userName, String pass, String passCheck, String sexo, int edad, ArrayList<String> seleccionAlergia, String alimentosNoCome) {
        Customer customerEnter;
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

    // devuelve un cliente que tenga el ID pasado como parametro
    public static Customer getClienteId(String inputId){return(CustomerDAO.getClienteId(inputId));}

    // edita un cliente que exista en la base de datos
    public String editarPreferencias(Customer usuario, String userName, String pass, String passCheck, String sexo, int edad, ArrayList<String> seleccionAlergia, String alimentosNoCome) {
        Customer customerEdit;
        String flag = "a";
        if(pass.equals(passCheck)){
            if(userName.isEmpty()){userName = usuario.getUserName();}
            if(pass.isEmpty()){pass = usuario.getUserPass();}
            customerEdit = new Customer(usuario.getUserId(), userName, pass, sexo, edad, seleccionAlergia, alimentosNoCome);
            CustomerDAO.editCliente(customerEdit);
            flag = "b";
        }
        return flag;
    }
}
