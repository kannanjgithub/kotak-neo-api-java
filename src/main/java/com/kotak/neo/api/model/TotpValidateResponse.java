package com.kotak.neo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TotpValidateResponse {
    private ValidateData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValidateData {
        private String token;
        private String sid;
        private String rid;
        private String hsServerId;
        private String dataCenter;
        private String baseUrl;
    }
}
