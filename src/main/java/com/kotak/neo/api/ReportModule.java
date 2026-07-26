package com.kotak.neo.api;

import com.kotak.neo.api.model.OrderBookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ReportModule {
    private static final Logger log = LoggerFactory.getLogger(ReportModule.class);
    private final NeoHTTPClient httpClient;
    private final NeoConfig config;

    public ReportModule(NeoHTTPClient httpClient, NeoConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    private Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Sid", config.getSidSession());
        headers.put("Auth", config.getSessionToken());
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }

    public OrderBookResponse getOrderBook() {
        String url = config.getBaseUrl() + "/quick/user/orders";
        log.info("Fetching order book");
        return httpClient.get(url, getAuthHeaders(), OrderBookResponse.class);
    }

    public Object getTradeBook() {
        String url = config.getBaseUrl() + "/quick/user/trades";
        log.info("Fetching trade book");
        return httpClient.get(url, getAuthHeaders(), Object.class);
    }

    public Object getPositions() {
        String url = config.getBaseUrl() + "/quick/user/positions";
        log.info("Fetching positions");
        return httpClient.get(url, getAuthHeaders(), Object.class);
    }

    public Object getHoldings() {
        String url = config.getBaseUrl() + "/portfolio/v1/holdings";
        log.info("Fetching holdings");
        return httpClient.get(url, getAuthHeaders(), Object.class);
    }
}
