package io.oxiles.chain.service.health.strategy;

import io.oxiles.chain.service.BlockchainService;
import io.oxiles.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;

/**
 * An NodeFailureListener that reconnects the blockchain service and resubscribes to all
 * active event subscriptions on recovery.
 *
 * Note:  All subscriptions are unregistered before being reregistered.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
@Slf4j
public class HttpReconnectionStrategy extends ResubscribingReconnectionStrategy {

    public HttpReconnectionStrategy(SubscriptionService subscriptionService, BlockchainService blockchainService) {
        super(subscriptionService, blockchainService);
    }

    @Override
    public void reconnect() {
        //Do Nothing
    }
}
