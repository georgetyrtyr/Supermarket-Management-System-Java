package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Map;


    public class OrderTest {

        private ShoppingCart cart;
        private User customer;
        private Product product1;
        private Product product2;


    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        customer = new User("Elsapan", "elsapan@example.com", "customer");
        product1 = new Product("testtitle1","testdescription1","testcategory1","testsubcategory1",1.20,20, "kila");
        product2 = new Product("testtitle2","testdescription2","testcategory2","testsubcategory2",1,30, "kila");

        cart.addToShoppingCart(product1, 5);
        cart.addToShoppingCart(product2, 3);
    }

    @Test
        public void testOrderCreation() {
            Order order = new Order(cart, customer);

            assertNotNull(order);
            assertEquals(cart.totalCostOfCart(), order.getTotalValue());
            assertEquals(LocalDate.now(), order.getOrderDay());
            assertEquals(customer, order.getCustomer());

            // Validate that the shopping cart in the order is a deep copy
            ShoppingCart orderCart = order.getCart();
            assertNotSame(cart, orderCart);
            assertEquals(cart.getShoppingCart(), orderCart.getShoppingCart());
        }


        @Test
    void testGetCart() {
       Order order=new Order(cart,customer);
       ShoppingCart orderCart=order.getCart();

            assertNotNull(orderCart);
            assertEquals(cart.getShoppingCart().size(), orderCart.getShoppingCart().size());
        }

        @Test
        public void testGetTotalValue() {
            Order order = new Order(cart, customer);
            assertEquals(9, order.getTotalValue());
        }


        @Test
        public void testToString() {
            Order order = new Order(cart, customer);
            String expected ="Order id='" + order.getId() + "', Date=" + order.getOrderDay() + ", Total=" + order.getTotalValue() +", Products: " + cart;
            assertEquals(expected, order.toString());
        }


}
