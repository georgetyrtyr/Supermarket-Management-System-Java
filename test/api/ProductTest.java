package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void testProductCreation() {
        Product product = new Product("Apple", "Fresh red apple", "Fruits",
                "Fresh", 1.99, 100, "kg");

        assertEquals("Apple", product.getTitle());
        assertEquals("Fresh red apple", product.getDescription());
        assertEquals("Fruits", product.getCategory());
        assertEquals("Fresh", product.getSubCategory());
        assertEquals(1.99, product.getPrice());
        assertEquals(100, product.getQuantity());
        assertEquals("kg", product.getUnit());
    }

    @Test
    void testReduceQuantity() {
        Product product = new Product("Apple", "Fresh red apple", "Fruits",
                "Fresh", 1.99, 100, "kg");

        product.reduceQuantity(30);
        assertEquals(70, product.getQuantity());
    }

    @Test
    void testReduceQuantityWithExcessAmount() {
        Product product = new Product("Apple", "Fresh red apple", "Fruits",
                "Fresh", 1.99, 100, "kg");

        product.reduceQuantity(150);
        assertEquals(100, product.getQuantity());
    }

    @Test
    void testSetters() {
        Product product = new Product("Apple", "Fresh red apple", "Fruits",
                "Fresh", 1.99, 100, "kg");

        product.setTitle("Green Apple");
        product.setDescription("Fresh green apple");
        product.setCategory("Organic Fruits");
        product.setSubCategory("Premium Fresh");
        product.setPrice(2.49);
        product.setQuantity(80);
        product.setUnit("piece");

        assertEquals("Green Apple", product.getTitle());
        assertEquals("Fresh green apple", product.getDescription());
        assertEquals("Organic Fruits", product.getCategory());
        assertEquals("Premium Fresh", product.getSubCategory());
        assertEquals(2.49, product.getPrice());
        assertEquals(80, product.getQuantity());
        assertEquals("piece", product.getUnit());
    }

    @Test
    void testToString() {
        Product product = new Product("Apple", "Fresh red apple", "Fruits",
                "Fresh", 1.99, 100, "kg");

        String expected = "Product{title='Apple', description='Fresh red apple', " +
                "category='Fruits', subCategory='Fresh', price=1,99€, quantity=100 kg]";
        assertEquals(expected, product.toString());
    }
}