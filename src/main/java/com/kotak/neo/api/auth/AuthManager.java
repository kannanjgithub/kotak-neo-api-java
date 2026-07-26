package com.kotak.neo.api.auth;

import com.kotak.neo.api.NeoConfig;
import com.kotak.neo.api.NeoHTTPClient;
import com.kotak.neo.api.model.TotpLoginRequest;
import com.kotak.neo.api.model.TotpLoginResponse;
import com.kotak.neo.api.model.TotpValidateRequest;
import com.kotak.neo.api.model.TotpValidateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {
    private static final Logger log = LoggerFactory.getLogger(AuthManager.class);
    private final NeoHTTPClient httpClient;
    private final NeoConfig config;

    private static final String LOGIN_URL = "https://mis.kotaksecurities.com/login/1.0/tradeApiLogin";
    private static final String VALIDATE_URL = "https://mis.kotaksecurities.com/login/1.0/tradeApiValidate";

    public AuthManager(NeoHTTPClient httpClient, NeoConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    public TotpLoginResponse totpLogin(String mobileNumber, String ucc, String totp) {
        log.info("Attempting TOTP login for UCC: {}", ucc);
        
        TotpLoginRequest request = new TotpLoginRequest();
        request.setMobileNumber(mobileNumber);
        request.setUcc(ucc);
        request.setTotp(totp);

        Map<String, String> headers = new HashMap<>();
        headers.put("neo-fin-key", config.getNeoFinKey());
        headers.put("Authorization", config.getAccessToken());
        headers.put("Content-Type", "application/json");

        TotpLoginResponse response = httpClient.post(LOGIN_URL, headers, request, TotpLoginResponse.class);
        
        if (response != null && response.getData() != null) {
            config.setViewToken(response.getData().getToken());
            config.setSidView(response.getData().getSid());
            log.info("TOTP login successful. View token obtained.");
        }
        
        return response;
    }

    public TotpValidateResponse totpValidate(String mpin) {
        log.info("Attempting TOTP validation (2FA)");

        TotpValidateRequest request = new TotpValidateRequest();
        request.setMpin(mpin);

        Map<String, String> headers = new HashMap<>();
        headers.put("neo-fin-key", config.getNeoFinKey());
        headers.put("Authorization", config.getAccessToken());
        headers.put("Auth", config.getViewToken());
        headers.put("sid", config.getSidView());
        headers.put("Content-Type", "application/json");

        TotpValidateResponse response = httpClient.post(VALIDATE_URL, headers, request, TotpValidateResponse.class);

        if (response != null && response.getData() != null) {
            config.setSessionToken(response.getData().getToken());
            config.setSidSession(response.getData().getSid());
            config.setServerId(response.getData().getHsServerId());
            config.setBaseUrl(response.getData().getBaseUrl());
            log.info("TOTP validation successful. Session token obtained.");
        }

        return response;
    }
}
