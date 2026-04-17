package com.example.oms;

public class OrderRequest {
    private final String orderId;
    private final String sku;
    private final int quantity;
    private final double amount;

    public OrderRequest(String orderId, String sku, int quantity, double amount) {
        this.orderId = orderId;
        this.sku = sku;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAmount() {
        return amount;
    }
}