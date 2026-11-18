package api;


public class Admin extends  User {


    public Admin(String username, String password) {
        super(username, password, "admin");
    }
    @Override
    public String toString() {
        return "Admin{" +
                "username='" + getUsername() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }

}

