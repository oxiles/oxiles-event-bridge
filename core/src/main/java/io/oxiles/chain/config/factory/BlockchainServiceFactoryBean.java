package io.oxiles.chain.config.factory;

import io.oxiles.chain.factory.ContractEventDetailsFactory;
import io.oxiles.chain.service.BlockchainService;
import io.oxiles.chain.service.Web3jService;
import io.oxiles.chain.service.strategy.BlockSubscriptionStrategy;
import io.oxiles.chain.settings.Node;
import io.oxiles.service.AsyncTaskService;
import lombok.Data;
import io.oxiles.chain.service.EventBlockManagementService;
import org.springframework.beans.factory.FactoryBean;
import org.web3j.protocol.Web3j;

@Data
public class BlockchainServiceFactoryBean implements FactoryBean<BlockchainService> {

    private Node node;
    private Web3j web3j;
    private ContractEventDetailsFactory contractEventDetailsFactory;
    private EventBlockManagementService eventBlockManagementService;
    private BlockSubscriptionStrategy blockSubscriptionStrategy;
    private AsyncTaskService asyncTaskService;

    @Override
    public BlockchainService getObject() throws Exception {
        return new Web3jService(node.getName(), web3j,
                contractEventDetailsFactory, eventBlockManagementService, blockSubscriptionStrategy, asyncTaskService);
    }

    @Override
    public Class<?> getObjectType() {
        return BlockchainService.class;
    }
}
