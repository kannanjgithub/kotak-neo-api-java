package com.kotak.neo.api;

import com.kotak.neo.api.auth.AuthManager;
import com.kotak.neo.api.websocket.NeoWebSocketClient;
import lombok.Getter;

@Getter
public class NeoAPI {
    private final NeoConfig config;
    private final NeoHTTPClient httpClient;
    private final AuthManager auth;
    private final OrderModule orders;
    private final ReportModule reports;
    private final MarketModule market;
    private NeoWebSocketClient webSocket;

    public NeoAPI(String environment, String accessToken, String consumerKey, String consumerSecret) {
        this.config = new NeoConfig(environment);
        this.config.setAccessToken(accessToken);
        this.config.setConsumerKey(consumerKey);
        this.config.setConsumerSecret(consumerSecret);
        
        this.httpClient = new NeoHTTPClient(this.config);
        this.auth = new AuthManager(this.httpClient, this.config);
        this.orders = new OrderModule(this.httpClient, this.config);
        this.reports = new ReportModule(this.httpClient, this.config);
        this.market = new MarketModule(this.httpClient, this.config);
    }

    public void initWebSocket(String url, NeoWebSocketClient.NeoWebSocketListener listener) {
        this.webSocket = new NeoWebSocketClient(url, config.getSidSession(), config.getSessionToken());
        this.webSocket.setListener(listener);
        this.webSocket.connect();
    }
}
