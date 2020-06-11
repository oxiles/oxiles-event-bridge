package io.oxiles.chain.block.tx;

import io.oxiles.chain.block.tx.criteria.TransactionMatchingCriteria;
import io.oxiles.chain.block.BlockListener;

public interface TransactionMonitoringBlockListener extends BlockListener {

    void addMatchingCriteria(TransactionMatchingCriteria matchingCriteria);

    void removeMatchingCriteria(TransactionMatchingCriteria matchingCriteria);
}
