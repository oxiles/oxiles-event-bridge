package io.oxiles.chain.service.container;

import lombok.Data;
import io.oxiles.chain.service.BlockchainService;
import io.oxiles.chain.service.strategy.KabutoSDK;

@Data
public class HashgraphNodeServices extends AbstractNodeServices {
    KabutoSDK kabutoSDK;

    @Override
    public BlockchainService getBlockchainService() {
        return null;
    }
}
