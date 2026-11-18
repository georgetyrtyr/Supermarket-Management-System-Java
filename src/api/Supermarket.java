package api;

import java.io.*;
import java.util.*;
import java.text.ParseException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.stream.Collectors;


public class Supermarket {
    public UserManager userManager;
    public ArrayList<Product> products;
    private ShoppingCart cart;
    public OrderHistory orderHistory;
    private  String userFile = "users.ser";
    private  String productFile = "products.ser";



    public Supermarket() {
        userManager = new UserManager();
        products = new ArrayList<>();
        cart = new ShoppingCart();
        orderHistory = new OrderHistory();
        initializeData();
    }
    public Supermarket(String testUserFile, String testProductFile) {
        userManager = new UserManager();
        products = new ArrayList<>();
        cart = new ShoppingCart();
        // orderHistory = new OrderHistory();
        // Change file paths for tests
        this.userFile = testUserFile;
        this.productFile = testProductFile;
        this.orderHistory=new OrderHistory("test_order_history.bin");
    }

    //Method for test initialization
    private void initializeTestData() {
        if (!userManager.loadUsers(userFile)) {
            userManager.addUser(new User("testAdmin", "testPass", "admin"));
            userManager.addUser(new User("testUser", "testPass", "customer"));
            userManager.saveUsers(userFile);
        }
        products = loadProducts(productFile);
    }

    public ShoppingCart getCart() {
        return cart;
    }
    public OrderHistory getOrderHistory() {
        return orderHistory;
    }

    private void initializeData() {
        // Load users and products from files
        if (!userManager.loadUsers(userFile)) {
            userManager.addUser(new User("admin1", "password1", "admin"));
            userManager.addUser(new User("admin2", "password2", "admin"));
            userManager.addUser(new User("user1", "password1", "customer"));
            userManager.addUser(new User("user2", "password2", "customer"));
            userManager.saveUsers(userFile);
        }
        products = loadProducts(productFile);
    }

    // Method to get unavailable products (products with quantity = 0)
    public ArrayList<Product> getUnavailableProducts() {
        ArrayList<Product> unavailableProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantity() == 0) {
                unavailableProducts.add(product);
            }
        }
        return unavailableProducts;
    }

    // Method to get products ordered most frequently
    public List<Map.Entry<Product, Integer>> getMostOrderedProducts() {
        Map<Product, Integer> totalQuantities = new HashMap<>();

        // Παίρνουμε όλες τις παραγγελίες από το ιστορικό
        for (Order order : orderHistory.getOrders()) {
            // Παίρνουμε το καλάθι της παραγγελίας
            Map<Product, Integer> orderItems = order.getCart().getShoppingCart();

            // Για κάθε προϊόν στο καλάθι της παραγγελίας
            for (Map.Entry<Product, Integer> entry : orderItems.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                //  συνολική ποσότητα για το προϊόν
                totalQuantities.merge(product, quantity, Integer::sum);
            }
        }

        // Ταξινομηση προιόντων κατά ποσότητα σε φθίνουσα σειρά
        return totalQuantities.entrySet().stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed()) // Ταξινόμηση κατά ποσότητα (μεγαλύτερη προς μικρότερη)
                .collect(Collectors.toList()); // Επιστροφή ως λίστα
    }



    //method for adding a product
    public void addProduct(String title, String description, String category, String subCategory, double price, int quantity, String unit) {
        products.add(new Product(title, description, category, subCategory, price, quantity, unit));
        saveState();
    }

    // method for editing a program
    public boolean editProduct(String title, String fieldToEdit, String newValue) {
        for (Product product : products) {
            if (product.getTitle().equalsIgnoreCase(title)) {
                switch (fieldToEdit.toLowerCase()) {
                    case "title":
                        product.setTitle(newValue);
                        break;
                    case "description":
                        product.setDescription(newValue);
                        break;
                    case "category":
                        product.setCategory(newValue);
                        break;
                    case "subcategory":
                        product.setSubCategory(newValue);
                        break;
                    case "price":
                        try {
                            String normalizedPrice = newValue.replace('.', ',');
                            // Create a DecimalFormat with a comma as the decimal separator
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                            symbols.setDecimalSeparator(',');
                            DecimalFormat decimalFormat = new DecimalFormat();
                            decimalFormat.setDecimalFormatSymbols(symbols);

                            // Parse the input price
                            double price = decimalFormat.parse(normalizedPrice).doubleValue();
                            product.setPrice(price);
                        } catch (ParseException ex) {
                            return false; // Return false if parsing fails
                        }

                        break;
                    case "quantity":
                        try {
                            int quantity = Integer.parseInt(newValue);
                            product.setQuantity(quantity);
                        } catch (NumberFormatException ex) {
                            return false;
                        }
                        break;
                    case "unit":
                        product.setUnit(newValue);
                        break;
                    default:
                        return false;
                }
                saveState();
                return true;
            }
        }
        return false; // Product not found
    }

    //method for deleting a product
    public boolean deleteProduct(String title) {
        boolean removed = products.removeIf(product -> product.getTitle().equalsIgnoreCase(title));
        if (removed) {
            saveState(); // Save state after modification
        }
        return removed;
    }

    //method for searching a product by title
    public ArrayList<Product> searchProductsByTitle(String title) {
        ArrayList<Product> matches = new ArrayList<>();
        for (Product product : products) {
            if (product.getTitle().equalsIgnoreCase(title)) {
                matches.add(product);
            }
        }
        return matches;
    }

    //method for searching products by category
    public ArrayList<Product> searchProductsByCategory(String category) {
        ArrayList<Product> matches = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                matches.add(product);
            }
        }
        return matches;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    // method for returning the products from the file productFile
    public ArrayList<Product> loadProducts(String productFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(productFile))) {
            return (ArrayList<Product>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    //method for saving the products to the file productFile
    public void saveProducts(ArrayList<Product> products, String productFile) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(productFile))) {
            oos.writeObject(products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //save to files
    public void saveState() {
        userManager.saveUsers(userFile);
        saveProducts(products, productFile);
    }


    public void loadState(String productFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(productFile))) {
            products = (ArrayList<Product>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + productFile);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public boolean addProductToCart(String productName, int quantity, ShoppingCart cart) {
        for (Product product : products) {
            if (product.getTitle().equalsIgnoreCase(productName)) {
                // Έλεγχος αν υπάρχει επαρκές απόθεμα
                if (quantity <= product.getQuantity()) {
                    // Προσθήκη του προϊόντος στο καλάθι
                    cart.addToShoppingCart(product, quantity);

                    return true; // Προστέθηκε επιτυχώς στο καλάθι
                }
            }
        }
        return false; // Το προϊόν δεν προστέθηκε
    }

    public boolean updateProductQuantityInCart(String productName, int newQuantity, ShoppingCart cart) {
        if (cart.changeQuantity(productName, newQuantity)) {
            return true;
        }
        return false;
    }

    public boolean removeProductFromCart(String productName, ShoppingCart cart) {
        if (cart.isEmpty()) {
            return false;
        }
        cart.removeFromCart(productName);
        return true;
    }

    public boolean submitNewOrder(ShoppingCart cart, Customer customer, OrderHistory orderHistory) {
        if (cart.isEmpty()) {
            return false; // Το καλάθι είναι άδειο
        }

        // Έλεγχος αν όλα τα προϊόντα έχουν επαρκές απόθεμα
        for (Map.Entry<Product, Integer> entry : cart.getShoppingCart().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            // Επικύρωση ποσότητας
            if (quantity > product.getQuantity() || quantity <= 0) {
                return false; // Μη επαρκές απόθεμα ή μη έγκυρη ποσότητα
            }
        }

        // Δημιουργία νέας παραγγελίας
        Order newOrder = new Order(cart, customer);

        // Προσθήκη της παραγγελίας στο ιστορικό
        orderHistory.addOrder(newOrder);

        // Αποθήκευση του ιστορικού παραγγελιών στο αρχείο
        orderHistory.saveOrders("order_history.bin");


        // Αποθήκευση ενημερωμένου αποθέματος στο αρχείο προϊόντων
        saveProducts(products, productFile);
        // Μείωση αποθέματος για όλα τα προϊόντα στο καλάθι μετά το save
        for (Map.Entry<Product, Integer> entry : cart.getShoppingCart().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            product.reduceQuantity(quantity); // Μειώνει την ποσότητα του προϊόντος
        }



        // Εκκαθάριση καλαθιού μετά την παραγγελία
        cart.clearCart();

        return true; // Η παραγγελία ολοκληρώθηκε επιτυχώς
    }


    public List<Order> viewOrderHistory(OrderHistory orderHistory, User customer) {
        List<Order> orders = orderHistory.getOrdersForCustomer(customer);

        return orders;
    }
}
