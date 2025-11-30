package icai.dtc.isw.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Customer extends Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String userGender;
    private int userAge;
    // Alérgenos / restricciones por salud
    private ArrayList<String> illegalFood;
    // Alimentos que el usuario no quiere comer por preferencia
    private ArrayList<String> alimentosNoCome;
    //recetas que el cliente ha guardado como favoritas
    private ArrayList<Receta> recetas = new ArrayList<>();
    private int presupuesto;

    public Customer() {
        super();
        this.userGender = "e";
        this.userAge = 1;
        this.illegalFood = new ArrayList<>();
        this.alimentosNoCome = new ArrayList<>();
    }

    // Constructor sin ID conocido (se genera a partir del nombre)
    public Customer(String userName, String userPass, String userGender, int userAge,
                    ArrayList<String> illegalFood, ArrayList<String> alimentosNoCome) {
        super(userName,userPass);
        this.userGender = userGender;
        this.userAge = userAge;
        this.illegalFood = (illegalFood != null) ? illegalFood : new ArrayList<>();
        this.alimentosNoCome = (alimentosNoCome != null) ? alimentosNoCome : new ArrayList<>();
    }

    // Constructor con todos los atributos
    public Customer(String userId, String userName, String userPass, String userGender, int userAge,
                    ArrayList<String> illegalFood, ArrayList<String> alimentosNoCome, ArrayList<Receta> recetasFavo, int presupuesto) {
        super(userId, userName, userPass);
        this.userGender = userGender;
        this.userAge = userAge;
        this.illegalFood = (illegalFood != null) ? illegalFood : new ArrayList<>();
        this.alimentosNoCome = (alimentosNoCome != null) ? alimentosNoCome : new ArrayList<>();
        this.recetas = recetasFavo;
        this.presupuesto = presupuesto;
    }

    //getters y setters
    public String getUserGender() {return userGender;}
    public void setUserGender(String userGender) {this.userGender = userGender;}
    public int getUserAge() {return userAge;}
    public void setUserAge(int userAge) {this.userAge = userAge;}
    public ArrayList<String> getIllegalFood() {return illegalFood;}
    public void setIllegalFood(ArrayList<String> illegalFood) {this.illegalFood = (illegalFood != null) ? illegalFood : new ArrayList<>();}
    public ArrayList<String> getAlimentosNoCome() {return alimentosNoCome;}
    public void setAlimentosNoCome(ArrayList<String> alimentosNoCome) {this.alimentosNoCome = (alimentosNoCome != null) ? alimentosNoCome : new ArrayList<>();}
    public void setPresupuesto(int presupuesto) {this.presupuesto = presupuesto;}
    public int getPresupuesto(){return this.presupuesto;}
    // Devuelve los alimentos a los cuales el usuario es alérgico en formato String
    public String illegalFoodToString() {
        if (illegalFood == null || illegalFood.isEmpty()) {
            return "";
        }
        return String.join(",", illegalFood);
    }

    // Devuelve los alimentos a los cuales el usuario es alérgico en formato String
    public String alimentosNoComeToString() {
        if (alimentosNoCome == null || alimentosNoCome.isEmpty()) {
            return "";
        }
        return String.join(",", alimentosNoCome);
    }

    // indica si el cliente no puede comerlo por alergia o preferencia
    public boolean noPuedeConsumirNombre(String nombre) {
        if (nombre == null) return false;

        String n = nombre.toLowerCase();

        if (illegalFood != null) {
            for (String s : illegalFood) {
                if (s != null && !s.isEmpty() && n.contains(s.toLowerCase())) {
                    return true;
                }
            }
        }

        if (alimentosNoCome != null) {
            for (String s : alimentosNoCome) {
                if (s != null && !s.isEmpty() && n.contains(s.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }

    // comprueba si el usuario puede consumir el ingrediente
    public boolean noPuedeConsumir(Ingrediente ingrediente) {
        return ingrediente != null && noPuedeConsumirNombre(ingrediente.getNombre());
    }

    // comprueba si el usuario puede consumir el producto
    public boolean noPuedeConsumir(Producto producto) {
        return producto != null && noPuedeConsumirNombre(producto.getNombre());
    }

    // devuelve las recetas favoritas del usuario
    public ArrayList<Receta> getRecetasFav() {return recetas;}

    // añade una nueva receta favorita al usuario
    public void anadirRecetaFav(Receta recetaNew){
        this.recetas.add(recetaNew);
    }

    // devuelve los ID de las recetas favoritas del usuario para almacenarlos en la db
    public ArrayList<String> getRecetasFavId(){
        ArrayList<String> recetasId = new ArrayList<>();
        for(Receta receta : this.recetas){
            recetasId.add(receta.getId());
        }
        return recetasId;
    }

    // elimina una receta de las favoritas del usuario
    public void eliminarRecetaFav(Receta recetaElim){
        this.recetas.remove(recetaElim);
    }
}

