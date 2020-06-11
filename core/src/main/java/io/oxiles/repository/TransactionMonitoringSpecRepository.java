package io.oxiles.repository;

import io.oxiles.factory.ContractEventFilterRepositoryFactory;
import io.oxiles.model.TransactionMonitoringSpec;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring repository for storing active TransactionMonitoringSpec(s) in DB.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
@Repository
@ConditionalOnMissingBean(ContractEventFilterRepositoryFactory.class)
public interface TransactionMonitoringSpecRepository extends CrudRepository<TransactionMonitoringSpec, String> {
}
