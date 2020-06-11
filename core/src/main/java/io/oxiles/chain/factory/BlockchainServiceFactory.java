package io.oxiles.chain.factory;

import io.oxiles.chain.service.BlockchainService;
import io.oxiles.chain.settings.Node;

public interface BlockchainServiceFactory {

    BlockchainService create(Node node);
}
