package com.example.vo;

public record MenuItem(
        int id,
        String name,
        double price,
        String description
) {

    @Override
    public String toString() {
        return String.format("%-16s | W %.1f | %s", name(), price(), description());
    }
}
