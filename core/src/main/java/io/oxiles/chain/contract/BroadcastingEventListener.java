package io.oxiles.chain.contract;

import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * A contract event listener that broadcasts the event details via the configured broadcaster.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BroadcastingEventListener implements ContractEventListener {

    private BlockchainEventBroadcaster eventBroadcaster;

    @Autowired
    public BroadcastingEventListener(BlockchainEventBroadcaster eventBroadcaster) {
        this.eventBroadcaster = eventBroadcaster;
    }

    @Override
    public void onEvent(ContractEventDetails eventDetails) {
        eventBroadcaster.broadcastContractEvent(eventDetails);
    }
}
