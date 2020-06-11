package io.oxiles.chain.factory;

import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.event.ContractEventStatus;
import io.oxiles.dto.event.parameter.EventParameter;
import io.oxiles.chain.converter.EventParameterConverter;
import io.oxiles.chain.service.domain.TransactionReceipt;
import io.oxiles.chain.settings.Node;
import io.oxiles.chain.util.Web3jUtil;
import io.oxiles.dto.event.filter.ContractEventFilter;
import io.oxiles.dto.event.filter.ContractEventSpecification;
import io.oxiles.dto.event.filter.ParameterDefinition;
import io.oxiles.dto.event.filter.ParameterType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultContractEventDetailsFactoryTest {

    //Values: 123, 0x00a329c0648769a73afac7f9381e08fb43dbea72, -42
    private static final String LOG_DATA = "0x000000000000000000000000000000000000000000000000000000000000007b00000000000000000000000000a329c0648769a73afac7f9381e08fb43dbea72ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd6";

    //Values: 456
    private static final String INDEXED_PARAM = "0x00000000000000000000000000000000000000000000000000000000000001c8";

    private static final String CONTRACT_ADDRESS = "0x7a55a28856d43bba3c6a7e36f2cee9a82923e99b";

    private static final String EVENT_NAME = "DummyEvent";

    private static final String ADDRESS = "0x2250683dbe4e0b90395c3c5d7def87784a2b916c";

    private static final BigInteger LOG_INDEX = BigInteger.TEN;

    private static final String TX_HASH = "0x1fb4a22baf926bd643d796e1332b73452b4eeb1dc6e8be787d4bf54dcccf4485";

    private static final BigInteger BLOCK_NUMBER = BigInteger.valueOf(12345);

    private static final String BLOCK_HASH = "0xf6c7c0822df1bce82b8edf55ab93f2e69ea80ef714801789fae3b3a08f761047";

    private static final String NETWORK_NAME = "ThisIsANetworkName";

    private static final BigInteger BLOCK_TIMESTAMP = BigInteger.TEN;

    public static final String NONCE = "0";

    public static final String EMPTY = "empty";

    private static final String FROM_ADDRESS = "0x5fd30686247835ee5e96567e29d88bD9A83dca52";

    private DefaultContractEventDetailsFactory underTest;

    private EventParameterConverter mockParameterCoverter;

    private org.web3j.protocol.core.methods.response.Log mockLog;

    private static ContractEventSpecification eventSpec;

    private ContractEventFilter filter;

    private EthBlock mockEthBlock;

    private TransactionReceipt mockTransactionReceipt;

    static {
        eventSpec = new ContractEventSpecification();
        eventSpec.setEventName(EVENT_NAME);
        eventSpec.setIndexedParameterDefinitions(
                Arrays.asList(new ParameterDefinition(0, ParameterType.build("UINT256"))));

        eventSpec.setNonIndexedParameterDefinitions(Arrays.asList(
                new ParameterDefinition(1, ParameterType.build("UINT256")),
                new ParameterDefinition(2, ParameterType.build("ADDRESS")),
                new ParameterDefinition(3, ParameterType.build("INT256"))));
    }

    @Before
    public void init() {
        mockParameterCoverter = mock(EventParameterConverter.class);

        mockLog = mock(org.web3j.protocol.core.methods.response.Log.class);
        mockEthBlock = mock(EthBlock.class);
        EthBlock.Block mockBlock = mock(EthBlock.Block.class);
        mockTransactionReceipt = mock(TransactionReceipt.class);

        when(mockLog.getData()).thenReturn(LOG_DATA);
        when(mockLog.getTopics()).thenReturn(Arrays.asList(null, INDEXED_PARAM));
        when(mockLog.getAddress()).thenReturn(ADDRESS);
        when(mockLog.getLogIndex()).thenReturn(LOG_INDEX);
        when(mockLog.getTransactionHash()).thenReturn(TX_HASH);
        when(mockLog.getBlockNumber()).thenReturn(BLOCK_NUMBER);
        when(mockLog.getBlockHash()).thenReturn(BLOCK_HASH);
        when(mockEthBlock.getBlock()).thenReturn(mockBlock);
        when(mockBlock.getTimestamp()).thenReturn(BLOCK_TIMESTAMP);
        when(mockTransactionReceipt.getFrom()).thenReturn(FROM_ADDRESS);

        filter = new ContractEventFilter();
        filter.setContractAddress(CONTRACT_ADDRESS);
        filter.setEventSpecification(eventSpec);
    }

    @Test
    public void testValuesCorrect() {
        DefaultContractEventDetailsFactory underTest = createFactory(BigInteger.TEN);

        final ContractEventDetails eventDetails = underTest.createEventDetails(filter, mockLog, mockEthBlock, mockTransactionReceipt);

        assertEquals(eventDetails.getName(), eventSpec.getEventName());
        assertEquals(filter.getId(), eventDetails.getFilterId());
        assertEquals(Keys.toChecksumAddress(ADDRESS), eventDetails.getAddress());
        assertEquals(LOG_INDEX, eventDetails.getLogIndex());
        assertEquals(TX_HASH, eventDetails.getTransactionHash());
        assertEquals(BLOCK_NUMBER, eventDetails.getBlockNumber());
        assertEquals(BLOCK_HASH, eventDetails.getBlockHash());
        assertEquals(Web3jUtil.getSignature(eventSpec), eventDetails.getEventSpecificationSignature());
        Assert.assertEquals(ContractEventStatus.UNCONFIRMED, eventDetails.getStatus());
        assertEquals(NETWORK_NAME,eventDetails.getNetworkName());
        assertEquals(BLOCK_TIMESTAMP, eventDetails.getBlockTimestamp());
        assertEquals(FROM_ADDRESS, eventDetails.getFrom());
    }

    @Test
    public void testStatusWhenLogRemoved() {
        when(mockLog.isRemoved()).thenReturn(true);

        DefaultContractEventDetailsFactory underTest = createFactory(BigInteger.TEN);

        final ContractEventDetails eventDetails = underTest.createEventDetails(filter, mockLog, mockEthBlock, mockTransactionReceipt);

        assertEquals(ContractEventStatus.INVALIDATED, eventDetails.getStatus());
    }

    @Test
    public void testStatusWhenZeroConfirmationsConfigured() {
        DefaultContractEventDetailsFactory underTest = createFactory(BigInteger.ZERO);

        final ContractEventDetails eventDetails = underTest.createEventDetails(filter, mockLog, mockEthBlock, mockTransactionReceipt);

        assertEquals(ContractEventStatus.CONFIRMED, eventDetails.getStatus());
    }

    @Test
    public void testIndexedParametersAreCorrect() {
        final DefaultContractEventDetailsFactory underTest = createFactory(BigInteger.TEN);

        final EventParameter mockParam1 = mock(EventParameter.class);
        final ArgumentCaptor<Type> argumentCaptor = ArgumentCaptor.forClass(Type.class);
        when(mockParameterCoverter.convert(argumentCaptor.capture())).thenReturn(mockParam1);

        final ContractEventDetails eventDetails = underTest.createEventDetails(filter, mockLog, mockEthBlock, mockTransactionReceipt);

        assertEquals(Arrays.asList(mockParam1), eventDetails.getIndexedParameters());
        assertEquals(BigInteger.valueOf(456), argumentCaptor.getAllValues().get(3).getValue());
    }

    @Test
    public void testNonIndexedParametersAreCorrect() {

        final DefaultContractEventDetailsFactory underTest = createFactory(BigInteger.TEN);

        final EventParameter mockParam1 = mock(EventParameter.class);
        final ArgumentCaptor<Type> argumentCaptor = ArgumentCaptor.forClass(Type.class);
        when(mockParameterCoverter.convert(argumentCaptor.capture())).thenReturn(mockParam1);

        final ContractEventDetails eventDetails = underTest.createEventDetails(filter, mockLog, mockEthBlock, mockTransactionReceipt);

        assertEquals(Arrays.asList(mockParam1, mockParam1, mockParam1), eventDetails.getNonIndexedParameters());
        assertEquals(BigInteger.valueOf(123), argumentCaptor.getAllValues().get(0).getValue());
        assertEquals("0x00a329c0648769a73afac7f9381e08fb43dbea72",
                argumentCaptor.getAllValues().get(1).toString());
        assertEquals(BigInteger.valueOf(-42), argumentCaptor.getAllValues().get(2).getValue());
    }

    private DefaultContractEventDetailsFactory createFactory(BigInteger confirmations) {
        Node node =
                new Node();
        node.setBlocksToWaitForConfirmation(confirmations);
        node.setBlocksToWaitForMissingTx(BigInteger.valueOf(100));
        node.setBlocksToWaitBeforeInvalidating(BigInteger.valueOf(5));
        return new DefaultContractEventDetailsFactory(mockParameterCoverter, node, NETWORK_NAME);
    }
}
