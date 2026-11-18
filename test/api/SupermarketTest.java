package api;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.String;
import java.util.stream.Collectors;


class SupermarketTest {
    private Supermarket supermarket;
    private static final String TEST_PRODUCT_FILE = "test_products.ser";
    private static final String TEST_USER_FILE = "test_users.ser";
    private static final String TEST_ORDER_FILE = "test_order_history.bin";

    @BeforeEach
    void setUp() {
        // Clean up any existing test files
        cleanup();

        // Create new instance with test files
        supermarket = new Supermarket(TEST_USER_FILE, TEST_PRODUCT_FILE);

        // Add test users manually
        supermarket.userManager.addUser(new User("testAdmin", "testPass", "admin"));
        supermarket.userManager.addUser(new User("testUser", "testPass", "customer"));
        supermarket.userManager.saveUsers(TEST_USER_FILE);

        // Add test products
        supermarket.addProduct("Apple", "Fresh apple", "Fruits", "Fresh", 1.99, 100, "kg");
        supermarket.addProduct("Banana", "Yellow banana", "Fruits", "Fresh", 2.99, 150, "kg");


    }

    @AfterEach
    void cleanup() {
        // Clean up test files
        deleteFileIfExists(TEST_PRODUCT_FILE);
        deleteFileIfExists(TEST_USER_FILE);
        deleteFileIfExists(TEST_ORDER_FILE);
    }

    private void deleteFileIfExists(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testGetUnavailableProducts() {
        // Add products to the supermarket
        supermarket.addProduct("Orange", "Fresh Oranges", "Fruits", "Citrus", 3.0, 0, "Kg"); // Unavailable
        supermarket.addProduct("Apple", "Fresh Apples", "Fruits", "Tree Fruit", 2.5, 10, "Kg"); // Available
        supermarket.addProduct("Banana", "Fresh Bananas", "Fruits", "Tropical", 1.2, 0, "Kg"); // Unavailable

        // Get unavailable products
        List<Product> unavailableProducts = supermarket.getUnavailableProducts();

        // Verify the result
        assertEquals(2, unavailableProducts.size(), "There should be 2 unavailable products.");
        List<String> productTitles = unavailableProducts.stream()
                .map(Product::getTitle)
                .collect(Collectors.toList());
        assertTrue(productTitles.contains("Orange"), "Unavailable products should include 'Orange'.");
        assertTrue(productTitles.contains("Banana"), "Unavailable products should include 'Banana'.");
        assertFalse(productTitles.contains("Apple"), "Available products should not be in the result.");
    }

    @Test
    void testGetMostOrderedProducts() {

        // Προσθήκη προϊόντων στο κατάστημα
        supermarket.addProduct("Cheese", "Cheddar cheese", "Dairy", "Cheese", 5.0, 100, "kg");
        supermarket.addProduct("Milk", "Fresh milk", "Dairy", "Milk", 2.0, 200, "liters");
        supermarket.addProduct("Bread", "Whole grain bread", "Bakery", "Bread", 1.5, 150, "pieces");

        // Δημιουργία παραγγελιών
        ShoppingCart cart1 = new ShoppingCart();
        cart1.addToShoppingCart(supermarket.searchProductsByTitle("Cheese").get(0), 5);
        cart1.addToShoppingCart(supermarket.searchProductsByTitle("Milk").get(0), 3);
        supermarket.getOrderHistory().addOrder(new Order(cart1, new Customer("user1", "password", "customer1")));

        ShoppingCart cart2 = new ShoppingCart();
        cart2.addToShoppingCart(supermarket.searchProductsByTitle("Milk").get(0), 2);
        cart2.addToShoppingCart(supermarket.searchProductsByTitle("Bread").get(0), 7);
        supermarket.getOrderHistory().addOrder(new Order(cart2, new Customer("user2", "password", "customer2")));

        ShoppingCart cart3 = new ShoppingCart();
        cart3.addToShoppingCart(supermarket.searchProductsByTitle("Cheese").get(0), 4);
        cart3.addToShoppingCart(supermarket.searchProductsByTitle("Bread").get(0), 3);
        supermarket.getOrderHistory().addOrder(new Order(cart3, new Customer("user3", "password", "customer3")));

        // Κλήση της μεθόδου getMostOrderedProducts
        List<Map.Entry<Product, Integer>> mostOrderedProducts = supermarket.getMostOrderedProducts();

        // Επαλήθευση ότι τα προϊόντα επιστρέφονται ταξινομημένα κατά ποσότητα σε φθίνουσα σειρά
        assertEquals("Bread", mostOrderedProducts.get(0).getKey().getTitle());
        assertEquals(10, mostOrderedProducts.get(0).getValue()); // Bread έχει 10 παραγγελίες συνολικά

        assertEquals("Cheese", mostOrderedProducts.get(1).getKey().getTitle());
        assertEquals(9, mostOrderedProducts.get(1).getValue()); // Cheese έχει 9 παραγγελίες συνολικά

        assertEquals("Milk", mostOrderedProducts.get(2).getKey().getTitle());
        assertEquals(5, mostOrderedProducts.get(2).getValue()); // Milk έχει 5 παραγγελίες συνολικά
    }




    @Test
    void testAddProduct() {
        supermarket.addProduct("Orange", "Fresh orange", "Fruits", "Fresh", 3.99, 80, "kg");
        ArrayList<Product> products = supermarket.getProducts();
        boolean found = false;
        for (Product p : products) {
            if (p.getTitle().equals("Orange")) {
                found = true;
                assertEquals(3.99, p.getPrice());
                assertEquals(80, p.getQuantity());
            }
        }
        assertTrue(found);
    }

    @Test
    void testEditProduct() {
        assertTrue(supermarket.editProduct("Apple", "price", "2,99"));
        assertTrue(supermarket.editProduct("Apple", "quantity", "150"));
        assertTrue(supermarket.editProduct("Apple", "description", "Fresh red apple"));

        ArrayList<Product> products = supermarket.searchProductsByTitle("Apple");
        assertFalse(products.isEmpty());
        Product apple = products.get(0);
        assertEquals(2.99, apple.getPrice());
        assertEquals(150, apple.getQuantity());
        assertEquals("Fresh red apple", apple.getDescription());
    }

    @Test
    void testEditProductInvalidInput() {
        assertFalse(supermarket.editProduct("Apple", "price", "invalid"));
        assertFalse(supermarket.editProduct("Apple", "quantity", "invalid"));
        assertFalse(supermarket.editProduct("NonExistent", "price", "2,99"));
    }

    @Test
    void testDeleteProduct() {
        assertTrue(supermarket.deleteProduct("Apple"));
        assertTrue(supermarket.searchProductsByTitle("Apple").isEmpty());
        assertFalse(supermarket.deleteProduct("NonExistentProduct"));
    }

    @Test
    void testSearchProducts() {
        // Verify we have the expected number of products in total
        ArrayList<Product> allProducts = supermarket.getProducts();
        assertEquals(2, allProducts.size()); // Now expecting 4 products

        // Test category search
        ArrayList<Product> fruitProducts = supermarket.searchProductsByCategory("Fruits");
        assertEquals(2, fruitProducts.size()); // All 4 products are in "Fruits" category

        // Test title search
        ArrayList<Product> appleProducts = supermarket.searchProductsByTitle("Apple");
        assertEquals(1, appleProducts.size());
        assertEquals("Apple", appleProducts.get(0).getTitle());
    }

    @Test
    void testLoadAndSaveState() {
        // Add test products
        supermarket.addProduct("TestProduct", "Test Description", "Test Category",
                "Test SubCategory", 1.99, 100, "unit");

        // Save state
        supermarket.saveProducts(supermarket.getProducts(), TEST_PRODUCT_FILE);

        // Load state in new supermarket instance
        Supermarket newSupermarket = new Supermarket();
        newSupermarket.loadState(TEST_PRODUCT_FILE);

        // Verify products were loaded
        ArrayList<Product> loadedProducts = newSupermarket.getProducts();
        assertFalse(loadedProducts.isEmpty());
        boolean foundTestProduct = false;
        for (Product p : loadedProducts) {
            if (p.getTitle().equals("TestProduct")) {
                foundTestProduct = true;
                assertEquals(1.99, p.getPrice());
                assertEquals(100, p.getQuantity());
            }
        }
        assertTrue(foundTestProduct);
    }


    @Test
    void testAddProductToCart() {
        boolean addedToCart = supermarket.addProductToCart("Apple", 5, supermarket.getCart());
        assertTrue(addedToCart);
        assertEquals(1, supermarket.getCart().getShoppingCart().size());
    }

    @Test
    void testAddProductToCartInsufficientStock() {

        boolean addedToCart = supermarket.addProductToCart("Apple", 200, supermarket.getCart());
        assertFalse(addedToCart); // Δεν πρέπει να προστεθεί εάν το απόθεμα είναι ανεπαρκές
    }

    @Test
    void testUpdateProductQuantityInCart() {
        // Προσθήκη προϊόντος στο καλάθι
        boolean addedToCart = supermarket.addProductToCart("Apple", 5, supermarket.getCart());
        assertTrue(addedToCart);

        // 1. Ενημέρωση ποσότητας σε έγκυρη τιμή
        boolean quantityUpdated = supermarket.updateProductQuantityInCart("Apple", 3, supermarket.getCart());
        assertTrue(quantityUpdated);
        assertEquals(3, supermarket.getCart().getShoppingCart().get(supermarket.searchProductsByTitle("Apple").get(0)).intValue());

        // 2. Προσπάθεια ενημέρωσης ποσότητας για προϊόν που δεν υπάρχει στο καλάθι
        boolean updateFailed = supermarket.updateProductQuantityInCart("NonExistingProduct", 5, supermarket.getCart());
        assertFalse(updateFailed);

        // Ενημέρωση ποσότητας σε μηδέν
        boolean quantityRemoved = supermarket.updateProductQuantityInCart("Apple", 0, supermarket.getCart());
        assertFalse(quantityRemoved);

    }



    @Test
    void testRemoveProductFromCart() {
        // Προσθήκη προϊόντος στο καλάθι
        supermarket.addProductToCart("Apple", 5, supermarket.getCart());
        assertEquals(1, supermarket.getCart().getShoppingCart().size());

        // Αφαίρεση προϊόντος
        boolean removed = supermarket.removeProductFromCart("Apple", supermarket.getCart());
        assertTrue(removed);
        assertTrue(supermarket.getCart().isEmpty());

        // Αφαίρεση προϊόντος που δεν υπάρχει
        boolean removeNonExisting = supermarket.removeProductFromCart("NonExistingProduct", supermarket.getCart());
        assertFalse(removeNonExisting);
    }


    @Test
    public void testSubmitNewOrder() {
        // Προσθήκη προϊόντος στο supermarket
        supermarket.addProduct("Orange", "Fresh Orange", "Fruits", "Fresh", 1.5, 100, "Litre");

        // Δημιουργία καλαθιού και προσθήκη προϊόντος
        ShoppingCart cart = supermarket.getCart();
        supermarket.addProductToCart("Orange", 2, cart);

        // Δημιουργία πελάτη και ιστορικού παραγγελιών
        Customer customer = new Customer("testuser", "testpass", "testname", "testlastname");
        OrderHistory orderHistory = supermarket.getOrderHistory();

        // Υποβολή παραγγελίας
        boolean result = supermarket.submitNewOrder(cart, customer, orderHistory);
        assertTrue(result, "Order submission should return true.");
        assertTrue(cart.isEmpty(), "Cart should be cleared after order submission.");

        // Αποθήκευση παραγγελιών στο test αρχείο
        orderHistory.saveOrders("test_order_history.bin");

        // Φόρτωση παραγγελιών από το test αρχείο
        OrderHistory testOrderHistory = new OrderHistory("test_order_history.bin");

        // Επαλήθευση παραγγελίας από το test αρχείο
        List<Order> orders = testOrderHistory.getOrdersForCustomer(customer);
        assertEquals(1, orders.size(), "Order history should contain one order for the customer.");

        // Παίρνουμε την παραγγελία
        Order submittedOrder = orders.get(0);

        // Αναζήτηση του προϊόντος "Orange" στη λίστα προϊόντων
        Product orange = null;
        for (Product product : supermarket.getProducts()) {
            if (product.getTitle().equals("Orange")) {
                orange = product;
                break;
            }
        }
        assertNotNull(orange, "Product Orange should exist in the supermarket.");

        // Επαλήθευση ότι η ποσότητα μειώθηκε σωστά
        assertEquals(98, orange.getQuantity(), "Product quantity should be reduced by the ordered amount.");

        // Επαλήθευση της παραγγελίας
        for (Map.Entry<Product, Integer> entry : submittedOrder.getCart().getShoppingCart().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            if (product.getTitle().equals("Orange")) {
                assertEquals(2, quantity, "Ordered quantity for Orange should match.");
            } else {
                fail("Unexpected product in the order.");
            }
        }
    }


    @Test
    public void testViewOrderHistory() {

        supermarket.addProduct("Orange", "Fresh Oranges", "Fruits", "Citrus", 3.0, 100, "Kg");
        supermarket.addProduct("Apple", "Fresh Apples", "Fruits", "Tree Fruit", 2.5, 50, "Kg");


        ShoppingCart cart = supermarket.getCart();
        supermarket.addProductToCart("Orange", 2, cart);
        supermarket.addProductToCart("Apple", 3, cart);


        Customer customer = new Customer("User4", "pass4", "NIck", "Pap");
        OrderHistory orderHistory = supermarket.getOrderHistory();

        boolean orderSubmitted = supermarket.submitNewOrder(cart, customer, orderHistory);
        assertTrue(orderSubmitted, "Order should be submitted successfully.");

        // Προβολή ιστορικού παραγγελιών
        var orders = supermarket.viewOrderHistory(orderHistory, customer);
        assertEquals(1, orders.size(), "Order history should contain one order for the customer.");

        // Παίρνουμε την παραγγελία
        Order submittedOrder = orders.get(0);

        // επαλήθευση των προϊόντων της παραγγελίας
        for (Map.Entry<Product, Integer> entry : submittedOrder.getCart().getShoppingCart().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            if (product.getTitle().equals("Orange")) {
                assertEquals(2, quantity, "Ordered quantity for Orange should match.");
            } else if (product.getTitle().equals("Apple")) {
                assertEquals(3, quantity, "Ordered quantity for Apple should match.");
            } else {
                fail("Unexpected product in the order.");
            }
        }
    }

}