package com.kotak.neo.api;

import lombok.Data;

@Data
public class NeoConfig {
    private String environment; // "uat" or "prod"
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String neoFinKey = "neotradeapi";
    
    // Tokens and SIDs obtained after login
    private String viewToken;
    private String sidView;
    private String sessionToken;
    private String sidSession;
    private String serverId;
    private String baseUrl;

    public NeoConfig(String environment) {
        this.environment = environment.toLowerCase();
    }

    public boolean isProd() {
        return "prod".equals(environment);
    }
}
