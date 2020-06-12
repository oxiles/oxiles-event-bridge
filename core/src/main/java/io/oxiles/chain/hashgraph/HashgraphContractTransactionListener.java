package io.oxiles.chain.hashgraph;

import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import io.oxiles.integration.eventstore.EventStore;
import io.oxiles.integration.eventstore.SaveableEventStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class HashgraphContractTransactionListener implements ContractTransactionListener {

    private EventStore eventStore;
    private BlockchainEventBroadcaster eventBroadcaster;

    @Override
    public void onTransaction(HashGraphTransactionData txData) {
        if (!eventStore.txExistsById(txData.getId())) {
            //broadcasting
            log.info("New Transaction id detected: {}",txData.getId());
            eventBroadcaster.broadcastTransaction(txData);
            if (eventStore instanceof SaveableEventStore){
                ((SaveableEventStore)eventStore).save(txData);
            }
        }
    }
}