package com.kotak.neo.api;

import com.kotak.neo.api.model.OrderBookResponse;
import com.kotak.neo.api.model.OrderRequest;
import com.kotak.neo.api.model.OrderResponse;
import com.kotak.neo.api.model.TotpLoginResponse;
import com.kotak.neo.api.model.TotpValidateResponse;
import com.kotak.neo.api.websocket.NeoWebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class Demo {
    private static final Logger log = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        // Replace with actual credentials
        String environment = "uat";
        String accessToken = "42cbf1f8-e609-4015-8d43-6347bf77550b";
        String mobileNumber = "+918971114094";
        String ucc = "YN0NL";
        String totp = "051301"; // Current TOTP from app
        String mpin = "169240";

        NeoAPI api = new NeoAPI(environment, accessToken, "", "");

        try {
            // 1. TOTP Login
            TotpLoginResponse loginResp = api.getAuth().totpLogin(mobileNumber, ucc, totp);
            log.info("Login Response: {}", loginResp);

            // 2. TOTP Validate (2FA)
            TotpValidateResponse validateResp = api.getAuth().totpValidate(mpin);
            log.info("Validate Response: {}", validateResp);

            // 3. Fetch Order Book
            OrderBookResponse orderBook = api.getReports().getOrderBook();
            log.info("Order Book size: {}", orderBook.getData() != null ? orderBook.getData().size() : 0);

            // 4. Place a Test Order
            OrderRequest orderReq = OrderRequest.builder()
                    .amo("NO")
                    .exchangeSegment("nse_cm")
                    .tradingSymbol("RELIANCE-EQ")
                    .transactionType("B")
                    .quantity("1")
                    .price("2500")
                    .priceType("L")
                    .productCode("CNC")
                    .retentionType("DAY")
                    .disclosedQuantity("0")
                    .marketProtection("0")
                    .priceFlag("N")
                    .triggerPrice("0")
                    .build();

            // OrderResponse orderResp = api.getOrders().placeOrder(orderReq);
            // log.info("Order Response: {}", orderResp);

            // 5. Init WebSocket
            api.initWebSocket("wss://mlhsm.kotaksecurities.com", new NeoWebSocketClient.NeoWebSocketListener() {
                @Override
                public void onOpen() {
                    log.info("WS Listener: Connected");
                    api.getWebSocket().subscribe("nse_cm|11536"); // RELIANCE
                }

                @Override
                public void onMessage(String message) {
                    log.info("WS Listener: Received text: {}", message);
                }

                @Override
                public void onBinaryMessage(ByteBuffer data) {
                    log.info("WS Listener: Received binary data (LTP/Depth)");
                }

                @Override
                public void onClose(int statusCode, String reason) {
                    log.info("WS Listener: Closed: {}", reason);
                }

                @Override
                public void onError(Throwable error) {
                    log.error("WS Listener: Error", error);
                }
            });

        } catch (Exception e) {
            log.error("Error in Demo", e);
        }
    }
}
