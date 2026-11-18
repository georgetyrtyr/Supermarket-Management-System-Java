package api;
import java.io.Serializable;

public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private ShoppingCart cart; // Καλάθι αγορών


    // Constructor με 2 παραμέτρους (μόνο username και password) για αρχικοποίηση
    public Customer(String username, String password, String role) {
        super(username, password, "customer");  // Κλήση του constructor της User
        this.firstName = "";  // Αν δεν έχουμε το όνομα και το επώνυμο, τα ορίζουμε ως κενά
        this.lastName = "";
    }

    //constructor για εγγραφή νέου πελάτη 4 παραμμετροι
    public Customer(String username,String password, String firstName, String lastName) {
        super(username, password, "customer");  // Κλήση του constructor της User
        this.firstName = firstName;
        this.lastName = lastName;

    }


    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ShoppingCart getCart() {
        return cart;
    }




    @Override
    public String toString() {
        return "Customer{" +
                "username='" + getUsername() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}









