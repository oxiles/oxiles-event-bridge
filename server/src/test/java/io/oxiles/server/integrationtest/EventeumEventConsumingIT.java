package io.oxiles.server.integrationtest;

import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.event.ContractEventStatus;
import io.oxiles.dto.event.filter.ContractEventFilter;
import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.dto.transaction.TransactionStatus;
import io.oxiles.integration.broadcast.internal.KafkaEventeumEventBroadcaster;
import io.oxiles.model.TransactionIdentifierType;
import io.oxiles.model.TransactionMonitoringSpec;
import io.oxiles.repository.ContractEventFilterRepository;
import io.oxiles.repository.TransactionMonitoringSpecRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Hash;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations="classpath:application-test-multiinstance.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventeumEventConsumingIT extends BaseKafkaIntegrationTest {

    @Autowired
    private KafkaEventeumEventBroadcaster broadcaster;

    @Autowired
    private ContractEventFilterRepository filterRepo;

    @Autowired
    private TransactionMonitoringSpecRepository txMonitorRepo;

    @Test
    public void testFilterAddedEventRegistersFilter() throws Exception {

        doBroadcastFilterAddedEventAndVerifyRegistered(deployEventEmitterContract());
    }

    @Test
    public void testFilterRemovedEventRemovesFilter() throws Exception {
        final EventEmitter emitter = deployEventEmitterContract();

        final ContractEventFilter filter = doBroadcastFilterAddedEventAndVerifyRegistered(emitter);

        broadcaster.broadcastEventFilterRemoved(filter);

        waitForFilterEventMessages(2);

        clearMessages();

        emitter.emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue").send();

        waitForBroadcast();

        assertEquals(0, getBroadcastContractEvents().size());
    }

    @Test
    public void testTxMonitorAddedEventRegistersMonitor() throws Exception {

        final String signedTxHex = createRawSignedTransactionHex();
        final String txHash = Hash.sha3(signedTxHex);

        final TransactionMonitoringSpec spec = new TransactionMonitoringSpec();
        spec.setNodeName("default");
        spec.setTransactionIdentifierValue(txHash);
        spec.setType(TransactionIdentifierType.HASH);

        broadcaster.broadcastTransactionMonitorAdded(spec);

        waitForTransactionMonitorEventMessages(1);

        assertEquals(txHash, sendRawTransaction(signedTxHex));

        waitForTransactionMessages(1);

        assertEquals(1, getBroadcastTransactionMessages().size());

        final TransactionDetails txDetails = getBroadcastTransactionMessages().get(0);
        assertEquals(txHash, txDetails.getHash());
        assertEquals(TransactionStatus.UNCONFIRMED, txDetails.getStatus());
    }

    @Test
    public void testTxMonitorRemovedEventRemovesMonitor() throws Exception {

        final String signedTxHex = createRawSignedTransactionHex();
        final String txHash = Hash.sha3(signedTxHex);

        final TransactionMonitoringSpec spec = new TransactionMonitoringSpec();
        spec.setNodeName("default");
        spec.setTransactionIdentifierValue(txHash);
        spec.setType(TransactionIdentifierType.HASH);

        broadcaster.broadcastTransactionMonitorAdded(spec);

        waitForTransactionMonitorEventMessages(1);

        broadcaster.broadcastTransactionMonitorRemoved(spec);

        waitForTransactionMonitorEventMessages(1);

        assertEquals(txHash, sendRawTransaction(signedTxHex));

        waitForBroadcast();

        assertEquals(0, getBroadcastTransactionMessages().size());
    }

    private ContractEventFilter doBroadcastFilterAddedEventAndVerifyRegistered(
            EventEmitter emitter) throws Exception {

        final ContractEventFilter filter = createDummyEventFilter(emitter.getContractAddress());

        broadcaster.broadcastEventFilterAdded(filter);

        waitForFilterEventMessages(1);

        emitter.emitEvent(stringToBytes("BytesValue"), BigInteger.TEN, "StringValue").send();

        waitForContractEventMessages(1);

        assertEquals(1, getBroadcastContractEvents().size());

        final ContractEventDetails eventDetails = getBroadcastContractEvents().get(0);
        verifyDummyEventDetails(filter, eventDetails, ContractEventStatus.UNCONFIRMED);

        return filter;
    }

    private void waitForFilterEventMessages(int expectedMessageCounnt) throws InterruptedException {
        waitForMessages(expectedMessageCounnt, getBroadcastFilterEventMessages());

        //Wait an extra 2 seconds because there may be a race condition
        Thread.sleep(2000);
    }

    private void waitForTransactionMonitorEventMessages(int expectedMessageCounnt) throws InterruptedException {
        waitForMessages(expectedMessageCounnt, getBroadcastTransactionEventMessages());

        //Wait an extra 2 seconds because there may be a race condition
        Thread.sleep(2000);
    }
}
