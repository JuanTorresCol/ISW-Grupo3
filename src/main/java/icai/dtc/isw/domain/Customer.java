package icai.dtc.isw.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import icai.dtc.isw.utils.Util;

public class Customer implements Serializable{

    Util util = new Util();

    @Serial
    private static final long serialVersionUID = 1L;
    private String userId = util.createUserId("e");
    private String userName = "e";
    private String userPass = "e";
    private String userGender = "e";
    private int userAge = 1;
    private ArrayList<String> illegalFood = new ArrayList<>();
    private String alimentosNoCome = "e";

    // constructor del customer una vez que no se sabe su ID
    public Customer( String userName, String userPass, String userGender, int userAge, ArrayList<String> illegalFood, String alimentosNoCome) {
        this.userId = util.createUserId(userName);
        this.userName = userName;
        this.userPass = userPass;
        this.userGender = userGender;
        this.userAge = userAge;
        this.illegalFood = illegalFood;
        this.alimentosNoCome = alimentosNoCome;
    }

    // constructor predeterminado del customer
    public Customer(){
    }

    // constructor del customer en funcion a todos sus atributos
    public Customer(String userId, String userName, String userPass, String userGender, int userAge, ArrayList<String> illegalFood, String alimentosNoCome) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
        this.userGender = userGender;
        this.userAge = userAge;
        this.illegalFood = illegalFood;
        this.alimentosNoCome = alimentosNoCome;
    }

    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}
    public String getUserPass() {return userPass;}
    public void setUserPass(String userPass) {this.userPass = userPass;}
    public String getUserGender() {return userGender;}
    public void setUserGender(String userGender) {this.userGender = userGender;}
    public int getUserAge() {return userAge;}
    public void setUserAge(int userAge) {this.userAge = userAge;}
    public ArrayList<String> getIllegalFood() {return illegalFood;}
    public void setIllegalFood(ArrayList<String> illegalFood) {
        this.illegalFood = illegalFood;
    }
    public String getAlimentosNoCome() {
        return alimentosNoCome;
    }
    public String getUserName() {
        return userName;
    }

    // devuelve los alimentos a los cuales el usuario es alérgico en formato string
    public String illegalFoodToString() {
        StringBuilder nS = new StringBuilder();
        if (illegalFood != null) {

            for (String s : illegalFood) {
                nS.append(s);
            }
            return nS.toString();
        } else {
            return "e";
        }
    }
}
