package icai.dtc.isw.domain;

import icai.dtc.isw.utils.Util;

public class Usuario {
    private String userId;
    private String userName;
    private String userPass;

    private static final Util util = new Util();

    public Usuario(String userName, String userPass) {
        this.userId = util.createUserId(userName);
        this.userName = userName;
        this.userPass = userPass;
    }
    public Usuario(){
        this.userId = util.createUserId("default");
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
