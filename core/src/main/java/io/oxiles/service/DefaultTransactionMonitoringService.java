package io.oxiles.service;

import io.oxiles.chain.block.tx.criteria.TransactionMatchingCriteria;
import io.oxiles.chain.factory.TransactionDetailsFactory;
import io.oxiles.chain.service.container.HCSNodeServices;
import io.oxiles.chain.service.container.NodeServices;
import io.oxiles.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import io.oxiles.model.TransactionIdentifierType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import io.oxiles.chain.block.tx.TransactionMonitoringBlockListener;
import io.oxiles.chain.block.tx.criteria.factory.TransactionMatchingCriteriaFactory;
import io.oxiles.chain.service.BlockCache;
import io.oxiles.chain.service.container.ChainServicesContainer;
import io.oxiles.chain.service.container.HashgraphNodeServices;
import io.oxiles.chain.settings.NodeType;
import io.oxiles.integration.broadcast.internal.EventeumEventBroadcaster;
import io.oxiles.model.TransactionMonitoringSpec;
import io.oxiles.repository.TransactionMonitoringSpecRepository;
import io.oxiles.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class DefaultTransactionMonitoringService implements TransactionMonitoringService {

    private ChainServicesContainer chainServices;

    private BlockchainEventBroadcaster broadcaster;

    private EventeumEventBroadcaster eventeumEventBroadcaster;

    private TransactionDetailsFactory transactionDetailsFactory;

    private TransactionMonitoringSpecRepository transactionMonitoringRepo;

    private TransactionMonitoringBlockListener monitoringBlockListener;

    private TransactionMatchingCriteriaFactory matchingCriteriaFactory;

    private BlockCache blockCache;

    @Getter
    @Setter
    private Map<String, TransactionMonitor> transactionMonitors = new HashMap<>();

    @Autowired
    public DefaultTransactionMonitoringService(ChainServicesContainer chainServices,
                                               BlockchainEventBroadcaster broadcaster,
                                               EventeumEventBroadcaster eventeumEventBroadcaster,
                                               TransactionDetailsFactory transactionDetailsFactory,
                                               TransactionMonitoringSpecRepository transactionMonitoringRepo,
                                               TransactionMonitoringBlockListener monitoringBlockListener,
                                               TransactionMatchingCriteriaFactory matchingCriteriaFactory,
                                               BlockCache blockCache) {
        this.chainServices = chainServices;
        this.broadcaster = broadcaster;
        this.eventeumEventBroadcaster = eventeumEventBroadcaster;
        this.transactionDetailsFactory = transactionDetailsFactory;
        this.transactionMonitoringRepo = transactionMonitoringRepo;
        this.monitoringBlockListener = monitoringBlockListener;
        this.matchingCriteriaFactory = matchingCriteriaFactory;
        this.blockCache = blockCache;
    }

    @Override
    public void registerTransactionsToMonitor(TransactionMonitoringSpec spec) {
        registerTransactionsToMonitor(spec, true);
    }

    @Override
    public void registerTransactionsToMonitor(TransactionMonitoringSpec spec, boolean broadcast) {
        if (isTransactionSpecRegistered(spec)) {
            log.info("Already registered transaction monitoring spec with id: " + spec.getId());
            return;
        }

        registerTransactionMonitoring(spec);
        saveTransactionMonitoringSpec(spec);

        if (broadcast) {
            eventeumEventBroadcaster.broadcastTransactionMonitorAdded(spec);
        }
    }

    @Override
    public void stopMonitoringTransactions(String monitorId) throws NotFoundException {
        stopMonitoringTransactions(monitorId, true);
    }

    @Override
    public void stopMonitoringTransactions(String monitorId, boolean broadcast) throws NotFoundException {

        final TransactionMonitor transactionMonitor = getTransactionMonitor(monitorId);

        if (transactionMonitor == null) {
            throw new NotFoundException("No monitored transaction with id: " + monitorId);
        }

        removeTransactionMonitorMatchinCriteria(transactionMonitor);
        deleteTransactionMonitor(monitorId);

        if (broadcast) {
            eventeumEventBroadcaster.broadcastTransactionMonitorRemoved(transactionMonitor.getSpec());
        }
    }

    private void removeTransactionMonitorMatchinCriteria(TransactionMonitor transactionMonitor) {
        monitoringBlockListener.removeMatchingCriteria(transactionMonitor.getMatchingCriteria());
    }

    private void deleteTransactionMonitor(String monitorId) {
        transactionMonitors.remove(monitorId);

        transactionMonitoringRepo.deleteById(monitorId);
    }

    private TransactionMonitor getTransactionMonitor(String monitorId) {
        return transactionMonitors.get(monitorId);
    }

    private void registerTransactionMonitoring(TransactionMonitoringSpec spec) {

        final TransactionMatchingCriteria matchingCriteria = matchingCriteriaFactory.build(spec);

        NodeServices nodeServices = chainServices.getNodeServices(spec.getNodeName());

        if (nodeServices == null) {
            log.warn("The defined transaction monitor ".concat(spec.getId()).concat(" does not had valid node service"));
        } else {
            if (nodeServices.getNodeType().equals(NodeType.NORMAL)) {
                monitoringBlockListener.addMatchingCriteria(matchingCriteria);
            } else if (nodeServices.getNodeType().equals(NodeType.KABUTO)) {
                ((HashgraphNodeServices) nodeServices).getKabutoSDK().transactionFlowable(spec.getTransactionIdentifierValue());
            } else if (nodeServices.getNodeType().equals(NodeType.DRAGONGLASS)) {
                if(spec.getType().equals(TransactionIdentifierType.FROM_ADDRESS)) {
                    ((HashgraphNodeServices) nodeServices).getDragonGlassSDK().transactionFlowable(spec.getTransactionIdentifierValue());
                }
                if(spec.getType().equals(TransactionIdentifierType.FROM_TOKEN)) {
                    ((HashgraphNodeServices) nodeServices).getDragonGlassSDK().tokenTransferFlowable(spec.getTransactionIdentifierValue());
                }

            } else if(nodeServices.getNodeType().equals(NodeType.MIRROR)){
                ((HCSNodeServices)nodeServices).getHcsService().subscribeToTopic(spec.getTransactionIdentifierValue());
            }

            transactionMonitors.put(spec.getId(), new TransactionMonitor(spec, matchingCriteria));
        }
    }

    private TransactionMonitoringSpec saveTransactionMonitoringSpec(TransactionMonitoringSpec spec) {
        return transactionMonitoringRepo.save(spec);
    }

    private boolean isTransactionSpecRegistered(TransactionMonitoringSpec spec) {
        return transactionMonitors.containsKey(spec.getId());
    }

    @Data
    private class TransactionMonitor {
        TransactionMonitoringSpec spec;

        TransactionMatchingCriteria matchingCriteria;

        public TransactionMonitor(TransactionMonitoringSpec spec, TransactionMatchingCriteria matchingCriteria) {
            this.spec = spec;
            this.matchingCriteria = matchingCriteria;
        }

    }
}
