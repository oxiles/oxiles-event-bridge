package io.oxiles.integration.broadcast.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaNetworkException;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.consensus.ConsensusMessageSubmitTransaction;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicId;
import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.dto.block.BlockDetails;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.message.*;
import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.dto.message.*;
import io.oxiles.integration.HCSSettings;
import io.oxiles.integration.broadcast.BroadcastException;
import io.oxiles.dto.message.*;

public class HCSBlockChainEventBroadcaster implements BlockchainEventBroadcaster {

    HCSSettings hcsSettings;
    Client client;
    ObjectMapper objectMapper;

    public HCSBlockChainEventBroadcaster(HCSSettings hcsSettings, Client client, ObjectMapper objectMapper) {
        this.hcsSettings = hcsSettings;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    public void broadcastNewBlock(BlockDetails block) {
        this.submitMessage(hcsSettings.getTopic().getBlockEvents(), new BlockEvent(block));
    }

    @Override
    public void broadcastContractEvent(ContractEventDetails eventDetails) {
        this.submitMessage(hcsSettings.getTopic().getContractEvents(), new ContractEvent(eventDetails));
    }

    @Override
    public void broadcastTransaction(TransactionDetails transactionDetails) {
        this.submitMessage(hcsSettings.getTopic().getTransactionEvents(), new TransactionEvent(transactionDetails));
    }

    @Override
    public void broadcastTransaction(HashGraphTransactionData hederaTransactionData) {
        this.submitMessage(hcsSettings.getTopic().getTransactionEvents(), new HashgraphTransactionEvent(hederaTransactionData));
    }

    private void submitMessage(String topic, EventeumMessage<?> eventeumMessage) {
        try {
            new ConsensusMessageSubmitTransaction()
                    .setTopicId(ConsensusTopicId.fromString(topic))
                    .setMessage(objectMapper.writeValueAsString(eventeumMessage))
                    .execute(client)
                    .getReceipt(client);
        } catch (HederaStatusException | HederaNetworkException | JsonProcessingException e) {
            throw new BroadcastException("Unable to send message", e);
        }
    }
}
