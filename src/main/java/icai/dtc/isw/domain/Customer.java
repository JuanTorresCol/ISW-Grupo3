package icai.dtc.isw.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable{
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;
    private String userId = createUserId("e");
    private String userName = "e";
    private String userPass = "e";
    private String userGender = "e";
    private int userAge = 1;
    private ArrayList<String> illegalFood = new ArrayList<>();
    private String alimentosNoCome = "e";

    public Customer( String userName, String userPass, String userGender, int userAge, ArrayList<String> illegalFood, String alimentosNoCome) {
        this.userId = createUserId(userName);
        this.userName = userName;
        this.userPass = userPass;
        this.userGender = userGender;
        this.userAge = userAge;
        this.illegalFood = illegalFood;
        this.alimentosNoCome = alimentosNoCome;
    }
    public Customer(){

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

    public static String createUserId(String username) {
        char firstLetter = Character.toUpperCase(username.charAt(0));
        int hash = 0;
        for (char c : username.toCharArray()) {
            hash += c;
        }
        int digits = Math.abs(hash % 100000);
        String formattedDigits = String.format("%05d", digits);
        return firstLetter + formattedDigits;
    }
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
