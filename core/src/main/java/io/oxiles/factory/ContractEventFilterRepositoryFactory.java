package io.oxiles.factory;

import io.oxiles.dto.event.filter.ContractEventFilter;
import org.springframework.data.repository.CrudRepository;

public interface ContractEventFilterRepositoryFactory {

    CrudRepository<ContractEventFilter, String> build();
}
