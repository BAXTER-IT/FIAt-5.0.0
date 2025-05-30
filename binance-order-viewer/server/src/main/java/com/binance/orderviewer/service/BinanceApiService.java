package com.binance.orderviewer.service;

import com.binance.orderviewer.model.OrderBook;
import com.binance.orderviewer.model.PriceQuantityPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BinanceApiService {

    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/depth?symbol=BTCUSDT&limit=5";
    
    @Autowired
    private RestTemplate restTemplate;
    
    // Cache for the order book data
    private OrderBook cachedOrderBook;
    
    public OrderBook getOrderBookData() {
        Map<String, Object> response = restTemplate.getForObject(BINANCE_API_URL, Map.class);
        
        if (response == null) {
            return cachedOrderBook != null ? cachedOrderBook : new OrderBook();
        }
        
        Long lastUpdateId = Long.valueOf(response.get("lastUpdateId").toString());
        List<List<String>> bidsRaw = (List<List<String>>) response.get("bids");
        List<List<String>> asksRaw = (List<List<String>>) response.get("asks");
        
        List<PriceQuantityPair> bids = convertToPriceQuantityPairs(bidsRaw);
        List<PriceQuantityPair> asks = convertToPriceQuantityPairs(asksRaw);
        
        // Create the order book
        OrderBook orderBook = new OrderBook(lastUpdateId, bids, asks);
        
        // Add dummy orders
        addDummyOrders(orderBook);
        
        // Cache the order book
        this.cachedOrderBook = orderBook;
        
        return orderBook;
    }
    
    /**
     * Adds dummy orders to the order book
     * @param orderBook The order book to add dummy orders to
     */
    private void addDummyOrders(OrderBook orderBook) {
        if (orderBook.getBids() != null && !orderBook.getBids().isEmpty()) {
            // Add a dummy bid order with quantity 12345678
            // Use the first bid price as a reference and slightly increase it
            String bidPrice = orderBook.getBids().get(0).getPrice();
            double bidPriceValue = Double.parseDouble(bidPrice);
            String dummyBidPrice = String.format("%.2f", bidPriceValue * 1.001); // Slightly higher price
            
            PriceQuantityPair dummyBid = new PriceQuantityPair(dummyBidPrice, "12345678");
            dummyBid.setIsManuallyAdded(true);
            orderBook.getBids().add(0, dummyBid);
        }
        
        if (orderBook.getAsks() != null && !orderBook.getAsks().isEmpty()) {
            // Add a dummy ask order with quantity 54655
            // Use the first ask price as a reference and slightly decrease it
            String askPrice = orderBook.getAsks().get(0).getPrice();
            double askPriceValue = Double.parseDouble(askPrice);
            String dummyAskPrice = String.format("%.2f", askPriceValue * 0.999); // Slightly lower price
            
            PriceQuantityPair dummyAsk = new PriceQuantityPair(dummyAskPrice, "54655");
            dummyAsk.setIsManuallyAdded(true);
            orderBook.getAsks().add(0, dummyAsk);
        }
    }
    
    private List<PriceQuantityPair> convertToPriceQuantityPairs(List<List<String>> rawData) {
        List<PriceQuantityPair> result = new ArrayList<>();
        
        if (rawData != null) {
            for (List<String> item : rawData) {
                if (item.size() >= 2) {
                    result.add(new PriceQuantityPair(item.get(0), item.get(1)));
                }
            }
        }
        
        return result;
    }
    
    /**
     * Gets the cached order book data
     * @return The cached order book data
     */
    public OrderBook getCachedOrderBookData() {
        return cachedOrderBook != null ? cachedOrderBook : new OrderBook();
    }
}
