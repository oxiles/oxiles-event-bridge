package io.oxiles.chain.service.container;

import io.oxiles.chain.service.BlockchainService;
import io.oxiles.chain.settings.NodeType;

public interface NodeServices {

    String getNodeName();
    BlockchainService getBlockchainService();
    NodeType getNodeType();
}
