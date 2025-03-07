package com.example;

public enum Category {
    BURGER("Burgers"),
    DRINK("Drinks"),
    DESSERT("Desserts"),
    ;

    private final String title;

    Category(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
