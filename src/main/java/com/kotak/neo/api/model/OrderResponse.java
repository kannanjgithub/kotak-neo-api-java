package com.kotak.neo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {
    private String nOrdNo;
    private String status;
    private String message;
}
