package io.oxiles.chain.service;

import io.oxiles.chain.service.container.ChainServicesContainer;
import io.oxiles.chain.settings.NodeSettings;
import io.oxiles.chain.util.Web3jUtil;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.event.filter.ContractEventFilter;
import io.oxiles.service.EventStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default implementation of an EventBlockManagementService, which "Manages the latest block
 * that has been seen to a specific event specification."
 *
 * This implementation stores the latest blocks for each event filter in memory, but delegates to
 * the event store if an entry is not found in memory.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
@Slf4j
@Component
public class DefaultEventBlockManagementService implements EventBlockManagementService {

    private AbstractMap<String, AbstractMap> latestBlocks = new ConcurrentHashMap<>();

    private ChainServicesContainer chainServicesContainer;

    private EventStoreService eventStoreService;

    private NodeSettings nodeSettings;

    @Autowired
    public DefaultEventBlockManagementService(@Lazy ChainServicesContainer chainServicesContainer,
                                              EventStoreService eventStoreService,
                                              NodeSettings nodeSettings) {
        this.chainServicesContainer = chainServicesContainer;
        this.eventStoreService = eventStoreService;
        this.nodeSettings = nodeSettings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLatestBlock(String eventSpecHash, BigInteger blockNumber, String address) {
        AbstractMap<String, BigInteger> events = latestBlocks.get(address);

        if (events == null) {
            events = new ConcurrentHashMap<>();
            latestBlocks.put(address, events);
        }

        final BigInteger currentLatest = events.get(eventSpecHash);


        if (currentLatest == null || blockNumber.compareTo(currentLatest) > 0) {
            events.put(eventSpecHash, blockNumber);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger getLatestBlockForEvent(ContractEventFilter eventFilter) {
        final BigInteger currentBlockNumber = chainServicesContainer.getNodeServices(eventFilter.getNode()).getBlockchainService().getCurrentBlockNumber();
        final BigInteger maxUnsyncedBlocksForFilter = nodeSettings.getNode(eventFilter.getNode()).getMaxUnsyncedBlocksForFilter();
        final String eventSignature = Web3jUtil.getSignature(eventFilter.getEventSpecification());
        final AbstractMap<String, BigInteger> events = latestBlocks.get(eventFilter.getContractAddress());

        if (events != null) {
            final BigInteger latestBlockNumber = events.get(eventSignature);

            if (latestBlockNumber != null) {

                BigInteger cappedBlockNumber = BigInteger.valueOf(0);
                if(currentBlockNumber.subtract(latestBlockNumber).compareTo(maxUnsyncedBlocksForFilter) == 1){
                    cappedBlockNumber = currentBlockNumber.subtract(maxUnsyncedBlocksForFilter);
                    log.info("{} :Max Unsynced Blocks gap reached ´{} to {} . Applied {}. Max {}",eventFilter.getId(),latestBlockNumber,cappedBlockNumber,maxUnsyncedBlocksForFilter);
                    return cappedBlockNumber;
                }
                else {
                    log.debug("Block number for event> {} found in memory, starting at blockNumber: {}", eventFilter.getId(), latestBlockNumber.add(BigInteger.ONE));

                    return latestBlockNumber.add(BigInteger.ONE);
                }
            }
        }

        final Optional<ContractEventDetails> contractEvent =
                eventStoreService.getLatestContractEvent(eventSignature, eventFilter.getContractAddress());

        if (contractEvent.isPresent()) {
            BigInteger blockNumber = contractEvent.get().getBlockNumber();

            BigInteger limitedBlockNumber = BigInteger.valueOf(0);
            if(currentBlockNumber.subtract(blockNumber).compareTo(maxUnsyncedBlocksForFilter) == 1){
                limitedBlockNumber = currentBlockNumber.subtract(maxUnsyncedBlocksForFilter);
                log.info("{} :Max Unsynced Blocks gap reached ´{} to {} . Applied {}. Max {}",eventFilter.getId(),blockNumber,limitedBlockNumber,maxUnsyncedBlocksForFilter);
                return limitedBlockNumber;
            }else {

                log.debug("Block number for event {} found in the database, starting at blockNumber: {}", eventFilter.getId(), blockNumber.add(BigInteger.ONE));

                return blockNumber.add(BigInteger.ONE);
            }
        }

        BigInteger syncStartBlock = nodeSettings.getNode(eventFilter.getNode()).getSyncStartBlock();

        if( syncStartBlock != BigInteger.valueOf(Long.valueOf(NodeSettings.DEFAULT_SYNC_START_BLOCK))) {

            log.debug("Block number for event {}, starting at blockNumber configured with the node special startBlockNumber: {}", eventFilter.getId(), syncStartBlock);
            return  syncStartBlock;
        }
        else
        {
            if (eventFilter.getStartBlock() != null) {

                BigInteger blockNumber = eventFilter.getStartBlock();

                log.debug("Block number for event {}, starting at blockNumber configured for the event: {}", eventFilter.getId(), blockNumber);

                return blockNumber;
            }

            final BlockchainService blockchainService =
                    chainServicesContainer.getNodeServices(eventFilter.getNode()).getBlockchainService();

            BigInteger blockNumber = blockchainService.getCurrentBlockNumber();

            log.debug("Block number for event {} not found in memory or database, starting at blockNumber: {}", eventFilter.getId(), blockNumber);

            return blockNumber;
        }
    }
}
