package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    public void testConstructorWithThreeParameters() {
        // Δημιουργία πελάτη με τον constructor που έχει μόνο username και password
        Customer customer = new Customer("user1", "pass1", "customer");

        // Έλεγχος αν τα πεδία έχουν αρχικοποιηθεί σωστά
        assertEquals("user1", customer.getUsername());
        assertEquals("pass1", customer.getPassword());
        assertEquals("customer", customer.getRole());
        assertEquals("", customer.getFirstName());
        assertEquals("", customer.getLastName());
    }

    @Test
    public void testConstructorWithFourParameters() {
        // Δημιουργία πελάτη με τον constructor που έχει username, password, firstName, lastName
        Customer customer = new Customer("user2", "pass2", "Elena", "Sapanidou");

        // Έλεγχος αν τα πεδία έχουν αρχικοποιηθεί σωστά
        assertEquals("user2", customer.getUsername());
        assertEquals("pass2", customer.getPassword());
        assertEquals("customer", customer.getRole());
        assertEquals("Elena", customer.getFirstName());
        assertEquals("Sapanidou", customer.getLastName());
    }

    @Test
    public void testSettersAndGetters() {
        // Δημιουργία πελάτη
        Customer customer = new Customer("user3", "pass3", "customer");

        // Ορισμός νέων τιμών μέσω setters
        customer.setFirstName("George");
        customer.setLastName("Tyrpsis");

        // Έλεγχος αν οι getters επιστρέφουν τις σωστές τιμές
        assertEquals("George", customer.getFirstName());
        assertEquals("Tyrpsis", customer.getLastName());
    }

    @Test
    public void testToString() {
        // Δημιουργία πελάτη και έλεγχος της μεθόδου toString
        Customer customer = new Customer("user4", "pass4", "John", "Pap");
        String expected = "Customer{username='user4', firstName='John', lastName='Pap'}";
        assertEquals(expected, customer.toString());
    }
}
