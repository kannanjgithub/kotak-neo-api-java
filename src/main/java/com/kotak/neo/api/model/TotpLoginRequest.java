package com.kotak.neo.api.model;

import lombok.Data;

@Data
public class TotpLoginRequest {
    private String mobileNumber;
    private String ucc;
    private String totp;
}
