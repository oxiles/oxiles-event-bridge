package io.oxiles.integration.broadcast.blockchain;

import io.oxiles.chain.service.strategy.HashGraphTokenTransferData;
import io.oxiles.dto.block.BlockDetails;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.dto.message.*;
import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.integration.RabbitSettings;
import io.oxiles.utils.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * A RabbitBlockChainEventBroadcaster that broadcasts the events to a RabbitMQ exchange.
 *
 * The routing key for each message will defined by the routingKeyPrefix configured,
 * plus filterId for new contract events.
 *
 * The exchange and routingKeyPrefix can be configured via the
 * rabbitmq.exchange and rabbitmq.routingKeyPrefix properties.
 *
 * @author ioBuilders technical team <tech@io.builders>
 */
public class RabbitBlockChainEventBroadcaster implements BlockchainEventBroadcaster {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitBlockChainEventBroadcaster.class);

    private RabbitTemplate rabbitTemplate;
    private RabbitSettings rabbitSettings;

    public RabbitBlockChainEventBroadcaster(RabbitTemplate rabbitTemplate, RabbitSettings rabbitSettings) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitSettings = rabbitSettings;
    }

    @Override
    public void broadcastNewBlock(BlockDetails block) {
        final EventeumMessage<BlockDetails> message = createBlockEventMessage(block);
        rabbitTemplate.convertAndSend(this.rabbitSettings.getExchange(),
                String.format("%s", this.rabbitSettings.getBlockEventsRoutingKey()),
                message);

        LOG.info(String.format("New block sent: [%s] to exchange [%s] with routing key [%s]",
                JSON.stringify(message),
                this.rabbitSettings.getExchange(),
                this.rabbitSettings.getBlockEventsRoutingKey())
        );
    }

    @Override
    public void broadcastContractEvent(ContractEventDetails eventDetails) {
        final EventeumMessage<ContractEventDetails> message = createContractEventMessage(eventDetails);
        rabbitTemplate.convertAndSend(this.rabbitSettings.getExchange(),
                String.format("%s.%s", this.rabbitSettings.getContractEventsRoutingKey(), eventDetails.getFilterId()),
                message);

        LOG.info(String.format("New contract event sent: [%s] to exchange [%s] with routing key [%s.%s]",
                JSON.stringify(message),
                this.rabbitSettings.getExchange(),
                this.rabbitSettings.getContractEventsRoutingKey(),
                eventDetails.getFilterId()));
    }

    @Override
    public void broadcastTransaction(TransactionDetails transactionDetails) {
        final EventeumMessage<TransactionDetails> message = createTransactionEventMessage(transactionDetails);
        rabbitTemplate.convertAndSend(this.rabbitSettings.getExchange(),
                String.format("%s.%s", this.rabbitSettings.getTransactionEventsRoutingKey(), transactionDetails.getHash()),
                message);

        LOG.info(String.format("New transaction event sent: [%s] to exchange [%s] with routing key [%s.%s]",
                JSON.stringify(message),
                this.rabbitSettings.getExchange(),
                this.rabbitSettings.getTransactionEventsRoutingKey(),
                transactionDetails.getHash()
                )
        );
    }



    @Override
    public void broadcastTransaction(HashGraphTransactionData hashGraphTransactionData) {

    }

    @Override
    public void broadcastTransaction(HashGraphTokenTransferData hashGraphTransactionData) {

    }
    @Override
    public void broadcastMessageTransaction(HCSMessageTransactionDetails hcsMessageTransactionDetails) {
        final EventeumMessage<HCSMessageTransactionDetails> message = new HCSMessageTransactionEvent(hcsMessageTransactionDetails);
        rabbitTemplate.convertAndSend(this.rabbitSettings.getExchange(),
                String.format("%s.%s", this.rabbitSettings.getTransactionEventsRoutingKey(), Integer.valueOf(hcsMessageTransactionDetails.hashCode()).toString()),
                message);

        LOG.info(String.format("New transaction event sent: [%s] to exchange [%s] with routing key [%s.%s]",
                JSON.stringify(message),
                this.rabbitSettings.getExchange(),
                this.rabbitSettings.getTransactionEventsRoutingKey(),
                Integer.valueOf(hcsMessageTransactionDetails.hashCode()).toString()
                )
        );
    }

    protected EventeumMessage<BlockDetails> createBlockEventMessage(BlockDetails blockDetails) {
        return new BlockEvent(blockDetails);
    }

    protected EventeumMessage<ContractEventDetails> createContractEventMessage(ContractEventDetails contractEventDetails) {
        return new ContractEvent(contractEventDetails);
    }

    protected EventeumMessage<TransactionDetails> createTransactionEventMessage(TransactionDetails transactionDetails) {
        return new TransactionEvent(transactionDetails);
    }

}
