package gui;

import api.Customer;
import api.Order;
import api.Product;
import api.Supermarket;
import api.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;
public class SupermarketGUI {
    private Supermarket supermarket;
    private User loggedInUser;

    public SupermarketGUI(Supermarket supermarket) {
        this.supermarket = supermarket;
        showLoginScreen();
    }

    private void showLoginScreen() {
        JFrame frame = new JFrame("Supermarket Management System - Login");
        addWindowCloseListener(frame);
        frame.setSize(600, 400);



        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel for welcome message
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Supermarket System", JLabel.CENTER);
        welcomeLabel.setOpaque(true); // Enable background color
        welcomeLabel.setBackground(Color.ORANGE); // Highlight the label with lightGray
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Bold and larger font
        welcomeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); // Black border around the label
        welcomeLabel.setPreferredSize(new Dimension(frame.getWidth(), 50)); // Adjust height of the label

        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Center panel for login form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the form

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding between components

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15); // Smaller width
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15); // Smaller width
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);

        JCheckBox showPasswordCheckbox = new JCheckBox("Show Password");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(showPasswordCheckbox, gbc);

        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);

        // Toggle password visibility
        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('*'); // Hide password
            }
        });

        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Action for login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = supermarket.userManager.loginUser(username, password);
            if (user != null) {
                frame.dispose();
                loggedInUser = user;
                if (user.getRole().equalsIgnoreCase("admin")) {
                    showAdminPanel(user);
                } else if (user instanceof Customer) {
                    showCustomerPanel(user);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials. Please try again.");
            }
        });

        // Action for register button
        registerButton.addActionListener(e -> {
            frame.dispose();
            showRegisterScreen();
        });


        // Action for exit button
        exitButton.addActionListener(e -> {
            supermarket.saveState();
            System.exit(0);
        });
    }

    private void showRegisterScreen() {
        JFrame frame = new JFrame("Register New Customer");
        frame.setSize(350, 300);
        frame.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Περιθώρια ανάμεσα στα στοιχεία
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel firstNameLabel = new JLabel("First Name:");
        JLabel lastNameLabel = new JLabel("Last Name:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");

        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);

        // Στοιχείο για το "First Name"
        gbc.gridx = 0; // Στήλη 0
        gbc.gridy = 0; // Γραμμή 0
        formPanel.add(firstNameLabel, gbc);

        gbc.gridx = 1; // Στήλη 1
        formPanel.add(firstNameField, gbc);

        // Στοιχείο για το "Last Name"
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lastNameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);

        // Στοιχείο για το "Username"
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Στοιχείο για το "Password"
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Στοιχείο για το "Confirm Password"
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        // Κουμπιά Register και Back
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        frame.add(formPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Action for register button
        registerButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.");
                return;
            }

            supermarket.userManager.addUser(new Customer(username, password, "customer"));
            supermarket.saveState();
            JOptionPane.showMessageDialog(frame, "Registration successful!");

            frame.dispose();
            showLoginScreen();
        });

        // Action for back button
        backButton.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });
    }




    private void showAdminPanel(User admin) {
        JFrame frame = new JFrame("Admin Panel - " + admin.getUsername());
        addWindowCloseListener(frame);
        frame.setSize(800, 450);

        // Left side: Messages explaining the options
        JTextArea messagesArea = new JTextArea();
        messagesArea.setText(
                "Welcome to the Admin Menu!\n\n"
                        + "Available Options:\n\n"
                        + "- Add Product: Add a new product to the inventory.\n\n"
                        + "- Edit Product: Modify details of an existing product.\n\n"
                        + "- Delete Product: Remove a product from the inventory.\n\n"
                        + "- View All Products: Display all products in the inventory.\n\n"
                        + "- Search Products: Search for specific products.\n\n"
                        + "- View Unavailable Products: Show products with zero quantity.\n\n"
                        + "- View Most Ordered Products: Show products by order frequency.\n\n"
                        + "- Logout: Exit the admin panel and return to the login screen.\n\n"
        );
        messagesArea.setEditable(false);
        messagesArea.setLineWrap(true);
        messagesArea.setWrapStyleWord(true);
        messagesArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane messageScrollPane = new JScrollPane(messagesArea);

        // Right side: Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        JButton addProductButton = new JButton("Add Product");
        JButton editProductButton = new JButton("Edit Product");
        JButton deleteProductButton = new JButton("Delete Product");
        JButton viewProductsButton = new JButton("View All Products");
        JButton searchProductsButton = new JButton("Search Products");
        JButton unavailableProductsButton = new JButton("View Unavailable Products");
        JButton mostOrderedProductsButton = new JButton("View Most Ordered Products");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(addProductButton);
        buttonPanel.add(editProductButton);
        buttonPanel.add(deleteProductButton);
        buttonPanel.add(viewProductsButton);
        buttonPanel.add(searchProductsButton);
        buttonPanel.add(unavailableProductsButton);
        buttonPanel.add(mostOrderedProductsButton);
        buttonPanel.add(logoutButton);

        // Split pane to divide the screen
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, messageScrollPane, buttonPanel);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);

        frame.add(splitPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Button actions
        addProductButton.addActionListener(e -> addProduct(frame));
        editProductButton.addActionListener(e -> editProduct(frame));
        deleteProductButton.addActionListener(e -> deleteProduct(frame));
        viewProductsButton.addActionListener(e -> displayProducts(frame));
        searchProductsButton.addActionListener(e -> searchProducts(frame));
        unavailableProductsButton.addActionListener(e -> displayUnavailableProducts(frame));
        mostOrderedProductsButton.addActionListener(e -> displayMostOrderedProducts(frame));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });
    }

    // New method to display unavailable products
    private void displayUnavailableProducts(JFrame parentFrame) {
        ArrayList<Product> unavailableProducts = supermarket.getUnavailableProducts();

        JFrame frame = new JFrame("Unavailable Products");
        frame.setSize(600, 400);

        // Create table model with columns
        String[] columnNames = {"Title", "Description", "Category", "SubCategory", "Price", "Unit"};
        Object[][] data = new Object[unavailableProducts.size()][6];

        // Fill table data
        for (int i = 0; i < unavailableProducts.size(); i++) {
            Product p = unavailableProducts.get(i);
            data[i] = new Object[]{
                    p.getTitle(),
                    p.getDescription(),
                    p.getCategory(),
                    p.getSubCategory(),
                    String.format("%.2f", p.getPrice()),
                    p.getUnit()
            };
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add a label showing the total count
        JLabel countLabel = new JLabel("Total Unavailable Products: " + unavailableProducts.size());
        countLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        frame.setLayout(new BorderLayout());
        frame.add(countLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(parentFrame);
        frame.setVisible(true);
    }

    // New method to display most ordered products
    private void displayMostOrderedProducts(JFrame parentFrame) {
        // Get the most ordered products as a list of entries (Product, OrderCount)
        List<Map.Entry<Product, Integer>> mostOrderedProducts = supermarket.getMostOrderedProducts();

        // Create frame for displaying the table
        JFrame frame = new JFrame("Most Ordered Products");
        frame.setSize(600, 400);

        // Create table model with columns
        String[] columnNames = {"Title", "Category", "Price", "Current Stock", "Times Ordered"};
        Object[][] data = new Object[mostOrderedProducts.size()][5];

        // Fill table data
        int i = 0;
        for (Map.Entry<Product, Integer> entry : mostOrderedProducts) {
            Product p = entry.getKey();
            int orderCount = entry.getValue();
            data[i] = new Object[]{
                    p.getTitle(),
                    p.getCategory(),
                    String.format("%.2f", p.getPrice()),
                    p.getQuantity(),
                    orderCount
            };
            i++;
        }

        // Create the JTable with the data
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add a label showing the total count of products
        JLabel countLabel = new JLabel("Total Products Listed: " + mostOrderedProducts.size());
        countLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Set up the frame layout
        frame.setLayout(new BorderLayout());
        frame.add(countLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Center the frame relative to the parent
        frame.setLocationRelativeTo(parentFrame);
        frame.setVisible(true);
    }

    // Customer menu
    private void showCustomerPanel(User customer) {
        JFrame frame = new JFrame("Customer Panel - " + customer.getUsername());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(800, 600);

        // Δημιουργία στοιχείων διεπαφής
        JLabel welcomeLabel = new JLabel("Welcome, " + customer.getUsername(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton viewProductsButton = new JButton("View Products");
        JButton searchProductsButton = new JButton("Search Product");
        JButton viewCartButton = new JButton("View Cart");
        JButton viewHistoryButton = new JButton("History Order");
        JButton logoutButton = new JButton("Logout");


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(viewProductsButton);
        buttonPanel.add(searchProductsButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(viewHistoryButton);
        buttonPanel.add(logoutButton);



        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Προσθήκη λειτουργιών στα κουμπιά
        viewProductsButton.addActionListener(e -> displayProductsForCustomer(frame));
        searchProductsButton.addActionListener(e->searchProducts(frame));
        viewCartButton.addActionListener(e -> viewCart(frame));
        viewHistoryButton.addActionListener(e -> viewOrderHistory(frame));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });
    }

    private void addProduct(JFrame parentFrame) {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField subCategoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField unitField = new JTextField();

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Subcategory:"));
        panel.add(subCategoryField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Unit:"));
        panel.add(unitField);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String desc = descField.getText();
                String category = categoryField.getText();
                String subCategory = subCategoryField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                String unit = unitField.getText();

                supermarket.products.add(new Product(title, desc, category, subCategory, price, quantity, unit));
                JOptionPane.showMessageDialog(parentFrame, "Product added successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parentFrame, "Error adding product: " + e.getMessage());
            }
        }
    }

    private void editProduct(JFrame parentFrame) {
        String title = JOptionPane.showInputDialog(parentFrame, "Enter product title to edit:");
        if (title == null || title.isEmpty()) return;

        for (Product product : supermarket.products) {
            if (product.getTitle().equalsIgnoreCase(title)) {
                JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
                JTextField descField = new JTextField(product.getDescription());
                JTextField categoryField = new JTextField(product.getCategory());
                JTextField subCategoryField = new JTextField(product.getSubCategory());
                JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
                JTextField quantityField = new JTextField(String.valueOf(product.getQuantity()));
                JTextField unitField = new JTextField(product.getUnit());

                panel.add(new JLabel("Description:"));
                panel.add(descField);
                panel.add(new JLabel("Category:"));
                panel.add(categoryField);
                panel.add(new JLabel("Subcategory:"));
                panel.add(subCategoryField);
                panel.add(new JLabel("Price:"));
                panel.add(priceField);
                panel.add(new JLabel("Quantity:"));
                panel.add(quantityField);
                panel.add(new JLabel("Unit:"));
                panel.add(unitField);

                int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Edit Product", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String normalizedPrice = priceField.getText().replace('.', ',');
                        // Create a DecimalFormat with a comma as the decimal separator
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                        symbols.setDecimalSeparator(',');
                        DecimalFormat decimalFormat = new DecimalFormat();
                        decimalFormat.setDecimalFormatSymbols(symbols);

                        product.setDescription(descField.getText());
                        product.setCategory(categoryField.getText());
                        product.setSubCategory(subCategoryField.getText());
                        product.setPrice(decimalFormat.parse(normalizedPrice).doubleValue());
                        product.setQuantity(Integer.parseInt(quantityField.getText()));
                        product.setUnit(unitField.getText());

                        JOptionPane.showMessageDialog(parentFrame, "Product updated successfully!");
                    }catch (ParseException e) {
                        JOptionPane.showMessageDialog(parentFrame, "Invalid price format. Please use a comma for decimals." + e.getMessage());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(parentFrame, "Error updating product: " + e.getMessage());
                    }
                }
                return;
            }
        }

        JOptionPane.showMessageDialog(parentFrame, "Product not found!");
    }

    private void deleteProduct(JFrame parentFrame) {
        String title = JOptionPane.showInputDialog(parentFrame, "Enter product title to delete:");
        if (title == null || title.isEmpty()) return;

        boolean removed = supermarket.products.removeIf(product -> product.getTitle().equalsIgnoreCase(title));
        if (removed) {
            JOptionPane.showMessageDialog(parentFrame, "Product deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Product not found!");
        }
    }

    private void displayProducts(JFrame parentFrame) {
        JTextArea productArea = new JTextArea();
        productArea.setEditable(false);

        StringBuilder productsList = new StringBuilder();
        for (Product product : supermarket.products) {
            productsList.append(product).append("\n");
        }
        productArea.setText(productsList.toString());

        JOptionPane.showMessageDialog(parentFrame, new JScrollPane(productArea), "All Products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchProducts(JFrame parentFrame) {
        String query = JOptionPane.showInputDialog(parentFrame, "Enter title or category to search:");
        if (query == null || query.isEmpty()) return;

        ArrayList<Product> results = new ArrayList<>();
        for (Product product : supermarket.products) {
            if (product.getTitle().equalsIgnoreCase(query) || product.getCategory().equalsIgnoreCase(query)) {
                results.add(product);
            }
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "No products found!");
        } else {
            JTextArea resultArea = new JTextArea();
            resultArea.setEditable(false);

            StringBuilder resultText = new StringBuilder();
            for (Product product : results) {
                resultText.append(product).append("\n");
            }
            resultArea.setText(resultText.toString());

            JOptionPane.showMessageDialog(parentFrame, new JScrollPane(resultArea), "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void addWindowCloseListener(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(frame,
                        "Do you want to save changes before exiting?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    supermarket.saveState(); // Save the current state
                }
                System.exit(0); // Exit the application
            }
        });
    }

    private void displayProductsForCustomer(JFrame parentFrame) {
        JFrame productFrame = new JFrame("View Products - Add to Cart");
        productFrame.setSize(800, 400);
        productFrame.setLayout(new BorderLayout());

        // Δημιουργία πίνακα για τα προϊόντα (χωρίς την ποσότητα)
        String[] columnNames = {"Title", "Description", "Category", "Price","Unit"};

        // Προσαρμοσμένο TableModel που δεν επιτρέπει την επεξεργασία
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Κανένα κελί δεν είναι επεξεργάσιμο
            }
        };

        for (Product product : supermarket.products) {
            tableModel.addRow(new Object[]{
                    product.getTitle(),
                    product.getDescription(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getUnit()
            });
        }

        JTable productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Κουμπιά για προσθήκη στο καλάθι και κλείσιμο
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addToCartButton = new JButton("Add to Cart");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(addToCartButton);
        buttonPanel.add(closeButton);

        // Ενέργεια: Προσθήκη προϊόντος στο καλάθι
        addToCartButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(productFrame, "Please select a product to add.");
                return;
            }

            String productName = (String) productTable.getValueAt(selectedRow, 0);
            Product selectedProduct = null;

            for (Product product : supermarket.products) {
                if (product.getTitle().equalsIgnoreCase(productName)) {
                    selectedProduct = product;
                    break;
                }
            }

            if (selectedProduct != null) {
                String quantityInput = JOptionPane.showInputDialog(productFrame, "Enter quantity for " + productName + ":");
                try {
                    int quantity = Integer.parseInt(quantityInput);
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(productFrame, "Quantity must be positive.");
                        return;
                    }
                    if (quantity > selectedProduct.getQuantity()) {
                        JOptionPane.showMessageDialog(productFrame, "Not enough stock available.");
                        return;
                    }

                    supermarket.addProductToCart(productName, quantity, supermarket.getCart());

                    JOptionPane.showMessageDialog(productFrame, "Product added to cart!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(productFrame, "Invalid quantity format.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(productFrame, "Error: " + ex.getMessage());
                }
            }
        });


        // Ενέργεια: Κλείσιμο παραθύρου
        closeButton.addActionListener(e -> productFrame.dispose());

        // Προσθήκη στοιχείων στο παράθυρο
        productFrame.add(scrollPane, BorderLayout.CENTER);
        productFrame.add(buttonPanel, BorderLayout.SOUTH);

        productFrame.setLocationRelativeTo(parentFrame);
        productFrame.setVisible(true);
    }



    private void viewCart(JFrame parentFrame) {
        JFrame cartFrame = new JFrame("Your Cart");
        cartFrame.setSize(600, 400);
        cartFrame.setLayout(new BorderLayout());

        // Δημιουργία πίνακα δεδομένων για το καλάθι
        String[] columnNames = {"Product", "Quantity", "Price", "Total"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (var entry : supermarket.getCart().getShoppingCart().entrySet()) {
            Product product = entry.getKey();
            double quantity = entry.getValue();
            double total = product.getPrice() * quantity;

            // Χρήση String.format για περιορισμό σε 2 δεκαδικά
            tableModel.addRow(new Object[]{product.getTitle(),quantity,String.format("%.2f", product.getPrice()),
                    String.format("%.2f", total)
            });
        }

        JTable cartTable = new JTable(tableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(cartTable);

        // Πάνελ για τα κουμπιά
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton submitOrderButton = new JButton("Submit Order");

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(submitOrderButton);

        // Ενέργεια: Αλλαγή ποσότητας
        editButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(cartFrame, "Select a product to edit.");
                return;
            }

            String productName = (String) cartTable.getValueAt(selectedRow, 0);
            String newQuantityInput = JOptionPane.showInputDialog(cartFrame, "Enter new quantity:");

            try {
                int newQuantity = Integer.parseInt(newQuantityInput);
                if (newQuantity <= 0) {
                    JOptionPane.showMessageDialog(cartFrame, "Quantity must be positive.");
                    return;
                }

                if (supermarket.updateProductQuantityInCart(productName, newQuantity, supermarket.getCart())) {
                    JOptionPane.showMessageDialog(cartFrame, "Quantity updated successfully!");
                    cartFrame.dispose();
                    viewCart(parentFrame); // Ανανεώνει το καλάθι
                } else {
                    JOptionPane.showMessageDialog(cartFrame, "Failed to update quantity. Product not found in cart.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(cartFrame, "Invalid quantity format.");
            }
        });

        // Ενέργεια: Διαγραφή προϊόντος
        deleteButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(cartFrame, "Select a product to delete.");
                return;
            }

            String productName = (String) cartTable.getValueAt(selectedRow, 0);

            if (supermarket.removeProductFromCart(productName, supermarket.getCart())) {
                JOptionPane.showMessageDialog(cartFrame, "Product removed successfully!");
                cartFrame.dispose();
                viewCart(parentFrame); // Ανανεώνει το καλάθι
            } else {
                JOptionPane.showMessageDialog(cartFrame, "Failed to remove product. Cart might be empty or product not found.");
            }
        });

        // Ενέργεια: Υποβολή παραγγελίας
        submitOrderButton.addActionListener(e -> {submitOrder(cartFrame);
            cartFrame.dispose();
        });

        // Προσθήκη στοιχείων στο παράθυρο
        cartFrame.add(scrollPane, BorderLayout.CENTER);
        cartFrame.add(buttonPanel, BorderLayout.SOUTH);

        cartFrame.setLocationRelativeTo(parentFrame);
        cartFrame.setVisible(true);
    }



    private void submitOrder(JFrame parentFrame) {
        if (supermarket.getCart().getShoppingCart().isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Your cart is empty. Add products before submitting an order.");
            return;
        }

        // Έλεγχος αν ο χρήστης είναι πελάτης
        if (!(loggedInUser instanceof Customer)) {
            JOptionPane.showMessageDialog(parentFrame, "Only customers can submit orders.");
            return;
        }

        Customer customer = (Customer) loggedInUser; // Μετατροπή του χρήστη σε τύπο Customer

        boolean success = supermarket.submitNewOrder(
                supermarket.getCart(),
                customer,
                supermarket.getOrderHistory()
        );

        if (success) {
            JOptionPane.showMessageDialog(parentFrame, "Order submitted successfully!");
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Failed to submit order.");
        }
    }





    private void viewOrderHistory(JFrame parentFrame) {
        List<Order> orders = supermarket.viewOrderHistory(supermarket.getOrderHistory(), loggedInUser);

        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "No orders found for this customer.", "Order History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Δημιουργία string για την εμφάνιση του ιστορικού
        StringBuilder historyContent = new StringBuilder();
        for (Order order : orders) {
            historyContent.append("Date: ").append(order.getOrderDay()).append("\n");
            historyContent.append("Products:\n");

            // Προσθήκη προϊόντων από το καλάθι
            order.getCart().getShoppingCart().forEach((product, quantity) -> {
                historyContent.append("  - ").append(product.getTitle())
                        .append(" (Quantity: ").append(quantity).append(")\n");
            });

            historyContent.append("Total Price: ").append(String.format("%.2f", order.getTotalValue())).append(" €\n");
            historyContent.append("----------------------------------------------------\n");
        }

        // Δημιουργία JTextArea για την εμφάνιση του ιστορικού
        JTextArea historyTextArea = new JTextArea(historyContent.toString());
        historyTextArea.setEditable(false);
        historyTextArea.setLineWrap(true);
        historyTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(historyTextArea);

        // Δημιουργία παραθύρου
        JFrame historyFrame = new JFrame("Order History - " + loggedInUser.getUsername());
        historyFrame.setSize(600, 400);
        historyFrame.setLayout(new BorderLayout());

        historyFrame.add(scrollPane, BorderLayout.CENTER);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> historyFrame.dispose());
        historyFrame.add(closeButton, BorderLayout.SOUTH);

        historyFrame.setLocationRelativeTo(parentFrame);
        historyFrame.setVisible(true);
    }



}