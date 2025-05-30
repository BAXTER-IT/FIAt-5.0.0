package com.binance.orderviewer.scheduler;

import com.binance.orderviewer.controller.OrderBookController;
import com.binance.orderviewer.model.OrderBook;
import com.binance.orderviewer.service.BinanceApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BinanceDataScheduler {

    @Autowired
    private BinanceApiService binanceApiService;
    
    @Autowired
    private OrderBookController orderBookController;
    
    @Scheduled(fixedRate = 5000) // 5 seconds
    public void fetchAndBroadcastOrderBookData() {
        try {
            OrderBook orderBook = binanceApiService.getOrderBookData();
            orderBookController.sendOrderBookUpdate(orderBook);
        } catch (Exception e) {
            // Log the error but don't stop the scheduler
            System.err.println("Error fetching order book data: " + e.getMessage());
        }
    }
}
