package com.example.service;

import com.example.domain.CartItem;
import com.example.domain.MenuItem;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Cart {

    private final Map<Integer, CartItem> carts = new HashMap<>();

    /**
     * 장바구니에 메뉴 항목을 추가합니다. 만약 해당 항목이 이미 장바구니에 존재하는 경우, 그 수량이 증가합니다.
     *
     * @param menuItem 장바구니에 추가할 MenuItem
     */
    public void addCartItem(MenuItem menuItem) {
        CartItem cartItem = carts.getOrDefault(menuItem.id(), new CartItem(menuItem, 0));
        cartItem.addQuantity();
        carts.put(menuItem.id(), cartItem);
    }

    /**
     * 장바구니에 아이템이 있는지 확인합니다.
     *
     * @return 장바구니에 하나 이상의 아이템이 있으면 true 를 반환하고, 그렇지 않으면 false 를 반환합니다.
     */
    public boolean isFilled() {
        return !carts.isEmpty();
    }

    /**
     * 현재 장바구니에 있는 모든 아이템을 가져옵니다.
     *
     * @return 장바구니에 있는 아이템을 나타내는 CartItem 객체의 변경 불가능한 리스트를 반환합니다.
     */
    public List<CartItem> getCartItems() {
        return List.copyOf(carts.values());
    }

    /**
     * 장바구니에 있는 모든 아이템의 가격을 각 아이템의 가격을 합산하여 총 가격을 계산합니다.
     *
     * @return 장바구니에 있는 모든 아이템의 총 가격을 double 타입으로 반환합니다.
     */
    public double getTotalPrice() {
        AtomicReference<Double> totalPrice = new AtomicReference<>((double) 0);
        carts.values()
                .forEach(cartItem -> totalPrice.updateAndGet(v -> (v + cartItem.getPrice())));
        return totalPrice.get();
    }

    /**
     * 장바구니에 있는 모든 아이템을 제거합니다.
     */
    public void clear() {
        carts.clear();
    }

    /**
     * 지정된 검색어를 포함하는 메뉴 아이템의 이름을 찾고 해당 메뉴 아이템의 리스트를 가져옵니다.
     * 대소문자를 구분하지 않고 일치 여부를 확인합니다.
     *
     * @param name 검색할 메뉴 아이템의 이름 또는 이름의 일부
     * @return 지정된 검색어와 일치하는 MenuItem 객체의 리스트를 반환합니다;
     *         일치하는 항목이 없으면 빈 리스트를 반환합니다.
     */
    public List<MenuItem> findMenuItemsByName(String name) {
        return carts.values().stream()
                .map(CartItem::getMenuItem)
                .filter(menuItem -> menuItem.name().toLowerCase().contains(name.toLowerCase()))
                .toList();

    }

    /**
     * 장바구니에서 지정된 메뉴 아이템을 제거합니다.
     * 만약 해당 메뉴 아이템이 장바구니에 존재하지 않을 경우, 아무 작업도 수행되지 않습니다.
     *
     * @param menuItem 장바구니에서 제거할 MenuItem
     */
    public void removeCartItem(MenuItem menuItem) {
        carts.remove(menuItem.id());
    }
}
