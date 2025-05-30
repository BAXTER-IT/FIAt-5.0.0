package com.binance.orderviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BinanceOrderViewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinanceOrderViewerApplication.class, args);
    }
}
