package io.oxiles.server.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicCreateTransaction;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicId;
import com.hedera.hashgraph.sdk.mirror.MirrorClient;
import com.hedera.hashgraph.sdk.mirror.MirrorConsensusTopicQuery;
import io.oxiles.dto.block.BlockDetails;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.message.EventeumMessage;
import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.integration.HCSSettings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test-db-hcs.properties")
public class HCSBroadcasterIT extends BroadcasterSmokeTest {

    private static final MirrorClient TESTNET_MIRROR_CLIENT = new MirrorClient("hcs.testnet.mirrornode.hedera.com:5600");

    @Autowired
    private HCSSettings hcsSettings;

    @Autowired
    private ObjectMapper objectMapper;

    private static boolean subscribed;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        if (!subscribed) {
            Thread.sleep(5000); // Wait for the mirror node to get new topics.
            ConsensusTopicId blockEventsTopic = ConsensusTopicId.fromString(hcsSettings.getTopic().getBlockEvents());
            ConsensusTopicId contractEventsTopic = ConsensusTopicId.fromString(hcsSettings.getTopic().getContractEvents());
            ConsensusTopicId transactionEventsTopic = ConsensusTopicId.fromString(hcsSettings.getTopic().getTransactionEvents());

            subscribeToTopic(blockEventsTopic, message -> onBlockMessageReceived((BlockDetails) message.getDetails()));
            subscribeToTopic(contractEventsTopic, message -> onContractEventMessageReceived((ContractEventDetails) message.getDetails()));
            subscribeToTopic(transactionEventsTopic, message -> onTransactionMessageReceived((TransactionDetails) message.getDetails()));
            subscribed = true;
        }
    }

    private void subscribeToTopic(ConsensusTopicId topic, Consumer<EventeumMessage<?>> consumer) {
        new MirrorConsensusTopicQuery()
                .setTopicId(topic)
                .subscribe(TESTNET_MIRROR_CLIENT, resp -> {
                            try {
                                EventeumMessage<?> message = objectMapper.readValue(new String(resp.message, StandardCharsets.UTF_8), EventeumMessage.class);
                                consumer.accept(message);
                            } catch (IOException e) {
                                Assert.fail(e.getMessage());
                            }
                        },
                        e -> Assert.fail(e.getMessage()));
    }

    @TestConfiguration
    static class Configuration {

        @Autowired
        Client client;

        @Autowired
        HCSSettings hcsSettings;

        @PostConstruct
        public void createAndSetTopics() throws HederaStatusException {
            hcsSettings.getTopic().setBlockEvents(createTopic());
            hcsSettings.getTopic().setContractEvents(createTopic());
            hcsSettings.getTopic().setTransactionEvents(createTopic());
        }

        private String createTopic() throws HederaStatusException {
            final TransactionId transactionId = new ConsensusTopicCreateTransaction().execute(client);
            return transactionId.getReceipt(client).getConsensusTopicId().toString();
        }
    }
}
