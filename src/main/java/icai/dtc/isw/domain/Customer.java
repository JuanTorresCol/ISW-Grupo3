package icai.dtc.isw.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String userId;
    private String userPass;
    private String userGender;
    private int userAge;
    private ArrayList<String> illegalFood;

    public Customer(String userId, String userPass, String userGender, int userAge, ArrayList<String> illegalFood) {
        this.userId = userId;
        this.userPass = userPass;
        this.userGender = userGender;
        this.userAge = userAge;
        this.illegalFood = illegalFood;
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


}
