package com.example.service;

import com.example.vo.CartItem;
import com.example.vo.MenuItem;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Cart {

    private final Map<Integer, CartItem> carts = new HashMap<>();

    public void addCartItem(MenuItem menuItem) {
        CartItem cartItem = carts.getOrDefault(menuItem.id(), new CartItem(menuItem, 0));
        cartItem.addQuantity();
        carts.put(menuItem.id(), cartItem);
    }

    public boolean isFilled() {
        return !carts.isEmpty();
    }

    public List<CartItem> getCartItems() {
        return List.copyOf(carts.values());
    }

    public double getTotalPrice() {
        AtomicReference<Double> totalPrice = new AtomicReference<>((double) 0);
        carts.values()
                .forEach(cartItem -> totalPrice.updateAndGet(v -> (v + cartItem.getPrice())));
        return totalPrice.get();
    }

    public void clear() {
        carts.clear();
    }

    public List<MenuItem> findMenuItemsByName(String name) {
        return carts.values().stream()
                .map(CartItem::getMenuItem)
                .filter(menuItem -> menuItem.name().toLowerCase().contains(name.toLowerCase()))
                .toList();

    }

    public void removeCartItem(MenuItem menuItem) {
        carts.remove(menuItem.id());
    }
}
