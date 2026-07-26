package com.kotak.neo.api.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class NeoWebSocketClient implements WebSocket.Listener {
    private static final Logger log = LoggerFactory.getLogger(NeoWebSocketClient.class);
    private final String url;
    private final String sid;
    private final String token;
    private final ObjectMapper objectMapper;
    private WebSocket webSocket;
    private NeoWebSocketListener listener;

    public NeoWebSocketClient(String url, String sid, String token) {
        this.url = url;
        this.sid = sid;
        this.token = token;
        this.objectMapper = new ObjectMapper();
    }

    public void setListener(NeoWebSocketListener listener) {
        this.listener = listener;
    }

    public void connect() {
        HttpClient client = HttpClient.newHttpClient();
        client.newWebSocketBuilder()
                .buildAsync(URI.create(url), this)
                .thenAccept(ws -> {
                    this.webSocket = ws;
                    authenticate();
                });
    }

    private void authenticate() {
        try {
            Map<String, Object> auth = new HashMap<>();
            auth.put("type", "cn");
            auth.put("sid", sid);
            auth.put("token", token);
            auth.put("source", "API");
            
            String json = objectMapper.writeValueAsString(auth);
            webSocket.sendText(json, true);
            log.info("Sent WebSocket authentication request");
        } catch (Exception e) {
            log.error("Error during WebSocket authentication", e);
        }
    }

    public void subscribe(String tokens) {
        try {
            Map<String, Object> sub = new HashMap<>();
            sub.put("type", "scrp");
            sub.put("tokens", tokens);
            sub.put("action", "sub");
            
            String json = objectMapper.writeValueAsString(sub);
            webSocket.sendText(json, true);
            log.info("Subscribed to tokens: {}", tokens);
        } catch (Exception e) {
            log.error("Error during WebSocket subscription", e);
        }
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        log.info("WebSocket connection opened");
        webSocket.request(1);
        if (listener != null) listener.onOpen();
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        log.debug("Received text message: {}", data);
        if (listener != null) listener.onMessage(data.toString());
        webSocket.request(1);
        return null;
    }

    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
        log.debug("Received binary message");
        // Binary parsing logic from HSWebSocketLib would go here
        if (listener != null) listener.onBinaryMessage(data);
        webSocket.request(1);
        return null;
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        log.info("WebSocket connection closed: {} - {}", statusCode, reason);
        if (listener != null) listener.onClose(statusCode, reason);
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        log.error("WebSocket error", error);
        if (listener != null) listener.onError(error);
    }

    public interface NeoWebSocketListener {
        void onOpen();
        void onMessage(String message);
        void onBinaryMessage(ByteBuffer data);
        void onClose(int statusCode, String reason);
        void onError(Throwable error);
    }
}
