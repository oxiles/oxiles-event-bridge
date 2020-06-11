package io.oxiles.config;

import io.oxiles.chain.block.BlockListener;
import io.oxiles.chain.block.EventStoreLatestBlockUpdater;
import io.oxiles.chain.factory.BlockDetailsFactory;
import io.oxiles.factory.EventStoreFactory;
import io.oxiles.integration.eventstore.SaveableEventStore;
import io.oxiles.monitoring.EventeumValueMonitor;
import io.oxiles.chain.contract.ContractEventListener;
import io.oxiles.chain.contract.EventStoreContractEventUpdater;
import io.oxiles.chain.service.container.ChainServicesContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
@ConditionalOnBean(EventStoreFactory.class)
public class CustomEventStoreConfiguration {

    @Bean
    public SaveableEventStore customEventStore(EventStoreFactory factory) {
        return factory.build();
    }

    @Bean
    public ContractEventListener eventStoreContractEventUpdater(SaveableEventStore eventStore) {
        return new EventStoreContractEventUpdater(eventStore);
    }

    @Bean
    public BlockListener eventStoreLatestBlockUpdater(SaveableEventStore eventStore,
                                                      BlockDetailsFactory blockDetailsFactory,
                                                      EventeumValueMonitor valueMonitor,
                                                      ChainServicesContainer chainServicesContainer) {
        return new EventStoreLatestBlockUpdater(eventStore, blockDetailsFactory,  valueMonitor, chainServicesContainer);
    }
}
