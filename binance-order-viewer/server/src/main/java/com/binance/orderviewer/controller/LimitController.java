package com.binance.orderviewer.controller;

import com.binance.orderviewer.service.BinanceApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow requests from any origin
public class LimitController {

    @Autowired
    private BinanceApiService binanceApiService;

    @GetMapping("/limit")
    public ResponseEntity<Map<String, Integer>> getLimit() {
        Map<String, Integer> response = new HashMap<>();
        response.put("limit", binanceApiService.getLimit());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/limit")
    public ResponseEntity<Map<String, Integer>> updateLimit(@RequestBody Map<String, Integer> request) {
        Integer limit = request.get("limit");
        
        if (limit != null && limit >= 1 && limit <= 20) {
            binanceApiService.setLimit(limit);
            
            Map<String, Integer> response = new HashMap<>();
            response.put("limit", binanceApiService.getLimit());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
