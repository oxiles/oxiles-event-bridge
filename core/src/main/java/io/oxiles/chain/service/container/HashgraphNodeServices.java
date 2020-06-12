package io.oxiles.chain.service.container;

import io.oxiles.chain.service.strategy.DragonGlassSDK;
import lombok.Data;
import io.oxiles.chain.service.BlockchainService;
import io.oxiles.chain.service.strategy.KabutoSDK;

@Data
public class HashgraphNodeServices extends AbstractNodeServices {
    KabutoSDK kabutoSDK;
    DragonGlassSDK dragonGlassSDK;

    @Override
    public BlockchainService getBlockchainService() {
        return null;
    }
}
