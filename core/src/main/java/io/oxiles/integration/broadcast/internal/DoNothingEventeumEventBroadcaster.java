package io.oxiles.integration.broadcast.internal;

import io.oxiles.dto.event.filter.ContractEventFilter;
import io.oxiles.model.TransactionMonitoringSpec;

/**
 * A dummy broadcaster that does nothing.
 *
 * (Used in single instance mode)
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
public class DoNothingEventeumEventBroadcaster implements EventeumEventBroadcaster {

    @Override
    public void broadcastEventFilterAdded(ContractEventFilter filter) {
        //DO NOTHING!
    }

    @Override
    public void broadcastEventFilterRemoved(ContractEventFilter filter) {
        //DO NOTHING!
    }

    @Override
    public void broadcastTransactionMonitorAdded(TransactionMonitoringSpec spec) {
        //DO NOTHING!
    }

    @Override
    public void broadcastTransactionMonitorRemoved(TransactionMonitoringSpec spec) {
        //DO NOTHING
    }
}
