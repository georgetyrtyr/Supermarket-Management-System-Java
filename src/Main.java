import api.*;
import gui.SupermarketGUI;


import api.Supermarket;

public class Main {

    public static void main(String[] args) {
        Supermarket supermarket = new Supermarket(); // Initialize your supermarket here
        new SupermarketGUI(supermarket);
    }
}