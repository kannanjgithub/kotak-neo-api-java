package com.kotak.neo.api;

import com.kotak.neo.api.model.OrderRequest;
import com.kotak.neo.api.model.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class OrderModule {
    private static final Logger log = LoggerFactory.getLogger(OrderModule.class);
    private final NeoHTTPClient httpClient;
    private final NeoConfig config;

    public OrderModule(NeoHTTPClient httpClient, NeoConfig config) {
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

    public OrderResponse placeOrder(OrderRequest request) {
        String url = config.getBaseUrl() + "/quick/order/rule/ms/place";
        log.info("Placing order for symbol: {}", request.getTradingSymbol());
        return httpClient.post(url, getAuthHeaders(), request, OrderResponse.class);
    }

    public OrderResponse modifyOrder(OrderRequest request) {
        String url = config.getBaseUrl() + "/quick/order/vr/modify";
        log.info("Modifying order: {}", request.getOrderNo());
        return httpClient.post(url, getAuthHeaders(), request, OrderResponse.class);
    }

    public OrderResponse cancelOrder(String orderNo, boolean isAmo) {
        String url = config.getBaseUrl() + "/quick/order/cancel";
        log.info("Cancelling order: {}", orderNo);
        
        Map<String, String> body = new HashMap<>();
        body.put("on", orderNo);
        body.put("am", isAmo ? "YES" : "NO");
        
        return httpClient.post(url, getAuthHeaders(), body, OrderResponse.class);
    }
}
