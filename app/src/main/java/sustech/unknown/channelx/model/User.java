package sustech.unknown.channelx.model;

/**
 * Created by Shen on 2017/12/20.
 */

public class User {
    private String userId;
    private String userName;
    private String email;
    private String phoneNumber;


    public User(){}
    public User(String userId){this(userId,"","");}
    public User(String userId,String email){this(userId,email,"");}

    public User(String userId, String email, String phoneNumber){
        this.setUserId(userId);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}