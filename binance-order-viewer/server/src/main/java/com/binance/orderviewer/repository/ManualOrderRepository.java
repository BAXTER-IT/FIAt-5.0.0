package com.binance.orderviewer.repository;

import com.binance.orderviewer.model.PriceQuantityPair;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Repository for storing manually added orders
 * This is a simple in-memory implementation, but could be replaced with a database in the future
 */
@Repository
public class ManualOrderRepository {
    
    private final List<PriceQuantityPair> manualBids = new CopyOnWriteArrayList<>();
    private final List<PriceQuantityPair> manualAsks = new CopyOnWriteArrayList<>();
    
    /**
     * Add a new manually added order
     * @param order The order to add
     * @param orderType The type of order (bid or ask)
     */
    public void addOrder(PriceQuantityPair order, String orderType) {
        if ("bid".equalsIgnoreCase(orderType)) {
            manualBids.add(order);
        } else if ("ask".equalsIgnoreCase(orderType)) {
            manualAsks.add(order);
        }
    }
    
    /**
     * Get all manually added bid orders
     * @return The list of manually added bid orders
     */
    public List<PriceQuantityPair> getManualBids() {
        return new ArrayList<>(manualBids);
    }
    
    /**
     * Get all manually added ask orders
     * @return The list of manually added ask orders
     */
    public List<PriceQuantityPair> getManualAsks() {
        return new ArrayList<>(manualAsks);
    }
}
