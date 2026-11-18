package  api;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Product, Integer> shoppingCart;

    public ShoppingCart() {
        this.shoppingCart = new HashMap<>();
    }

    // Μέθοδος προσθήκης προϊόντων στο καλάθι.
    public boolean addToShoppingCart(Product product, int quantity) {
        if (shoppingCart.containsKey(product)) { //αν το προιον υπάρχει ήδη στο καλάθι προσθετει στην υπαρχουσα ποσοτητα
            int currentQuantity = shoppingCart.get(product);
            shoppingCart.put(product, currentQuantity + quantity);
        } else {
            shoppingCart.put(product, quantity);
        }
        return true;
    }

    // Μέθοδος που διαγράφει προϊόν απο το καλάθι
    public boolean removeFromCart(String productName) {
        for (Product product : shoppingCart.keySet()) {
            if (product.getTitle().equalsIgnoreCase(productName)) {
                shoppingCart.remove(product); // Αφαιρεί το προϊόν από το καλάθι
                return true; // Το προϊόν αφαιρέθηκε
            }
        }
        return false; // Το προϊόν δεν βρέθηκε
    }

    // Μέθοδος που αλλάζει την ποσότητα αγοράς ενός προϊόντος.
    public boolean changeQuantity(String productName, int quantity) {
        // Έλεγχος για μηδενική ή αρνητική ποσότητα
        if (quantity <= 0) {
            System.out.println("Η ποσότητα δεν μπορεί να είναι μηδενική ή αρνητική.");
            return false;
        }

        // Αναζήτηση του προϊόντος στο καλάθι
        for (Product product : shoppingCart.keySet()) {
            if (product.getTitle().equalsIgnoreCase(productName)) {
                shoppingCart.put(product, quantity); // Ενημέρωση ποσότητας
                return true; // Επιτυχία
            }
        }

        return false; // Το προϊόν δεν βρέθηκε στο καλάθι
    }

    // Μέθοδος που υπολογίζει το συνολικό κόστος του καλαθιού
    public double totalCostOfCart() {
        double value = 0.0;
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            value += entry.getValue() * entry.getKey().getPrice();
        }
        return value;
    }

    // Μέθοδος για επιστροφή των περιεχομένων του καλαθιού
    public Map<Product, Integer> getShoppingCart() {
        return shoppingCart;
    }

    // Μέθοδος για έλεγχο αν το καλάθι είναι άδειο
    public boolean isEmpty() {
        return shoppingCart.isEmpty();
    }

    // Μέθοδος για εκκαθάριση του καλαθιού
    public void clearCart() {
        shoppingCart.clear();
    }

    @Override
    public String toString() {
        String result="Shopping Cart";
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            result += "Product: " + entry.getKey().getTitle()
                    + ", Quantity: " + entry.getValue()
                    + ", Price: " + entry.getKey().getPrice() + "\n";
        }
        return result;
    }


   }
