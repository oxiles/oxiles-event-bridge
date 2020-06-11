package io.oxiles.integration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "hcs")
public class HCSSettings {

    private boolean testnet;
    private Topics topic = new Topics();
    private Account account = new Account();

    @Data
    public static class Topics {
        private String contractEvents;
        private String blockEvents;
        private String transactionEvents;
    }

    @Data
    public static class Account {
        private String id;
        private String keystore;
        private String password;
        private String privateKey;
    }
}
