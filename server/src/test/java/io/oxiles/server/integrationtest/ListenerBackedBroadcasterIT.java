package io.oxiles.server.integrationtest;

import io.oxiles.dto.block.BlockDetails;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import io.oxiles.integration.broadcast.blockchain.ListenerInvokingBlockchainEventBroadcaster;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test-db-listener.properties")
@Import(ListenerBackedBroadcasterIT.ListenerConfig.class)
public class ListenerBackedBroadcasterIT extends BroadcasterSmokeTest {

    @Override
    protected List<BlockDetails> getBroadcastBlockMessages() {
        return ListenerConfig.broadcastBlockMessages;
    }

    @Override
    protected List<ContractEventDetails> getBroadcastContractEvents() {
        return ListenerConfig.broadcastContractEvents;
    }

    @Override
    protected List<TransactionDetails> getBroadcastTransactionMessages() {
        return ListenerConfig.broadcastTransactionMessages;
    }

    @TestConfiguration
    static class ListenerConfig {

        private static List<BlockDetails> broadcastBlockMessages = new ArrayList<>();

        private static List<ContractEventDetails> broadcastContractEvents = new ArrayList<>();

        private static List<TransactionDetails> broadcastTransactionMessages = new ArrayList<>();

        private static List<HCSMessageTransactionDetails> broadcastMessageTransactionMessages = new ArrayList<>();

        @Bean
        public BlockchainEventBroadcaster listenerBroadcaster() {

            return new ListenerInvokingBlockchainEventBroadcaster(new ListenerInvokingBlockchainEventBroadcaster.OnBlockchainEventListener() {
                @Override
                public void onNewBlock(BlockDetails block) {
                    broadcastBlockMessages.add(block);
                }

                @Override
                public void onContractEvent(ContractEventDetails eventDetails) {
                    broadcastContractEvents.add(eventDetails);
                }

                @Override
                public void onTransactionEvent(TransactionDetails transactionDetails) {
                    broadcastTransactionMessages.add(transactionDetails);
                }

                @Override
                public void onTransactionEvent(HCSMessageTransactionDetails hcsMessageTransactionDetails) {
                    broadcastMessageTransactionMessages.add(hcsMessageTransactionDetails);
                }
            });
        }
    }
}
