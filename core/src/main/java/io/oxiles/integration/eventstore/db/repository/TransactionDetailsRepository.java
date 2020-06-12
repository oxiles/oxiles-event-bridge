package io.oxiles.integration.eventstore.db.repository;

import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.factory.EventStoreFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("transactionsDetailRepository")
@ConditionalOnProperty(name = "eventStore.type", havingValue = "DB")
@ConditionalOnMissingBean(EventStoreFactory.class)
public interface TransactionDetailsRepository extends CrudRepository<HashGraphTransactionData, String> {
    boolean existsByHashAndNodeType(String hash, String nodeType);
}
