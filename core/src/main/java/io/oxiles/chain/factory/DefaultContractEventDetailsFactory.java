package io.oxiles.chain.factory;

import java.util.Collections;

import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.event.ContractEventStatus;
import io.oxiles.dto.event.parameter.EventParameter;
import io.oxiles.chain.converter.EventParameterConverter;
import io.oxiles.chain.settings.Node;
import io.oxiles.chain.util.Web3jUtil;
import io.oxiles.chain.service.domain.TransactionReceipt;

import io.oxiles.dto.event.filter.ContractEventFilter;
import io.oxiles.dto.event.filter.ContractEventSpecification;
import io.oxiles.dto.event.filter.ParameterDefinition;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Keys;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultContractEventDetailsFactory implements ContractEventDetailsFactory {

    private EventParameterConverter<Type> parameterConverter;
    private Node node;
    private String networkName;

    public DefaultContractEventDetailsFactory(EventParameterConverter<Type> parameterConverter,
                                              Node node,
                                              String networkName) {
        this.parameterConverter = parameterConverter;
        this.node = node;
        this.networkName = networkName;
    }

    @Override
    public ContractEventDetails createEventDetails(ContractEventFilter eventFilter, Log log, EthBlock ethBlock, TransactionReceipt transactionReceipt) {
        final ContractEventSpecification eventSpec = eventFilter.getEventSpecification();

        final List<EventParameter> nonIndexed = typeListToParameterList(getNonIndexedParametersFromLog(eventSpec, log));
        final List<EventParameter> indexed = typeListToParameterList(getIndexedParametersFromLog(eventSpec, log));

        final ContractEventDetails eventDetails = new ContractEventDetails();
        eventDetails.setName(eventSpec.getEventName());
        eventDetails.setFilterId(eventFilter.getId());
        eventDetails.setNonIndexedParameters(nonIndexed);
        eventDetails.setIndexedParameters(indexed);
        eventDetails.setAddress(Keys.toChecksumAddress(log.getAddress()));
        eventDetails.setLogIndex(log.getLogIndex());
        eventDetails.setTransactionHash(log.getTransactionHash());
        eventDetails.setBlockNumber(log.getBlockNumber());
        eventDetails.setBlockHash(log.getBlockHash());
        eventDetails.setEventSpecificationSignature(Web3jUtil.getSignature(eventSpec));
        eventDetails.setNetworkName(this.networkName);
        eventDetails.setNodeName(eventFilter.getNode());

        BigInteger timeStamp = null;

        while (timeStamp == null) {
            try {
                EthBlock.Block block = ethBlock.getBlock();
                timeStamp = block.getTimestamp();
            } catch (Exception ex) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        eventDetails.setBlockTimestamp(timeStamp);
        eventDetails.setFrom(transactionReceipt.getFrom());

        if (log.isRemoved()) {
            eventDetails.setStatus(ContractEventStatus.INVALIDATED);
        } else if (node.getBlocksToWaitForConfirmation().equals(BigInteger.ZERO)) {
            //Set to confirmed straight away if set to zero confirmations
            eventDetails.setStatus(ContractEventStatus.CONFIRMED);
        } else {
            eventDetails.setStatus(ContractEventStatus.UNCONFIRMED);
        }

        return eventDetails;
    }

    private List<EventParameter> typeListToParameterList(List<Type> typeList) {
        if (isNullOrEmpty(typeList)) {
            return Collections.EMPTY_LIST;
        }

        return typeList
                .stream()
                .map(type -> parameterConverter.convert(type))
                .collect(Collectors.toList());
    }

    private List<Type> getNonIndexedParametersFromLog(ContractEventSpecification eventSpec, Log log) {
        if (isNullOrEmpty(eventSpec.getNonIndexedParameterDefinitions())) {
            return Collections.EMPTY_LIST;
        }

        return FunctionReturnDecoder.decode(
                log.getData(),
                Utils.convert(Web3jUtil.getTypeReferencesFromParameterDefinitions(
                        eventSpec.getNonIndexedParameterDefinitions())));
    }

    private List<Type> getIndexedParametersFromLog(ContractEventSpecification eventSpec, Log log) {
        if (isNullOrEmpty(eventSpec.getIndexedParameterDefinitions())) {
            return Collections.EMPTY_LIST;
        }

        final List<String> encodedParameters = log.getTopics().subList(1, log.getTopics().size());
        final List<ParameterDefinition> definitions = eventSpec.getIndexedParameterDefinitions();

        return IntStream.range(0, encodedParameters.size())
                .mapToObj(i -> FunctionReturnDecoder.decodeIndexedValue(encodedParameters.get(i),
                        Web3jUtil.getTypeReferenceFromParameterType(definitions.get(i).getType())))
                .collect(Collectors.toList());
    }

    private boolean isNullOrEmpty(List<?> toCheck) {
        return toCheck == null || toCheck.isEmpty();
    }
}