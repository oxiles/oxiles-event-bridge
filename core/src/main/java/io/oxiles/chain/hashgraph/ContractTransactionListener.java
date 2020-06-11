package io.oxiles.chain.hashgraph;

import io.oxiles.chain.service.strategy.HashGraphTransactionData;

public interface ContractTransactionListener {

    /**
     * Called when a new transaction is detected on hashgraph contract
     *
     * @param txData The new transaction Data
     */
    void onTransaction(HashGraphTransactionData txData);
}
