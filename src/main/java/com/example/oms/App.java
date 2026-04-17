package com.example.oms;

public class App {
    public static void main(String[] args) {
        // Minimal demo run (not required for tests/CI)
        Inventory inventory = new Inventory();
        inventory.addStock("PEN", 10);

        PaymentGateway paymentGateway = amount -> true; // always success

        OrderService orderService = new OrderService(inventory, paymentGateway);

        OrderRequest request = new OrderRequest("ORDER-1", "PEN", 2, 100.0);
        boolean ok = orderService.placeOrder(request);

        System.out.println("Order processed: " + ok);
        System.out.println("Remaining stock PEN: " + inventory.getStock("PEN"));
    }
}