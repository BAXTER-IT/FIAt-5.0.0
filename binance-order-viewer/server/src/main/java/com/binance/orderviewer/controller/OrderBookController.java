package com.binance.orderviewer.controller;

import com.binance.orderviewer.model.OrderBook;
import com.binance.orderviewer.model.PriceQuantityPair;
import com.binance.orderviewer.service.BinanceApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderBookController {

    private static final String ORDER_BOOK_TOPIC = "/topic/orderbook";
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private BinanceApiService binanceApiService;
    
    public void sendOrderBookUpdate(OrderBook orderBook) {
        messagingTemplate.convertAndSend(ORDER_BOOK_TOPIC, orderBook);
    }
    
    /**
     * REST endpoint to get the cached order book data
     * @return The cached order book data
     */
    @GetMapping("/api/orderbook")
    public OrderBook getOrderBook() {
        return binanceApiService.getCachedOrderBookData();
    }
    
    /**
     * REST endpoint to add a new order to the order book
     * @param order The order to add
     * @return The updated order book
     */
    @PostMapping("/api/order")
    public ResponseEntity<OrderBook> addOrder(@RequestBody NewOrderRequest request) {
        PriceQuantityPair order = new PriceQuantityPair(request.getPrice(), request.getQuantity());
        order.setIsManuallyAdded(true);
        
        OrderBook updatedOrderBook = binanceApiService.addOrder(order, request.getOrderType());
        
        // Send the updated order book to all connected clients
        sendOrderBookUpdate(updatedOrderBook);
        
        return ResponseEntity.ok(updatedOrderBook);
    }
    
    /**
     * Request object for adding a new order
     */
    public static class NewOrderRequest {
        private String price;
        private String quantity;
        private String orderType; // "bid" or "ask"
        
        public String getPrice() {
            return price;
        }
        
        public void setPrice(String price) {
            this.price = price;
        }
        
        public String getQuantity() {
            return quantity;
        }
        
        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }
        
        public String getOrderType() {
            return orderType;
        }
        
        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }
    }
}
