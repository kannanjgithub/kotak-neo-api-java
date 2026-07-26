package com.kotak.neo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequest {
    @JsonProperty("am")
    private String amo; // "YES" or "NO"
    @JsonProperty("dq")
    private String disclosedQuantity;
    @JsonProperty("es")
    private String exchangeSegment; // e.g., "nse_cm"
    @JsonProperty("mp")
    private String marketProtection;
    @JsonProperty("pc")
    private String productCode; // "CNC", "MIS", "NRML"
    @JsonProperty("pf")
    private String priceFlag; // "N"
    @JsonProperty("pr")
    private String price;
    @JsonProperty("pt")
    private String priceType; // "L" (Limit), "M" (Market)
    @JsonProperty("qt")
    private String quantity;
    @JsonProperty("rt")
    private String retentionType; // "DAY", "IOC"
    @JsonProperty("tp")
    private String triggerPrice;
    @JsonProperty("ts")
    private String tradingSymbol;
    @JsonProperty("tt")
    private String transactionType; // "B" (Buy), "S" (Sell)
    
    // For modify/cancel
    @JsonProperty("no")
    private String orderNo;
    @JsonProperty("on")
    private String cancelOrderNo;
}
