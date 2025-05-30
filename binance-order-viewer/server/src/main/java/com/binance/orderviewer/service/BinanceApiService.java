package com.binance.orderviewer.service;

import com.binance.orderviewer.model.OrderBook;
import com.binance.orderviewer.model.PriceQuantityPair;
import com.binance.orderviewer.repository.ManualOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class BinanceApiService {

    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/depth?symbol=BTCUSDT&limit=5";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ManualOrderRepository manualOrderRepository;
    
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
        
        // Sort Binance API bids in descending order (highest price first)
        if (bids != null && !bids.isEmpty()) {
            bids.sort(Comparator.comparing(
                pair -> Double.parseDouble(pair.getPrice()), 
                Comparator.reverseOrder()
            ));
        }
        
        // Sort Binance API asks in ascending order (lowest price first)
        if (asks != null && !asks.isEmpty()) {
            asks.sort(Comparator.comparing(
                pair -> Double.parseDouble(pair.getPrice())
            ));
        }
        
        // Create the order book
        OrderBook orderBook = new OrderBook(lastUpdateId, bids, asks);
        
        // Add manually added orders from the repository
        addManualOrders(orderBook);
        
        // Cache the order book
        this.cachedOrderBook = orderBook;
        
        return orderBook;
    }
    
    /**
     * Adds manually added orders from the repository to the order book
     * @param orderBook The order book to add manually added orders to
     */
    private void addManualOrders(OrderBook orderBook) {
        // Add manually added bids
        List<PriceQuantityPair> manualBids = manualOrderRepository.getManualBids();
        if (orderBook.getBids() == null) {
            orderBook.setBids(new ArrayList<>());
        }
        orderBook.getBids().addAll(manualBids);
        
        // Sort bids by price in descending order (highest price first)
        if (!orderBook.getBids().isEmpty()) {
            orderBook.getBids().sort(Comparator.comparing(
                pair -> Double.parseDouble(pair.getPrice()), 
                Comparator.reverseOrder()
            ));
        }
        
        // Add manually added asks
        List<PriceQuantityPair> manualAsks = manualOrderRepository.getManualAsks();
        if (orderBook.getAsks() == null) {
            orderBook.setAsks(new ArrayList<>());
        }
        orderBook.getAsks().addAll(manualAsks);
        
        // Sort asks by price in ascending order (lowest price first)
        if (!orderBook.getAsks().isEmpty()) {
            orderBook.getAsks().sort(Comparator.comparing(
                pair -> Double.parseDouble(pair.getPrice())
            ));
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
    
    /**
     * Adds a new order to the order book and stores it in the repository
     * @param order The order to add
     * @param orderType The type of order (bid or ask)
     * @return The updated order book
     */
    public OrderBook addOrder(PriceQuantityPair order, String orderType) {
        if (cachedOrderBook == null) {
            // Initialize the order book if it doesn't exist
            cachedOrderBook = new OrderBook();
            cachedOrderBook.setBids(new ArrayList<>());
            cachedOrderBook.setAsks(new ArrayList<>());
        }
        
        // Store the order in the repository
        manualOrderRepository.addOrder(order, orderType);
        
        if ("bid".equalsIgnoreCase(orderType)) {
            // Add to bids
            if (cachedOrderBook.getBids() == null) {
                cachedOrderBook.setBids(new ArrayList<>());
            }
            cachedOrderBook.getBids().add(order);
            
            // Sort bids by price in descending order (highest price first)
            cachedOrderBook.getBids().sort(Comparator.comparing(
                pair -> Double.parseDouble(pair.getPrice()), 
                Comparator.reverseOrder()
            ));
        } else if ("ask".equalsIgnoreCase(orderType)) {
            // Add to asks
            if (cachedOrderBook.getAsks() == null) {
                cachedOrderBook.setAsks(new ArrayList<>());
            }
            cachedOrderBook.getAsks().add(order);
            
            // Sort asks by price in ascending order (lowest price first)
            cachedOrderBook.getAsks().sort(Comparator.comparing(
                pair -> Double.parseDouble(pair.getPrice())
            ));
        }
        
        return cachedOrderBook;
    }
}
