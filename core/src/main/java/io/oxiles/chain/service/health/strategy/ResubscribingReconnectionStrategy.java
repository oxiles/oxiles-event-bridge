package io.oxiles.chain.service.health.strategy;

import io.oxiles.chain.service.BlockchainService;
import io.oxiles.service.SubscriptionService;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class ResubscribingReconnectionStrategy implements ReconnectionStrategy {

    private SubscriptionService subscriptionService;
    private BlockchainService blockchainService;

    @Override
    public void resubscribe() {
        //TODO need to figure out if we need to unregister
        subscriptionService.resubscribeToAllSubscriptions(blockchainService.getNodeName());

        blockchainService.reconnect();
    }
}
