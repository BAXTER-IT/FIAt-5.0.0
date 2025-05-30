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
    
    public OrderBook getOrderBookData() {
        Map<String, Object> response = restTemplate.getForObject(BINANCE_API_URL, Map.class);
        
        if (response == null) {
            return new OrderBook();
        }
        
        Long lastUpdateId = Long.valueOf(response.get("lastUpdateId").toString());
        List<List<String>> bidsRaw = (List<List<String>>) response.get("bids");
        List<List<String>> asksRaw = (List<List<String>>) response.get("asks");
        
        List<PriceQuantityPair> bids = convertToPriceQuantityPairs(bidsRaw);
        List<PriceQuantityPair> asks = convertToPriceQuantityPairs(asksRaw);
        
        return new OrderBook(lastUpdateId, bids, asks);
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
}
