package com.impal.demo_brew4u.models;

import java.math.BigDecimal;

public class CartItemView {
    private Menu menu;
    private int quantity;

    public CartItemView(Menu menu, int quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    // Tambahkan method ini di CartItemView.java
    public BigDecimal getSubtotal() {
    // Kalikan harga menu dengan kuantitas
    return menu.getHarga().multiply(BigDecimal.valueOf(quantity));
}
}