package io.oxiles.config;

import io.oxiles.chain.block.BlockListener;
import io.oxiles.chain.block.EventStoreLatestBlockUpdater;
import io.oxiles.chain.factory.BlockDetailsFactory;
import io.oxiles.factory.EventStoreFactory;
import io.oxiles.integration.eventstore.EventStore;
import io.oxiles.integration.eventstore.SaveableEventStore;
import io.oxiles.integration.eventstore.db.MongoEventStore;
import io.oxiles.integration.eventstore.db.SqlEventStore;
import io.oxiles.integration.eventstore.db.repository.*;
import io.oxiles.integration.eventstore.rest.RESTEventStore;
import io.oxiles.integration.eventstore.rest.client.EventStoreClient;
import io.oxiles.monitoring.EventeumValueMonitor;
import io.oxiles.chain.contract.ContractEventListener;
import io.oxiles.chain.contract.EventStoreContractEventUpdater;
import io.oxiles.chain.service.container.ChainServicesContainer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@Order(0)
public class EventStoreConfiguration {

	@Configuration
	@ConditionalOnExpression("'${eventStore.type}:${database.type}'=='DB:MONGO'")
	@ConditionalOnMissingBean(EventStoreFactory.class)
	public static class MongoEventStoreConfiguration {

		@Bean
		public SaveableEventStore dbEventStore(
				ContractEventDetailsRepository contractEventRepository,
				LatestBlockRepository latestBlockRepository,
				TransactionDetailsRepository transactionDetailsRepository,
				HCSMessageTransactionDetailsRepository hcsMessageTransactionDetailsRepository,
				TokenTransferRepository tokenTransferRepository,
				MongoTemplate mongoTemplate) {
			return new MongoEventStore(contractEventRepository, latestBlockRepository, transactionDetailsRepository, hcsMessageTransactionDetailsRepository, tokenTransferRepository, mongoTemplate);
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
			return new EventStoreLatestBlockUpdater(eventStore, blockDetailsFactory, valueMonitor, chainServicesContainer);
		}
	}

	@Configuration
	@ConditionalOnExpression("'${eventStore.type}:${database.type}'=='DB:SQL'")
	@ConditionalOnMissingBean(EventStoreFactory.class)
	public static class SqlEventStoreConfiguration {

		@Bean
		public SaveableEventStore dbEventStore(
				ContractEventDetailsRepository contractEventRepository,
				LatestBlockRepository latestBlockRepository,
				TransactionDetailsRepository transactionDetailsRepository,
				JdbcTemplate jdbcTemplate) {
			return new SqlEventStore(contractEventRepository, latestBlockRepository, transactionDetailsRepository, jdbcTemplate);
		}

		@Bean
		public ContractEventListener eventStoreContractEventUpdater(SaveableEventStore eventStore) {
			return new EventStoreContractEventUpdater(eventStore);
		}

		@Bean
		public BlockListener eventStoreLatestBlockUpdater(SaveableEventStore eventStore,
														  BlockDetailsFactory blockDetailsFactory,
														  EventeumValueMonitor valueMonitor,
														  ChainServicesContainer chainServiceContainer ) {
			return new EventStoreLatestBlockUpdater(eventStore, blockDetailsFactory, valueMonitor,chainServiceContainer);
		}
	}

	@Configuration
	@ConditionalOnProperty(name = "eventStore.type", havingValue = "REST")
	@ConditionalOnMissingBean(EventStoreFactory.class)
	public static class RESTEventStoreConfiguration {

		@Bean
		public EventStore RESTEventStore(EventStoreClient client) {
			return new RESTEventStore(client);
		}
	}




}
