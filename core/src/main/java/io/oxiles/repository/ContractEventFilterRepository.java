package io.oxiles.repository;

import io.oxiles.dto.event.filter.ContractEventFilter;
import io.oxiles.factory.ContractEventFilterRepositoryFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring repository for storing active ContractEventFilters in DB.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
@Repository
@ConditionalOnMissingBean(ContractEventFilterRepositoryFactory.class)
public interface ContractEventFilterRepository extends CrudRepository<ContractEventFilter, String> {
}
