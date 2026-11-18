package api;

import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {
    private UserManager userManager;
    private static final String TEST_FILE = "test_users.ser";

    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }

    @AfterEach
    void cleanup() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAddUser() {
        User user = new User("testUser", "password", "customer");
        userManager.addUser(user);
        User loggedInUser = userManager.loginUser("testUser", "password");
        assertNotNull(loggedInUser);
        assertEquals("testUser", loggedInUser.getUsername());
    }

    @Test
    void testLoginUserAdmin() {
        User admin = new Admin("admin1", "password");
        userManager.addUser(admin);
        User loggedInUser = userManager.loginUser("admin1", "password");
        assertNotNull(loggedInUser);
        assertEquals("admin", loggedInUser.getRole());
    }

    @Test
    void testLoginUserCustomer() {
        User customer = new Customer("customer1", "password", "customer");
        userManager.addUser(customer);
        User loggedInUser = userManager.loginUser("customer1", "password");
        assertNotNull(loggedInUser);
        assertTrue(loggedInUser instanceof Customer);
    }

    @Test
    void testLoginUserInvalid() {
        User user = new User("user1", "password", "customer");
        userManager.addUser(user);
        User loggedInUser = userManager.loginUser("user1", "wrongpassword");
        assertNull(loggedInUser);
    }

    @Test
    void testSaveAndLoadUsers() {
        User user1 = new User("user1", "password1", "customer");
        User user2 = new User("user2", "password2", "admin");
        userManager.addUser(user1);
        userManager.addUser(user2);

        userManager.saveUsers(TEST_FILE);

        UserManager newManager = new UserManager();
        assertTrue(newManager.loadUsers(TEST_FILE));

        User loadedUser = newManager.loginUser("user1", "password1");
        assertNotNull(loadedUser);
        assertEquals("user1", loadedUser.getUsername());
    }
}