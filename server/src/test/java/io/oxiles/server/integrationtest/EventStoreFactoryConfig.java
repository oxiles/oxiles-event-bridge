package io.oxiles.server.integrationtest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.oxiles.chain.service.strategy.HashGraphTokenTransferData;
import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.dto.block.BlockDetails;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.dto.message.HashgraphTokenTransferEvent;
import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.factory.EventStoreFactory;
import io.oxiles.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import io.oxiles.integration.broadcast.blockchain.ListenerInvokingBlockchainEventBroadcaster;
import io.oxiles.integration.eventstore.SaveableEventStore;
import io.oxiles.model.LatestBlock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

@TestConfiguration
public class EventStoreFactoryConfig {

    @Bean
    public BlockchainEventBroadcaster listenerBroadcaster() {

        return new ListenerInvokingBlockchainEventBroadcaster(new ListenerInvokingBlockchainEventBroadcaster.OnBlockchainEventListener() {
            @Override
            public void onNewBlock(BlockDetails block) {
                //DO NOTHING
            }

            @Override
            public void onContractEvent(ContractEventDetails eventDetails) {
                //DO NOTHING
            }

            @Override
            public void onTransactionEvent(TransactionDetails transactionDetails) {
                //DO NOTHING
            }

            @Override
            public void onTransactionEvent(HCSMessageTransactionDetails hcsMessageTransactionDetails) {
                //DO NOTHING
            }
        });
    }

    @Bean
    public EventStoreFactory eventStoreFactory() {
        return new EventStoreFactory() {

            @Override
            public SaveableEventStore build() {
                return new SaveableEventStore() {
                    @Override
                    public void save(ContractEventDetails contractEventDetails) {
                        savedEvents().getEntities().add(contractEventDetails);
                    }

                    @Override
                    public void save(LatestBlock latestBlock) {
                        savedLatestBlock().getEntities().clear();
                        savedLatestBlock().getEntities().add(latestBlock);
                    }

                    @Override
                    public void save(HashGraphTransactionData txData) {
                        savedTxs().getEntities().clear();
                        savedTxs().getEntities().add(txData);
                    }

                    @Override
                    public void save(HashGraphTokenTransferData txData) {
                        savedTransfers().getEntities().clear();
                        savedTransfers().getEntities().add(txData);
                    }



                    @Override
                    public Page<ContractEventDetails> getContractEventsForSignature(
                            String eventSignature, String contractAddress, PageRequest pagination) {
                        return null;
                    }

                    @Override
                    public Optional<LatestBlock> getLatestBlockForNode(String nodeName) {
                        return Optional.empty();
                    }

                    @Override
                    public boolean isPagingZeroIndexed() {
                        return false;
                    }

                    @Override
                    public boolean txExistsByHashAndNodeType(String hash, String nodeType) {
                        return savedTxs().getEntities().stream().anyMatch(tx->
                                tx.getHash().equals(hash) && tx.getNodeType().equals(nodeType));
                    }

                    @Override
                    public boolean transferExistsByHashAndNodeType(String hash, String nodeType) {
                        return savedTransfers().getEntities().stream().anyMatch(tx->
                                tx.getTransactionId().equals(hash) && tx.getNodeType().equals(nodeType));
                    }
                };
            }
        };
    }

    @Bean
    Entities<ContractEventDetails> savedEvents() {
        return new Entities<>();
    }

    @Bean
    Entities<LatestBlock> savedLatestBlock() {
        return new Entities<>();
    }

    public class Entities<T> {
        List<T> entities = new ArrayList<>();

        public List<T> getEntities() {
            return entities;
        }
    }

    @Bean
    Entities<HashGraphTransactionData> savedTxs() {
        return new Entities<>();
    }

    @Bean
    Entities<HashGraphTokenTransferData> savedTransfers() {
        return new Entities<>();
    }

    public class EventStoreSavedContractEvents {
        private List<ContractEventDetails> savedEvents = new ArrayList<>();

        public List<ContractEventDetails> getSavedEvents() {
            return savedEvents;
        }
    }
}
