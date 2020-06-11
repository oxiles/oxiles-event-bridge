package io.oxiles.integration.eventstore.db.repository;

import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.factory.EventStoreFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("contractEventDetailRepository")
@ConditionalOnProperty(name = "eventStore.type", havingValue = "DB")
@ConditionalOnMissingBean(EventStoreFactory.class)
public interface ContractEventDetailsRepository extends CrudRepository<ContractEventDetails, String> {

	Page<ContractEventDetails> findByEventSpecificationSignatureAndAddress(
			String eventSpecificationSignature, String address, Pageable pageable);
}
