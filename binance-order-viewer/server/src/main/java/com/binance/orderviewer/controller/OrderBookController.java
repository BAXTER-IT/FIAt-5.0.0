package com.binance.orderviewer.controller;

import com.binance.orderviewer.model.OrderBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class OrderBookController {

    private static final String ORDER_BOOK_TOPIC = "/topic/orderbook";
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public void sendOrderBookUpdate(OrderBook orderBook) {
        messagingTemplate.convertAndSend(ORDER_BOOK_TOPIC, orderBook);
    }
}
