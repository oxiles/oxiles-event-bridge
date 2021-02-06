package io.oxiles.integration.broadcast.blockchain;

import io.oxiles.chain.service.strategy.HashGraphTokenTransferData;
import io.oxiles.dto.block.BlockDetails;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.dto.message.*;
import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.dto.event.filter.ContractEventFilter;
import io.oxiles.integration.KafkaSettings;
import io.oxiles.utils.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

/**
 * A BlockchainEventBroadcaster that broadcasts the events to a Kafka queue.
 *
 * The key for each message will defined by the correlationIdStrategy if configured,
 * or a combination of the transactionHash, blockHash and logIndex otherwise.
 *
 * The topic names for block and contract events can be configured via the
 * kafka.topic.contractEvents and kafka.topic.blockEvents properties.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
public class KafkaBlockchainEventBroadcaster implements BlockchainEventBroadcaster {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaBlockchainEventBroadcaster.class);

    private KafkaTemplate<String, EventeumMessage> kafkaTemplate;

    private KafkaSettings kafkaSettings;

    private CrudRepository<ContractEventFilter, String> filterRespository;

    public KafkaBlockchainEventBroadcaster(KafkaTemplate<String, EventeumMessage> kafkaTemplate,
                                    KafkaSettings kafkaSettings,
                                    CrudRepository<ContractEventFilter, String> filterRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaSettings = kafkaSettings;
        this.filterRespository = filterRepository;
    }

    @Override
    public void broadcastNewBlock(BlockDetails block) {
        final EventeumMessage<BlockDetails> message = createBlockEventMessage(block);
        LOG.info("Sending block message: " + JSON.stringify(message));

        kafkaTemplate.send(kafkaSettings.getBlockEventsTopic(), message.getId(), message);
    }

    @Override
    public void broadcastTransaction(HashGraphTransactionData hashGraphTransactionData) {
        final EventeumMessage<HashGraphTransactionData> message = createTransactionEventMessage(hashGraphTransactionData);
        LOG.info("Sending transaction event message: " + JSON.stringify(message));

        kafkaTemplate.send(kafkaSettings.getTransactionEventsTopic(), hashGraphTransactionData.getHash(), message);

    }

    @Override
    public void broadcastTransaction(HashGraphTokenTransferData tokenTransferData) {
        final EventeumMessage<HashGraphTokenTransferData> message = createTokenTransferEventMessage(tokenTransferData);
        LOG.info("Sending token tranfer event message: " + JSON.stringify(message));

        kafkaTemplate.send(kafkaSettings.getTransactionEventsTopic(), tokenTransferData.getTransactionId(), message);

    }


    @Override
    public void broadcastMessageTransaction(HCSMessageTransactionDetails hcsMessageTransactionDetails) {
        final EventeumMessage<HCSMessageTransactionDetails> message = new HCSMessageTransactionEvent(hcsMessageTransactionDetails);
        LOG.info("Sending transaction event message: " + JSON.stringify(message));

        kafkaTemplate.send(kafkaSettings.getTransactionEventsTopic(), Integer.valueOf(hcsMessageTransactionDetails.hashCode()).toString(), message);
    }

    @Override
    public void broadcastContractEvent(ContractEventDetails eventDetails) {
        final EventeumMessage<ContractEventDetails> message = createContractEventMessage(eventDetails);
        LOG.info("Sending contract event message: " + JSON.stringify(message));

        kafkaTemplate.send(kafkaSettings.getContractEventsTopic(), getContractEventCorrelationId(message), message);
    }

    @Override
    public void broadcastTransaction(TransactionDetails transactionDetails) {
        final EventeumMessage<TransactionDetails> message = createTransactionEventMessage(transactionDetails);
        LOG.info("Sending transaction event message: " + JSON.stringify(message));

        kafkaTemplate.send(kafkaSettings.getTransactionEventsTopic(), transactionDetails.getBlockHash(), message);
    }

    protected EventeumMessage<BlockDetails> createBlockEventMessage(BlockDetails blockDetails) {
        return new BlockEvent(blockDetails);
    }

    protected EventeumMessage<ContractEventDetails> createContractEventMessage(ContractEventDetails contractEventDetails) {
        return new ContractEvent(contractEventDetails);
    }

    protected EventeumMessage<HashGraphTransactionData> createTransactionEventMessage(HashGraphTransactionData hashGraphTransactionData) {
        return new HashgraphTransactionEvent(hashGraphTransactionData);
    }

    protected EventeumMessage<HashGraphTokenTransferData> createTokenTransferEventMessage(HashGraphTokenTransferData tokenTransferData) {
        return new HashgraphTokenTransferEvent(tokenTransferData);
    }

    protected EventeumMessage<TransactionDetails> createTransactionEventMessage(TransactionDetails transactionDetails) {
        return new TransactionEvent(transactionDetails);
    }

    private String getContractEventCorrelationId(EventeumMessage<ContractEventDetails> message) {
        final Optional<ContractEventFilter> filter = filterRespository.findById(message.getDetails().getFilterId());

        if (!filter.isPresent() || filter.get().getCorrelationIdStrategy() == null) {
            return message.getId();
        }

        return filter
                .get()
                .getCorrelationIdStrategy()
                .getCorrelationId(message.getDetails());
    }
}
