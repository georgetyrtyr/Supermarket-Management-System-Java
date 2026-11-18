package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testUserCreation() {
        User user = new User("user1", "password123", "customer");
        assertEquals("user1", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("customer", user.getRole());
    }

    @Test
    void testEquals() {
        User user1 = new User("user1", "password1", "customer");
        User user2 = new User("user1", "password2", "admin");
        User user3 = new User("user2", "password1", "customer");

        assertTrue(user1.equals(user2)); // Same username
        assertFalse(user1.equals(user3)); // Different username
        assertTrue(user1.equals(user1)); // Same object
        assertFalse(user1.equals(null)); // Null comparison
    }

    @Test
    void testHashCode() {
        User user1 = new User("user1", "password1", "customer");
        User user2 = new User("user1", "password2", "admin");

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        User user = new User("user1", "password123", "customer");
        String expected = "User{username='user1', role='customer'}";
        assertEquals(expected, user.toString());
    }

    @Test
    void testSetters() {
        User user = new User("user1", "password123", "customer");

        user.setUsername("newUsername");
        user.setPassword("newPassword");
        user.setRole("admin");

        assertEquals("newUsername", user.getUsername());
        assertEquals("newPassword", user.getPassword());
        assertEquals("admin", user.getRole());
    }
}
