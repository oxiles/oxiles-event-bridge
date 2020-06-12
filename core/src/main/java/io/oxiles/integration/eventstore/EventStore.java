package io.oxiles.integration.eventstore;

import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.model.LatestBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * Interface for integrating with an event store, in order to obtain events for a specified signature.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
public interface EventStore {
    Page<ContractEventDetails> getContractEventsForSignature(
            String eventSignature, String contractAddress, PageRequest pagination);

    Optional<LatestBlock> getLatestBlockForNode(String nodeName);

    boolean txExistsByHashAndNodeType(String hash, String nodeType);

    boolean isPagingZeroIndexed();

    default Optional<HCSMessageTransactionDetails> getLatestMessageFromTopic(String mirrorNode, String topicId) {
        return Optional.empty();
    }
}
