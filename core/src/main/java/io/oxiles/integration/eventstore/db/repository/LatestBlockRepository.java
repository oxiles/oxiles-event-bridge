package io.oxiles.integration.eventstore.db.repository;

import io.oxiles.factory.EventStoreFactory;
import io.oxiles.model.LatestBlock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("latestBlockRepository")
@ConditionalOnProperty(name = "eventStore.type", havingValue = "DB")
@ConditionalOnMissingBean(EventStoreFactory.class)
public interface LatestBlockRepository extends CrudRepository<LatestBlock, String> {
}
