package com.binance.orderviewer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceQuantityPair {
    private String price;
    private String quantity;
    private Boolean isManuallyAdded = false;
    
    public PriceQuantityPair(String price, String quantity) {
        this.price = price;
        this.quantity = quantity;
        this.isManuallyAdded = false;
    }
}
