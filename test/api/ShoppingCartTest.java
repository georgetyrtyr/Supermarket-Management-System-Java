package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

class ShoppingCartTest {

    private ShoppingCart cart;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        product1 = new Product("testtitle1","testdescription1","testcategory1","testsubcategory1",1.20,20, "kila");
        product2 = new Product("testtitle2","testdescription2","testcategory2","testsubcategory2",1.20,10, "kila");
    }

    @Test
    void testAddToShoppingCart() {
        assertTrue(cart.addToShoppingCart(product1, 2));
        assertEquals(2, cart.getShoppingCart().get(product1));

        // Adding more of the same product
        assertTrue(cart.addToShoppingCart(product1, 3));
        assertEquals(5, cart.getShoppingCart().get(product1));

        // Adding a new product
        assertTrue(cart.addToShoppingCart(product2, 1));
        assertEquals(1, cart.getShoppingCart().get(product2));
    }

    @Test
    void testRemoveFromCart() {

        cart.addToShoppingCart(product1, 2);
        cart.addToShoppingCart(product2, 1);

        assertTrue(cart.removeFromCart("testtitle1")); // Το προϊόν αφαιρείται σωστά
        assertFalse(cart.getShoppingCart().containsKey(product1)); // Το product1 δεν υπάρχει πια

        assertFalse(cart.removeFromCart("mila")); // Προϊόν που δεν υπάρχει
    }


    @Test
    public void testChangeQuantity() {
        ShoppingCart cart = new ShoppingCart();

        cart.addToShoppingCart(product1, 5);

        // 1. Ενημέρωση ποσότητας σε έγκυρη τιμή
        boolean updated = cart.changeQuantity("testtitle1", 10);
        assertTrue(updated, "Η ποσότητα πρέπει να ενημερωθεί επιτυχώς.");
        assertEquals(10, cart.getShoppingCart().get(product1).intValue(), "Η ποσότητα πρέπει να είναι 10.");

        // 2. Προσπάθεια ενημέρωσης ποσότητας σε 0
        boolean invalidUpdate = cart.changeQuantity("testtitle1", 0);
        assertFalse(invalidUpdate, "Η ενημέρωση πρέπει να αποτύχει όταν η ποσότητα είναι μηδενική.");
        assertEquals(10, cart.getShoppingCart().get(product1).intValue(), "Η ποσότητα πρέπει να παραμείνει 10.");

        // 3. Ενημέρωση ποσότητας για προϊόν που δεν υπάρχει
        boolean nonExistentUpdate = cart.changeQuantity("testtitle3", 5);
        assertFalse(nonExistentUpdate, "Η ενημέρωση πρέπει να αποτύχει για προϊόν που δεν υπάρχει.");
    }

    @Test
    void testTotalCostOfCart() {


        cart.addToShoppingCart(product1, 2); // 2 *1.20= 4
        cart.addToShoppingCart(product2, 3); // 3 * 1.20 = 3.6

        assertEquals(6, cart.totalCostOfCart());
    }

    @Test
    void testIsEmpty() {
        assertTrue(cart.isEmpty());

        cart.addToShoppingCart(product1, 1);
        assertFalse(cart.isEmpty());
    }

    @Test
    void testClearCart() {
        cart.addToShoppingCart(product1, 2);
        cart.addToShoppingCart(product2, 1);

        cart.clearCart();
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getShoppingCart().size());
    }

    @Test
    void testGetShoppingCart() {


        // Προσθήκη προϊόντων στο καλάθι
        cart.addToShoppingCart(product1, 2);
        cart.addToShoppingCart(product2, 1);

        // Ανάκτηση περιεχομένου του καλαθιού
        Map<Product, Integer> cartContents = cart.getShoppingCart();

        // Έλεγχος του μεγέθους του καλαθιού
        assertEquals(2, cartContents.size(), "Το καλάθι πρέπει να περιέχει 2 προϊόντα.");

        // Έλεγχος για την ύπαρξη του κάθε προϊόντος και της ποσότητάς του
        assertTrue(cartContents.containsKey(product1), "Το καλάθι πρέπει να περιέχει το προϊόν πορτοκάλια.");
        assertTrue(cartContents.containsKey(product2), "Το καλάθι πρέπει να περιέχει το προϊόν καρότα.");

        assertEquals(2, cartContents.get(product1), "Η ποσότητα για το προϊόν πορτοκάλια πρέπει να είναι 2.");
        assertEquals(1, cartContents.get(product2), "Η ποσότητα για το προϊόν καρότα πρέπει να είναι 1.");
    }

}
