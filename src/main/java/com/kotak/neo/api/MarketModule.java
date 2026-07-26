package com.kotak.neo.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MarketModule {
    private static final Logger log = LoggerFactory.getLogger(MarketModule.class);
    private final NeoHTTPClient httpClient;
    private final NeoConfig config;

    public MarketModule(NeoHTTPClient httpClient, NeoConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    private Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", config.getAccessToken());
        headers.put("Content-Type", "application/json");
        return headers;
    }

    public Object getQuotes(String symbols) {
        // symbols format: "nse_cm|22,nse_cm|23"
        String url = config.getBaseUrl() + "/script-details/1.0/quotes/neosymbol/" + symbols + "/all";
        log.info("Fetching quotes for: {}", symbols);
        return httpClient.get(url, getAuthHeaders(), Object.class);
    }

    public Object getScripMasterFilePaths() {
        String url = config.getBaseUrl() + "/script-details/1.0/masterscrip/file-paths";
        log.info("Fetching scrip master file paths");
        return httpClient.get(url, getAuthHeaders(), Object.class);
    }
}
