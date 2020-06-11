package io.oxiles.chain.block.tx.criteria.factory;

import io.oxiles.chain.block.tx.criteria.TransactionMatchingCriteria;
import io.oxiles.model.TransactionMonitoringSpec;

public interface TransactionMatchingCriteriaFactory {

    TransactionMatchingCriteria build(TransactionMonitoringSpec spec);
}
