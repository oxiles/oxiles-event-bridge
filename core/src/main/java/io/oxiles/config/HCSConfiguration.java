package io.oxiles.config;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import io.oxiles.integration.HCSSettings;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "broadcaster.type", havingValue = "HCS")
public class HCSConfiguration {

    @Bean
    public Client hederaClient(HCSSettings hcsSettings) throws IOException {
        Client client = hcsSettings.isTestnet() ? Client.forTestnet() : Client.forMainnet();

        Ed25519PrivateKey operatorPrivateKey;
        if (!StringUtils.isEmpty(hcsSettings.getAccount().getKeystore()) && !StringUtils.isEmpty(hcsSettings.getAccount().getPassword())) {
            operatorPrivateKey = Ed25519PrivateKey.fromKeystore(hcsSettings.getAccount().getKeystore().getBytes(), hcsSettings.getAccount().getPassword());
        } else if (StringUtils.isEmpty(hcsSettings.getAccount().getPrivateKey())) {
            operatorPrivateKey = Ed25519PrivateKey.fromString(hcsSettings.getAccount().getPrivateKey());
        } else {
            throw new BeanCreationException("Invalid HCS account configuration. Provide a valid json keystore and password or a valid private key.");
        }

        client.setOperator(AccountId.fromString(hcsSettings.getAccount().getId()), operatorPrivateKey);

        return client;
    }
}
