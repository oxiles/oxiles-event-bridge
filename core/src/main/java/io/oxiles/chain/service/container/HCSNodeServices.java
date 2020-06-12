package io.oxiles.chain.service.container;

import lombok.Data;
import io.oxiles.chain.service.BlockchainService;
import io.oxiles.chain.service.HCSService;

@Data
public class HCSNodeServices extends AbstractNodeServices {

    private HCSService hcsService;

    @Override
    public BlockchainService getBlockchainService() {
        return null;
    }
}
