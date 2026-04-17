package com.example.oms;

public class OrderService {
    private final Inventory inventory;
    private final PaymentGateway paymentGateway;

    public OrderService(Inventory inventory, PaymentGateway paymentGateway) {
        this.inventory = inventory;
        this.paymentGateway = paymentGateway;
    }

    // Returns true if order succeeded, false otherwise (simple)
    public boolean placeOrder(OrderRequest request) {
        // 1) check inventory
        if (!inventory.hasStock(request.getSku(), request.getQuantity())) {
            return false;
        }

        // 2) take payment
        boolean paid = paymentGateway.charge(request.getAmount());
        if (!paid) {
            return false;
        }

        // 3) update inventory
        inventory.decreaseStock(request.getSku(), request.getQuantity());
        return true;
    }
}