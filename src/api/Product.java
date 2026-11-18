package api;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String description;
    private String category;
    private String subCategory;
    private double price;
    private int quantity;
    private String unit;


    public Product(String title, String description, String category, String subCategory, double price, int quantity, String unit) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;

    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public void reduceQuantity(int quantityOrder) {
        if (quantityOrder <= quantity) { // Επιτρέπει ποσότητα ίση με το απόθεμα
            quantity -= quantityOrder;
        }

    }


    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", price=" + String.format("%.2f", price) + '€' +
                ", quantity=" + quantity + " " + unit + "]";
    }
}