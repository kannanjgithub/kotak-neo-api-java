package com.kotak.neo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBookResponse {
    private List<OrderDetails> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderDetails {
        private String nOrdNo;
        private String ts; // Trading Symbol
        private String st; // Status
        private String qty;
        private String pr; // Price
        private String tt; // Transaction Type
        private String pc; // Product Code
        private String es; // Exchange Segment
    }
}
