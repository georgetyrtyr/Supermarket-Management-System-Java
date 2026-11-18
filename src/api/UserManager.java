package api;

import java.io.*;
import java.util.ArrayList;
import java.io.Serializable;
public class UserManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<User> users;

    public UserManager() {
        users = new ArrayList<>();
    }

    public boolean loadUsers(String userFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
            users = (ArrayList<User>) ois.readObject();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveUsers(String userFile) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userFile))) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addUser(User user) {

        users.add(user);
    }


    // method to handle username and password as parameters
    public User loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                if (user.getRole().equalsIgnoreCase("customer")) {
                    // Επιστροφή ως Customer
                    return new Customer(user.getUsername(), user.getPassword(), "customer");
                }
                return user; // Επιστροφή ως User (π.χ. admin)
            }
        }
        return null; // Αν δεν βρεθεί ο χρήστης
    }



}

