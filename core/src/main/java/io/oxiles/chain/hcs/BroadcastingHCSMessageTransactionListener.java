package io.oxiles.chain.hcs;

import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import io.oxiles.integration.eventstore.EventStore;
import io.oxiles.integration.eventstore.SaveableEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("hcsMessageTransactionListener")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BroadcastingHCSMessageTransactionListener implements HCSMessageTransactionListener {

    private EventStore eventStore;
    private BlockchainEventBroadcaster eventBroadcaster;

    @Autowired
    public BroadcastingHCSMessageTransactionListener(EventStore eventStore, BlockchainEventBroadcaster eventBroadcaster) {
        this.eventStore = eventStore;
        this.eventBroadcaster = eventBroadcaster;
    }

    @Override
    public void onMessageTransaction(HCSMessageTransactionDetails hcsMessageTransactionDetails) {
        eventBroadcaster.broadcastMessageTransaction(hcsMessageTransactionDetails);
        if (eventStore instanceof SaveableEventStore){
            ((SaveableEventStore)eventStore).save(hcsMessageTransactionDetails);
        }
    }
}
