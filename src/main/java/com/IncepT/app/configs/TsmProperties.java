package com.IncepT.app.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "tsm")
public class TsmProperties {

    private Api api;
    private AuctionHouseToSell auctionHouse;
    private List<String> scannedRealms;

    @Data
    public static class Api {
        private String baseUrl;
    }

    @Data
    public static class AuctionHouseToSell {
        private int euSilvermoon;
    }
}
