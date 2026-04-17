package com.example.oms;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<String, Integer> stock = new HashMap<>();

    public void addStock(String sku, int qty) {
        stock.put(sku, getStock(sku) + qty);
    }

    public int getStock(String sku) {
        return stock.getOrDefault(sku, 0);
    }

    public boolean hasStock(String sku, int qty) {
        return getStock(sku) >= qty;
    }

    public void decreaseStock(String sku, int qty) {
        int current = getStock(sku);
        if (current < qty) {
            throw new IllegalStateException("Not enough stock for " + sku);
        }
        stock.put(sku, current - qty);
    }
}