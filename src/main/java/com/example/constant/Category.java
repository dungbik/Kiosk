package com.example.constant;

public enum Category {
    BURGER(1,"Burgers", false),
    DRINK(2, "Drinks", false),
    DESSERT(3, "Desserts", false),
    ORDER(4, "Orders", true),
    CANCEL_ORDER(5, "Cancels", true),
    REMOVE_CART_ITEM(6, "Remove Cart Item", true)
    ;

    private final int id;
    private final String title;
    private final boolean cartFilled;

    Category(int id, String title, boolean cartFilled) {
        this.id = id;
        this.title = title;
        this.cartFilled = cartFilled;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCartFilled() {
        return cartFilled;
    }
}
