package icai.dtc.isw.domain;

import icai.dtc.isw.utils.Util;

// clase padre de Supermercado y Customer
// contiene la información que ambas clases comparten
public class Usuario {
    private String userId;
    private final String userName;
    private String userPass;


    public Usuario(String userName, String userPass) {
        this.userId = Util.createUserId(userName);
        this.userName = userName;
        this.userPass = userPass;
    }
    public Usuario(){
        this.userId = Util.createUserId("default");
        this.userName = "e";
        this.userPass = "e";
    }
    public Usuario(String userId, String userName, String userPass) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
    }

    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}
    public String getUserName() {return userName;}
    public String getUserPass() {return userPass;}
    public void setUserPass(String userPass) {this.userPass = userPass;}
}
