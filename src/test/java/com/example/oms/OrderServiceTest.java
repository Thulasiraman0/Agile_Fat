package com.example.oms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    @Test
    void orderSuccess_decreasesInventory() {
        Inventory inventory = new Inventory();
        inventory.addStock("BOOK", 5);

        PaymentGateway paymentGateway = amount -> true; // payment success
        OrderService service = new OrderService(inventory, paymentGateway);

        boolean ok = service.placeOrder(new OrderRequest("O-1", "BOOK", 2, 200.0));

        assertTrue(ok);
        assertEquals(3, inventory.getStock("BOOK"));
    }

    @Test
    void orderFails_whenInsufficientStock_inventoryNotChanged() {
        Inventory inventory = new Inventory();
        inventory.addStock("BOOK", 1);

        PaymentGateway paymentGateway = amount -> true; // payment success
        OrderService service = new OrderService(inventory, paymentGateway);

        boolean ok = service.placeOrder(new OrderRequest("O-2", "BOOK", 2, 200.0));

        assertFalse(ok);
        assertEquals(1, inventory.getStock("BOOK"));
    }

    @Test
    void orderFails_whenPaymentFails_inventoryNotChanged() {
        Inventory inventory = new Inventory();
        inventory.addStock("BOOK", 5);

        PaymentGateway paymentGateway = amount -> false; // payment failure
        OrderService service = new OrderService(inventory, paymentGateway);

        boolean ok = service.placeOrder(new OrderRequest("O-3", "BOOK", 2, 200.0));

        assertFalse(ok);
        assertEquals(5, inventory.getStock("BOOK"));
    }
}