package icai.dtc.isw.controler;

import java.util.ArrayList;
import java.util.Map;

import icai.dtc.isw.dao.CustomerDAO;
import icai.dtc.isw.domain.Customer;
import icai.dtc.isw.domain.Receta;
import icai.dtc.isw.domain.Usuario;

// clase que conecta el DAO de customer con la GUI
public class CustomerControler {

    CustomerDAO customerDAO = new CustomerDAO();

    // devuelve una lista con todos los customers de la db
    public void getCustomers(ArrayList<Customer> lista) {
        customerDAO.getClientes(lista);
    }

    // devuelve un cliente que tenga el nombre pasado como parametro
    public Customer getCustomer(String name) {
        return (customerDAO.getCliente(name));
    }

    // realiza el registro de un nuevo cliente en la base de datos
    public Map.Entry<Customer, String> realizarRegistro(String userName,
                                                        String pass,
                                                        String passCheck,
                                                        String sexo,
                                                        int edad,
                                                        ArrayList<String> seleccionAlergia,
                                                        String alimentosNoCome) {

        Customer customerEnter;
        String flag = "a";

        if (pass.equals(passCheck) && userName != null && !userName.isEmpty() && sexo != null && !sexo.isEmpty()) {

            ArrayList<String> alergias = (seleccionAlergia != null) ? seleccionAlergia : new ArrayList<>();
            ArrayList<String> noComeList = parseLista(alimentosNoCome);

            customerEnter = new Customer(userName, pass, sexo, edad, alergias, noComeList);
            CustomerDAO.registerCliente(customerEnter);
            flag = "b";

        } else {
            customerEnter = new Customer();
        }

        return Map.entry(customerEnter, flag);
    }

    // devuelve un cliente que tenga el ID pasado como parametro
    public static Customer getClienteId(String inputId) {
        return (CustomerDAO.getClienteId(inputId));
    }

    // edita un cliente que exista en la base de datos
    public String editarPreferencias(Customer usuario,
                                     String userName,
                                     String pass,
                                     String passCheck,
                                     String sexo,
                                     int edad,
                                     ArrayList<String> seleccionAlergia,
                                     String alimentosNoCome) {

        String flag = "a";

        if (pass.equals(passCheck)) {

            // si viene vacío desde la GUI, mantenemos los valores anteriores
            if (userName == null || userName.isEmpty()) {
                userName = usuario.getUserName();
            }
            if (pass == null || pass.isEmpty()) {
                pass = usuario.getUserPass();
            }
            if (sexo == null || sexo.isEmpty()) {
                sexo = usuario.getUserGender();
            }
            if (edad <= 0) {
                edad = usuario.getUserAge();
            }

            ArrayList<String> alergias;
            if (seleccionAlergia == null || seleccionAlergia.isEmpty()) {
                alergias = usuario.getIllegalFood();
            } else {
                alergias = seleccionAlergia;
            }

            ArrayList<String> noComeList = parseLista(alimentosNoCome);
            if (noComeList.isEmpty()) {
                noComeList = usuario.getAlimentosNoCome();
            }

            Customer customerEdit = new Customer(
                    usuario.getUserId(),
                    userName,
                    pass,
                    sexo,
                    edad,
                    alergias,
                    noComeList,
                    usuario.getRecetasFav()
            );

            CustomerDAO.editCliente(customerEdit);
            flag = "b";
        }

        return flag;
    }

    public static void refreshFavoritos(Customer usuario){
        CustomerDAO.editCliente(usuario);
    }

    // Convierte un String tipo "cerdo, marisco, alcohol" en ArrayList<String>
    private ArrayList<String> parseLista(String texto) {
        ArrayList<String> lista = new ArrayList<>();
        if (texto == null || texto.isBlank()) {
            return lista;
        }
        String[] partes = texto.split(",");
        for (String p : partes) {
            String t = p.trim();
            if (!t.isEmpty()) {
                lista.add(t);
            }
        }
        return lista;
    }

    public static void eliminarReceta(Customer usuario, Receta receta){
        usuario.eliminarRecetaFav(receta);
        CustomerDAO.editCliente(usuario);
    }
}

