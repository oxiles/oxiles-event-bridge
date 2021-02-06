package io.oxiles.integration.eventstore.db.repository;

import io.oxiles.chain.service.strategy.HashGraphTokenTransferData;
import io.oxiles.factory.EventStoreFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("tokenTransferRepository")
@ConditionalOnProperty(name = "eventStore.type", havingValue = "DB")
@ConditionalOnMissingBean(EventStoreFactory.class)
public interface TokenTransferRepository extends CrudRepository<HashGraphTokenTransferData, String> {
    boolean existsByTransactionIdAndNodeType(String transactionId, String nodeType);
}