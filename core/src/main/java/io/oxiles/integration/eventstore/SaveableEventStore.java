package io.oxiles.integration.eventstore;

import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.model.LatestBlock;

/**
 * Interface for integrating with an event store that supports direct saving of events.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
public interface SaveableEventStore extends EventStore {
    void save(ContractEventDetails contractEventDetails);

    void save(LatestBlock latestBlock);

    void save(HashGraphTransactionData transactionData);
}
