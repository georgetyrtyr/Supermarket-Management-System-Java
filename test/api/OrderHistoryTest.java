package api;

import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderHistoryTest {

    private static  final String TEST_FILE_NAME = "test_orderFile.bin";
    private OrderHistory orderHistory;
    private Order order1;
    private Order order2;
    private User customer1;
    private User customer2;

    @BeforeEach
    void setUp() {
        // Καθαρισμός του αρχείου τεστ πριν από κάθε τεστ
        File testFile = new File(TEST_FILE_NAME);
        if (testFile.exists() && !testFile.delete()) {
            fail("Αποτυχία διαγραφής του αρχείου " + TEST_FILE_NAME);
        }

        // Δημιουργία αντικειμένου OrderHistory με το test file
        orderHistory = new OrderHistory(TEST_FILE_NAME);

        // Αρχικοποίηση δεδομένων
        customer1 = new User("Customer1", "customer1@example.com", "customer");
        customer2 = new User("Customer2", "customer2@example.com", "customer");

        Product product1 = new Product("Product1", "Description1", "Category1", "Subcategory1", 10.0, 5, "unit");
        Product product2 = new Product("Product2", "Description2", "Category2", "Subcategory2", 20.0, 10, "unit");

        ShoppingCart cart1 = new ShoppingCart();
        cart1.addToShoppingCart(product1, 2);
        order1 = new Order(cart1, customer1);

        ShoppingCart cart2 = new ShoppingCart();
        cart2.addToShoppingCart(product2, 3);
        order2 = new Order(cart2, customer2);
    }

    @Test
    void testAddOrder() {
        assertTrue(orderHistory.addOrder(order1), "Η παραγγελία δεν αποθηκεύτηκε επιτυχώς.");
        assertTrue(orderHistory.addOrder(order2), "Η παραγγελία δεν αποθηκεύτηκε επιτυχώς.");

        List<Order> orders = orderHistory.getOrders();
        assertEquals(2, orders.size(), "Ο αριθμός των παραγγελιών δεν είναι σωστός.");
        assertTrue(orders.contains(order1), "Η λίστα δεν περιέχει την order1.");
        assertTrue(orders.contains(order2), "Η λίστα δεν περιέχει την order2.");
    }

    @Test
    void testLoadOrders() {
        // Προσθήκη παραγγελιών στο αρχείο
        orderHistory.addOrder(order1);
        orderHistory.addOrder(order2);

        // Δημιουργία νέου OrderHistory για έλεγχο της φόρτωσης
        OrderHistory newOrderHistory = new OrderHistory(TEST_FILE_NAME);
        List<Order> loadedOrders = newOrderHistory.getOrders();

        // Έλεγχος μεγέθους λίστας
        assertEquals(2, loadedOrders.size(), "Οι παραγγελίες δεν φορτώθηκαν σωστά.");

        // Έλεγχος ύπαρξης παραγγελιών με βάση τα ID
        boolean foundOrder1 = false;
        boolean foundOrder2 = false;

        for (Order order : loadedOrders) {
            if (order.getId() == order1.getId()) {
                foundOrder1 = true;
            } else if (order.getId() == order2.getId()) {
                foundOrder2 = true;
            }
        }

        assertTrue(foundOrder1, "Η παραγγελία order1 δεν βρέθηκε.");
        assertTrue(foundOrder2, "Η παραγγελία order2 δεν βρέθηκε.");
    }



    @Test
    void testGetOrdersForCustomer() {
        orderHistory.addOrder(order1);
        orderHistory.addOrder(order2);

        List<Order> customer1Orders = orderHistory.getOrdersForCustomer(customer1);
        assertEquals(1, customer1Orders.size(), "Λανθασμένος αριθμός παραγγελιών για τον customer1.");
        assertTrue(customer1Orders.contains(order1), "Η παραγγελία order1 δεν βρέθηκε για τον customer1.");

        List<Order> customer2Orders = orderHistory.getOrdersForCustomer(customer2);
        assertEquals(1, customer2Orders.size(), "Λανθασμένος αριθμός παραγγελιών για τον customer2.");
        assertTrue(customer2Orders.contains(order2), "Η παραγγελία order2 δεν βρέθηκε για τον customer2.");
    }

    @Test
    void testEmptyOrderHistory() {
        List<Order> orders = orderHistory.getOrders();
        assertTrue(orders.isEmpty(), "Η λίστα παραγγελιών δεν είναι κενή κατά την αρχικοποίηση.");
    }

    @AfterEach
    void tearDown() {
        // Καθαρισμός του αρχείου τεστ μετά από κάθε τεστ
        File testFile = new File(TEST_FILE_NAME);
        if (testFile.exists() && !testFile.delete()) {
            fail("Αποτυχία διαγραφής του αρχείου " + TEST_FILE_NAME);
        }
    }
}
