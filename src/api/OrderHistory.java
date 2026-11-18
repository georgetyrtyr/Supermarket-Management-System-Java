package api;

import java.io.*;
import java.util.List;
import java.util.ArrayList;


class OrderHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private String FILE_NAME = "order_history.bin";
    private List<Order> orders = new ArrayList<>();

    // Constructor
    public OrderHistory() {
        loadOrders(FILE_NAME);
    }

    public OrderHistory(String testOrderFile){
        loadOrders(testOrderFile);
        this.FILE_NAME=(testOrderFile);
    }

    // Load orders from the file
    public List<Order> loadOrders(String historyFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(historyFile))) {
            orders = (List<Order>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Αν το αρχείο δεν υπάρχει, ξεκινάμε με κενή λίστα
            orders = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            // Σε περίπτωση αποτυχίας φόρτωσης, επιστρέφουμε κενή λίστα
            orders = new ArrayList<>();
        }
        return orders;
    }

    // Save orders to the file
    public boolean saveOrders(String historyFile) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(historyFile))) {
            oos.writeObject(orders);
            return true; // Επιτυχής αποθήκευση
        } catch (IOException e) {
            return false; // Αποτυχία αποθήκευσης
        }
    }

    // Add a new order and save the history
    public boolean addOrder(Order order) {
        if (order != null) {
            orders.add(order);
            return saveOrders(FILE_NAME); // Αποθήκευση και επιστροφή αποτελέσματος
        }
        return false;
    }

    // Get the list of all orders
    public List<Order> getOrders() {
        return new ArrayList<>(orders); // Επιστροφή αντίγραφου της λίστας για ασφάλεια
    }

    // Get orders for a specific customer
    public List<Order> getOrdersForCustomer(User customer) {
        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer() != null && order.getCustomer().equals(customer)) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }
}
