package icai.dtc.isw.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import icai.dtc.isw.utils.Util;

public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Util util = new Util();

    private String userId;
    private String userName;
    private String userPass;
    private String userGender;
    private int userAge;
    // Alérgenos / restricciones por salud
    private ArrayList<String> illegalFood;
    // Alimentos que el usuario no quiere comer por preferencia
    private ArrayList<String> alimentosNoCome;
    //recetas que el cliente ha guardado como favoritas
    private ArrayList<Receta> recetas = new ArrayList<>();

    public Customer() {
        this.userId = util.createUserId("default");
        this.userName = "e";
        this.userPass = "e";
        this.userGender = "e";
        this.userAge = 1;
        this.illegalFood = new ArrayList<>();
        this.alimentosNoCome = new ArrayList<>();
    }

    // Constructor sin ID conocido (se genera a partir del nombre)
    public Customer(String userName, String userPass, String userGender, int userAge,
                    ArrayList<String> illegalFood, ArrayList<String> alimentosNoCome) {
        this.userId = util.createUserId(userName);
        this.userName = userName;
        this.userPass = userPass;
        this.userGender = userGender;
        this.userAge = userAge;
        this.illegalFood = (illegalFood != null) ? illegalFood : new ArrayList<>();
        this.alimentosNoCome = (alimentosNoCome != null) ? alimentosNoCome : new ArrayList<>();
    }

    // Constructor con todos los atributos
    public Customer(String userId, String userName, String userPass, String userGender, int userAge,
                    ArrayList<String> illegalFood, ArrayList<String> alimentosNoCome, ArrayList<Receta> recetasFavo) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
        this.userGender = userGender;
        this.userAge = userAge;
        this.illegalFood = (illegalFood != null) ? illegalFood : new ArrayList<>();
        this.alimentosNoCome = (alimentosNoCome != null) ? alimentosNoCome : new ArrayList<>();
        this.recetas = recetasFavo;
    }

    //getters y setters
    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}
    public String getUserName() {return userName;}
    public String getUserPass() {return userPass;}
    public void setUserPass(String userPass) {this.userPass = userPass;}
    public String getUserGender() {return userGender;}
    public void setUserGender(String userGender) {this.userGender = userGender;}
    public int getUserAge() {return userAge;}
    public void setUserAge(int userAge) {this.userAge = userAge;}
    public ArrayList<String> getIllegalFood() {return illegalFood;}
    public void setIllegalFood(ArrayList<String> illegalFood) {this.illegalFood = (illegalFood != null) ? illegalFood : new ArrayList<>();}
    public ArrayList<String> getAlimentosNoCome() {return alimentosNoCome;}
    public void setAlimentosNoCome(ArrayList<String> alimentosNoCome) {this.alimentosNoCome = (alimentosNoCome != null) ? alimentosNoCome : new ArrayList<>();}

    // Devuelve los alimentos a los cuales el usuario es alérgico en formato String
    public String illegalFoodToString() {
        if (illegalFood == null || illegalFood.isEmpty()) {
            return "";
        }
        return String.join(",", illegalFood);
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

    public boolean noPuedeConsumir(Ingrediente ingrediente) {
        return ingrediente != null && noPuedeConsumirNombre(ingrediente.getNombre());
    }

    public boolean noPuedeConsumir(Producto producto) {
        return producto != null && noPuedeConsumirNombre(producto.getNombre());
    }

    public ArrayList<Receta> getRecetasFav() {return recetas;}

    public void anadirRecetaFav(Receta recetaNew){
        this.recetas.add(recetaNew);
    }

    public ArrayList<String> getRecetasFavId(){
        ArrayList<String> recetasId = new ArrayList<>();
        for(Receta receta : this.recetas){
            recetasId.add(receta.getId());
        }
        return recetasId;
    }
}

