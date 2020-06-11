package io.oxiles.chain.hashgraph;

import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import io.oxiles.integration.eventstore.EventStore;
import io.oxiles.integration.eventstore.SaveableEventStore;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HashgraphContractTransactionListener implements ContractTransactionListener {

    private EventStore eventStore;
    private BlockchainEventBroadcaster eventBroadcaster;

    @Override
    public void onTransaction(HashGraphTransactionData txData) {
        if (!eventStore.txExistsById(txData.getId())) {
            //broadcasting
            eventBroadcaster.broadcastTransaction(txData);
            if (eventStore instanceof SaveableEventStore){
                ((SaveableEventStore)eventStore).save(txData);
            }
        }
    }
}
