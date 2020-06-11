package io.oxiles.factory;

import io.oxiles.dto.event.filter.ContractEventFilter;

import java.util.List;

public interface ContractEventFilterFactory {

    List<ContractEventFilter> build();
}
