package com.binance.orderviewer.controller;

import com.binance.orderviewer.model.OrderBook;
import com.binance.orderviewer.service.BinanceApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
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
}
