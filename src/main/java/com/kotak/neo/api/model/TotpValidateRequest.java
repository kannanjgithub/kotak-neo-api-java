package com.kotak.neo.api.model;

import lombok.Data;

@Data
public class TotpValidateRequest {
    private String mpin;
}
