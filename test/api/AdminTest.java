package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    @Test
    void testAdminCreation() {
        Admin admin = new Admin("admin1", "password123");
        assertEquals("admin1", admin.getUsername());
        assertEquals("password123", admin.getPassword());
        assertEquals("admin", admin.getRole());
    }

    @Test
    void testAdminToString() {
        Admin admin = new Admin("admin1", "password123");
        String expected = "Admin{username='admin1', role='admin'}";
        assertEquals(expected, admin.toString());
    }
}