package com.example;

public record MenuItem(
        String name,
        double price,
        String description
) {

    @Override
    public String toString() {
        return String.format("%-16s | W %.1f | %s", name(), price(), description());
    }
}
