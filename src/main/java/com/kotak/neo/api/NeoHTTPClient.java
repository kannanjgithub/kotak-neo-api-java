package com.kotak.neo.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotak.neo.api.exception.NeoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class NeoHTTPClient {
    private static final Logger log = LoggerFactory.getLogger(NeoHTTPClient.class);
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final NeoConfig config;

    public NeoHTTPClient(NeoConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public <T> T post(String url, Map<String, String> headers, Object body, Class<T> responseType) {
        return request("POST", url, headers, body, responseType);
    }

    public <T> T get(String url, Map<String, String> headers, Class<T> responseType) {
        return request("GET", url, headers, null, responseType);
    }

    private <T> T request(String method, String url, Map<String, String> headers, Object body, Class<T> responseType) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "NeoTradeApi-java/1.0.0");

            if (headers != null) {
                headers.forEach(builder::header);
            }

            String contentType = headers != null ? headers.getOrDefault("Content-Type", "application/json") : "application/json";

            if ("POST".equalsIgnoreCase(method)) {
                HttpRequest.BodyPublisher bodyPublisher;
                if (contentType.contains("application/x-www-form-urlencoded")) {
                    String jsonBody = objectMapper.writeValueAsString(body);
                    String formBody = "jData=" + URLEncoder.encode(jsonBody, StandardCharsets.UTF_8);
                    bodyPublisher = HttpRequest.BodyPublishers.ofString(formBody);
                } else {
                    bodyPublisher = HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body));
                }
                builder.POST(bodyPublisher);
            } else {
                builder.GET();
            }

            HttpRequest request = builder.build();
            log.debug("Sending {} request to {}", method, url);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), responseType);
            } else {
                log.error("Request failed with status {}: {}", response.statusCode(), response.body());
                throw new NeoException(response.statusCode(), "API Error", response.body());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error during API request", e);
            throw new NeoException("Error during API request: " + e.getMessage(), e);
        }
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
