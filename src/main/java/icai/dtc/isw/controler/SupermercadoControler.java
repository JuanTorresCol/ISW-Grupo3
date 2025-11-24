package icai.dtc.isw.controler;

import icai.dtc.isw.dao.SupermercadoDAO;
import icai.dtc.isw.domain.Supermercado;

import java.util.ArrayList;

public class SupermercadoControler {

    public static Supermercado loginSupermercado(String user, String pass) {
//        ArrayList<Supermercado> listaSupermercados = SupermercadoDAO.getSupermercados();
//        for (Supermercado supermercado : listaSupermercados) {
//            if(supermercado.getUserName().equals(user)&&supermercado.getUserPass().equals(pass)){
//                return supermercado;
//            }
//        }
//        return null;
        return SupermercadoDAO.getSuper(user);
    }

}
