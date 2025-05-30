package com.binance.orderviewer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBook {
    private Long lastUpdateId;
    private List<PriceQuantityPair> bids;
    private List<PriceQuantityPair> asks;
}
