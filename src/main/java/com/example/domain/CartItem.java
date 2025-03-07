package com.example.domain;

public class CartItem {

    private final MenuItem menuItem;
    private int quantity;

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    /**
     * 장바구니 항목(CartItem)의 수량을 1 증가시킵니다.
     */
    public void addQuantity() {
        this.quantity++;
    }

    /**
     * CartItem 의 총 가격을 계산합니다. MenuItem 의 가격에 수량을 곱하여 계산됩니다.
     *  *
     *  * @return CartItem 의 총 가격
     */
    public double getPrice() {
        return this.menuItem.price() * this.quantity;
    }

    /**
     * CartItem 과 연결된 MenuItem 을 가져옵니다.
     *
     * @return CartItem 과 연결된 MenuItem
     */
    public MenuItem getMenuItem() {
        return this.menuItem;
    }
}
