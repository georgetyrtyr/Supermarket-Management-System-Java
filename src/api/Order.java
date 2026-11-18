package api;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int idCounter = 0; // Στατικός μετρητής

    private int id; // Μοναδικό αναγνωριστικό για την παραγγελία
    private ShoppingCart cart;
    private double totalValue;
    private LocalDate orderDay;
    private User customer; // Αναφορά στον πελάτη που έκανε την παραγγελία

    public Order(ShoppingCart cart, User customer){
        this.id = ++idCounter;
        this.cart = new ShoppingCart();
        for (Map.Entry<Product, Integer> entry : cart.getShoppingCart().entrySet()) {
            this.cart.addToShoppingCart(entry.getKey(), entry.getValue());
        }
        totalValue= cart.totalCostOfCart();
        orderDay=LocalDate.now();
        this.customer=customer;
    }

    public int getId() {return id;}

    public ShoppingCart getCart() {
        return this.cart;
    }

    public double getTotalValue() {return totalValue;}

    public LocalDate getOrderDay() {return orderDay;}

    public User getCustomer() {
        return customer;
    }


    @Override
    public String toString() {
        return "Order id='" + id + "', Date=" + orderDay + ", Total=" + totalValue + ", Products: " + getCart() ;
    }

}
