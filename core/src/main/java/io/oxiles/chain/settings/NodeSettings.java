package io.oxiles.chain.settings;

import java.math.BigInteger;
import java.util.HashMap;

import lombok.Data;
import io.oxiles.chain.service.BlockchainException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Data
@Component
public class NodeSettings {

    private static final Long DEFAULT_POLLING_INTERVAL = 10000l;

    private static final Long DEFAULT_HEALTHCHECK_POLLING_INTERVAL = 10000l;

    private static final Long DEFAULT_KEEP_ALIVE_DURATION = 10000l;

    private static final Integer DEFAULT_MAX_IDLE_CONNECTIONS = 5;

    private static final Long DEFAULT_CONNECTION_TIMEOUT = 5000l;

    private static final Integer DEFAULT_SYNCING_THRESHOLD = 60;

    private static final Long DEFAULT_READ_TIMEOUT = 60000l;

    private static final String DEFAULT_BLOCKS_TO_WAIT_FOR_MISSING_TX = "200";

    private static final String DEFAULT_BLOCKS_TO_WAIT_BEFORE_INVALIDATING = "2";

    private static final String DEFAULT_BLOCKS_TO_WAIT_FOR_CONFIRMATION = "12";

    private static final BigInteger DEFAULT_MAX_UNSYNCED_BLOCKS_FOR_FILTER = BigInteger.valueOf(20000);

    public static final String DEFAULT_SYNC_START_BLOCK = "-1";

    private static final String ATTRIBUTE_PREFIX = "ethereum";

    private static final String NODE_ATTRIBUTE_PREFIX = ".nodes[%s]";

    private static final String NODE_URL_ATTRIBUTE = "url";

    private static final String NODE_TYPE_ATTRIBUTE = "type";

    private static final String NODE_NAME_ATTRIBUTE = "name";

    private static final String ETHEREUM_CHAIN_NAME_ATTRIBUTE = "ethereum";

    private static final String HASHGRAPH_CHAIN_NAME_ATTRIBUTE = "hashgraph";

    private static final String NODE_USERNAME_ATTRIBUTE = "username";

    private static final String NODE_PASSWORD_ATTRIBUTE = "password";

    private static final String NODE_POLLING_INTERVAL_ATTRIBUTE = "pollingInterval";

    private static final String BLOCK_STRATEGY_ATTRIBUTE = "blockStrategy";

    private static final String TRANSACTION_REVERT_REASON_ATTRIBUTTE = "addTransactionRevertReason";

    private static final String MAX_IDLE_CONNECTIONS_ATTRIBUTTE = "maxIdleConnections";

    private static final String KEEP_ALIVE_DURATION_ATTRIBUTTE = "keepAliveDuration";

    private static final String READ_TIMEOUT_ATTRIBUTTE = "readTimeout";

    private static final String CONNECTION_TIMEOUT_ATTRIBUTE = "connectionTimeout";

    private static final String SYNCING_THRESHOLD_ATTRIBUTE = "syncingThreshold";

    private static final String NODE_HEALTHCHECK_INTERVAL_ATTRIBUTE = "healthcheckInterval";

    private static final String BLOCKS_TO_WAIT_FOR_CONFIRMATION_ATTRIBUTE = "numBlocksToWait";

    private static final String GLOBAL_BLOCKS_TO_WAIT_FOR_CONFIRMATION_ATTRIBUTE = "broadcaster.event.confirmation.numBlocksToWait";

    private static final String BLOCKS_TO_WAIT_BEFORE_INVALIDATING_ATTRIBUTE = "numBlocksToWaitBeforeInvalidating";

    private static final String GLOBAL_BLOCKS_TO_WAIT_BEFORE_INVALIDATING_ATTIBUTE = "broadcaster.event.confirmation.numBlocksToWaitBeforeInvalidating";

    private static final String BLOCKS_TO_WAIT_FOR_MISSING_TX_ATTRIBUTE = "numBlocksToWaitForMissingTx";

    private static final String GLOBAL_BLOCKS_TO_WAIT_FOR_MISSING_TX_ATTRIBUTE = "broadcaster.event.confirmation.numBlocksToWaitForMissingTx";

    private static final String NODE_MAX_UNSYNCED_BLOCKS_FOR_FILTER_ATTRIBUTE = "maxUnsyncedBlocksForFilter";

    private static final String DEFAULT_MAX_UNSYNCED_BLOCKS_FOR_FILTER_ATTRIBUTE = "ethereum.maxUnsyncedBlocksForFilter";

    private static final String NODE_SYNC_START_BLOCK_ATTRIBUTE = "syncStartBlock";

    private static final String[] SUPPORTED_CHAINS = new String[]{ETHEREUM_CHAIN_NAME_ATTRIBUTE, HASHGRAPH_CHAIN_NAME_ATTRIBUTE};

    private HashMap<String, Node> nodes;

    private String blockStrategy;

    public NodeSettings(Environment environment) {
        populateNodeSettings(environment);

        blockStrategy = environment.getProperty(ATTRIBUTE_PREFIX + "." + BLOCK_STRATEGY_ATTRIBUTE);
    }

    public Node getNode(String nodeName) {
        return nodes.get(nodeName);
    }

    private void populateNodeSettings(Environment environment) {
        nodes = new HashMap<String, Node>();
        int nodeIndex = 0;

        for (String supportedChain : SUPPORTED_CHAINS) {
            nodeIndex = 0;

            while (nodeExistsAtIndex(environment, supportedChain, nodeIndex)) {

                String nodeName = getNodeNameProperty(environment, supportedChain, nodeIndex);
                Node node = new Node(
                        nodeName,
                        getNodeTypeProperty(environment, supportedChain, nodeIndex),
                        ChainType.valueOf(supportedChain.toUpperCase()),
                        getNodeUrlProperty(environment, supportedChain, nodeIndex),
                        getNodePollingIntervalProperty(environment, supportedChain, nodeIndex),
                        getNodeUsernameProperty(environment, supportedChain, nodeIndex),
                        getNodePasswordProperty(environment, supportedChain, nodeIndex),
                        getNodeBlockStrategyProperty(environment, supportedChain, nodeIndex),
                        getNodeTransactionRevertReasonProperty(environment, supportedChain, nodeIndex),
                        getMaxIdleConnectionsProperty(environment, supportedChain, nodeIndex),
                        getKeepAliveDurationProperty(environment, supportedChain, nodeIndex),
                        getConnectionTimeoutProperty(environment, supportedChain, nodeIndex),
                        getReadTimeoutProperty(environment, supportedChain, nodeIndex),
                        getSyncingThresholdProperty(environment, supportedChain, nodeIndex),
                        getNodeHealthcheckIntervalProperty(environment, supportedChain, nodeIndex),
                        getBlocksToWaitForConfirmationProperty(environment, supportedChain, nodeIndex),
                        getBlocksToWaitBeforeInvalidatingProperty(environment, supportedChain, nodeIndex),
                        getBlocksToWaitForMissingTxProperty(environment, supportedChain, nodeIndex),
                        getMaxUnsyncedBlocksForFilter(environment, supportedChain, nodeIndex),
                        getSyncStartBlock(environment, supportedChain, nodeIndex)
                );

                nodes.put(nodeName, node);

                nodeIndex++;
            }
        }

        if (nodes.isEmpty()) {
            throw new BlockchainException("No nodes configured!");
        }
    }

    private boolean nodeExistsAtIndex(Environment environment, String chainName, int index) {
        return environment.containsProperty(buildNodeAttribute(NODE_NAME_ATTRIBUTE, chainName, index));
    }

    private String getNodeNameProperty(Environment environment, String chainName, int index) {

        return getProperty(environment, buildNodeAttribute(NODE_NAME_ATTRIBUTE, chainName, index));
    }

    private String getNodeUrlProperty(Environment environment, String chainName, int index) {
        return getProperty(environment, buildNodeAttribute(NODE_URL_ATTRIBUTE, chainName, index));
    }


    private NodeType getNodeTypeProperty(Environment environment, String chainName, int index) {
        String nodeType = getProperty(environment, buildNodeAttribute(NODE_TYPE_ATTRIBUTE, chainName, index));

        if (nodeType == null)
        {
            nodeType = NodeType.NORMAL.getNodeType();
        }
        if (chainName.equals(ETHEREUM_CHAIN_NAME_ATTRIBUTE)) {

            if (!nodeType.equals(NodeType.NORMAL.getNodeType())) {
                throw new BlockchainException("Ethereum only supports normal nodes!");
            }
        }
        return NodeType.valueOf(nodeType);
    }

    private Long getNodePollingIntervalProperty(Environment environment, String chainName, int index) {
        final String pollingInterval =
                getProperty(environment, buildNodeAttribute(NODE_POLLING_INTERVAL_ATTRIBUTE, chainName, index));

        if (pollingInterval == null) {
            return DEFAULT_POLLING_INTERVAL;
        }

        return Long.valueOf(pollingInterval);
    }

    private Integer getMaxIdleConnectionsProperty(Environment environment, String chainName, int index) {
        final String maxIdleConnections =
                getProperty(environment, buildNodeAttribute(MAX_IDLE_CONNECTIONS_ATTRIBUTTE, chainName, index));

        if (maxIdleConnections == null) {
            return DEFAULT_MAX_IDLE_CONNECTIONS;
        }

        return Integer.valueOf(maxIdleConnections);
    }

    private Long getKeepAliveDurationProperty(Environment environment, String chainName, int index) {
        final String keepAliveDuration =
                getProperty(environment, buildNodeAttribute(KEEP_ALIVE_DURATION_ATTRIBUTTE, chainName, index));

        if (keepAliveDuration == null) {
            return DEFAULT_KEEP_ALIVE_DURATION;
        }

        return Long.valueOf(keepAliveDuration);
    }

    private Long getConnectionTimeoutProperty(Environment environment, String chainName, int index) {
        final String connectionTimeout =
                getProperty(environment, buildNodeAttribute(CONNECTION_TIMEOUT_ATTRIBUTE, chainName, index));

        if (connectionTimeout == null) {
            return DEFAULT_CONNECTION_TIMEOUT;
        }

        return Long.valueOf(connectionTimeout);
    }

    private Long getReadTimeoutProperty(Environment environment, String chainName, int index) {
        final String readTimeout =
                getProperty(environment, buildNodeAttribute(READ_TIMEOUT_ATTRIBUTTE, chainName, index));

        if (readTimeout == null) {
            return DEFAULT_READ_TIMEOUT;
        }

        return Long.valueOf(readTimeout);
    }

    private Integer getSyncingThresholdProperty(Environment environment, String chainName, int index) {
        final String syncingThreshold =
                getProperty(environment, buildNodeAttribute(SYNCING_THRESHOLD_ATTRIBUTE, chainName, index));

        if (syncingThreshold == null) {
            return DEFAULT_SYNCING_THRESHOLD;
        }

        return Integer.valueOf(syncingThreshold);
    }

    private BigInteger getBlocksToWaitForConfirmationProperty(Environment environment, String chainName, int index) {
        String blocksToWaitForConfirmation =
                getProperty(environment, buildNodeAttribute(BLOCKS_TO_WAIT_FOR_CONFIRMATION_ATTRIBUTE, chainName, index));

        if (blocksToWaitForConfirmation == null) {
            blocksToWaitForConfirmation = getProperty(environment,
                    GLOBAL_BLOCKS_TO_WAIT_FOR_CONFIRMATION_ATTRIBUTE, DEFAULT_BLOCKS_TO_WAIT_FOR_CONFIRMATION);
        }

        return BigInteger.valueOf(Long.valueOf(blocksToWaitForConfirmation));
    }

    private BigInteger getBlocksToWaitBeforeInvalidatingProperty(Environment environment, String chainName, int index) {
        String blocksToWaitBeforeInvalidating =
                getProperty(environment, buildNodeAttribute(BLOCKS_TO_WAIT_BEFORE_INVALIDATING_ATTRIBUTE, chainName, index));

        if (blocksToWaitBeforeInvalidating == null) {
            blocksToWaitBeforeInvalidating = getProperty(environment,
                    GLOBAL_BLOCKS_TO_WAIT_BEFORE_INVALIDATING_ATTIBUTE, DEFAULT_BLOCKS_TO_WAIT_BEFORE_INVALIDATING);
        }

        return BigInteger.valueOf(Long.valueOf(blocksToWaitBeforeInvalidating));
    }

    private BigInteger getBlocksToWaitForMissingTxProperty(Environment environment, String chainName, int index) {
        String blocksToWaitForMissingTx =
                getProperty(environment, buildNodeAttribute(BLOCKS_TO_WAIT_FOR_MISSING_TX_ATTRIBUTE, chainName, index));

        if (blocksToWaitForMissingTx == null) {
            blocksToWaitForMissingTx = getProperty(environment,
                    GLOBAL_BLOCKS_TO_WAIT_FOR_MISSING_TX_ATTRIBUTE, DEFAULT_BLOCKS_TO_WAIT_FOR_MISSING_TX);
        }

        return BigInteger.valueOf(Long.valueOf(blocksToWaitForMissingTx));
    }


    private String getNodeUsernameProperty(Environment environment, String chainName, int index) {
        return getProperty(environment, buildNodeAttribute(NODE_USERNAME_ATTRIBUTE, chainName, index));
    }

    private String getNodePasswordProperty(Environment environment, String chainName, int index) {
        return getProperty(environment, buildNodeAttribute(NODE_PASSWORD_ATTRIBUTE, chainName, index));
    }

    private String getNodeBlockStrategyProperty(Environment environment, String chainName, int index) {
        return getProperty(environment, buildNodeAttribute(BLOCK_STRATEGY_ATTRIBUTE, chainName, index));
    }

    private Boolean getNodeTransactionRevertReasonProperty(Environment environment, String chainName, int index) {
        return Boolean.parseBoolean(getProperty(environment, buildNodeAttribute(TRANSACTION_REVERT_REASON_ATTRIBUTTE, chainName, index)));
    }

    private Long getNodeHealthcheckIntervalProperty(Environment environment, String chainName, int index) {
        String healthcheckInterval =
                getProperty(environment, buildNodeAttribute(NODE_HEALTHCHECK_INTERVAL_ATTRIBUTE, chainName, index));

        if (healthcheckInterval == null) {
            // Get the generic configuration
            healthcheckInterval = environment.getProperty("ethereum.healthcheck.pollInterval");
        }

        if (healthcheckInterval == null) {
            // Get the default configuration
            return DEFAULT_HEALTHCHECK_POLLING_INTERVAL;
        }

        return Long.valueOf(healthcheckInterval);
    }

    private BigInteger getMaxUnsyncedBlocksForFilter(Environment environment, String chainName, int index) {
        String maxUnsyncedBlocksForFilter =
                getProperty(environment, buildNodeAttribute(NODE_MAX_UNSYNCED_BLOCKS_FOR_FILTER_ATTRIBUTE, chainName, index));

        if (maxUnsyncedBlocksForFilter == null) {
            // Get the generic configuration
            maxUnsyncedBlocksForFilter = environment.getProperty(DEFAULT_MAX_UNSYNCED_BLOCKS_FOR_FILTER_ATTRIBUTE);
        }

        if (maxUnsyncedBlocksForFilter == null) {
            // Get the default configuration
            return DEFAULT_MAX_UNSYNCED_BLOCKS_FOR_FILTER;
        }

        return BigInteger.valueOf(Long.valueOf(maxUnsyncedBlocksForFilter));
    }

    private BigInteger getSyncStartBlock(Environment environment, String chainName, int index) {
        String syncStartBlock =
                getProperty(environment, buildNodeAttribute(NODE_SYNC_START_BLOCK_ATTRIBUTE, chainName, index));

        if (syncStartBlock == null) {
            // Use -1 as non configures
            syncStartBlock = DEFAULT_SYNC_START_BLOCK;
        }

        return BigInteger.valueOf(Long.valueOf(syncStartBlock));
    }


    private String getProperty(Environment environment, String property) {
        return environment.getProperty(property);
    }

    private String getProperty(Environment environment, String property, String defaultValue) {
        return environment.getProperty(property, defaultValue);
    }

    private String buildNodeAttribute(String attribute, String chainName, int index) {
        return new StringBuilder(String.format(chainName + NODE_ATTRIBUTE_PREFIX, index))
                .append(".")
                .append(attribute)
                .toString();
    }
}
