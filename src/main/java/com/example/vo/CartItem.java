package com.example.vo;

public class CartItem {

    private final MenuItem menuItem;
    private int quantity;

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public void addQuantity() {
        this.quantity++;
    }

    public double getPrice() {
        return this.menuItem.price() * this.quantity;
    }

    public MenuItem getMenuItem() {
        return this.menuItem;
    }
}
