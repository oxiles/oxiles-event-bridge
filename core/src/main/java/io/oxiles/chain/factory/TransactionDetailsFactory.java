package io.oxiles.chain.factory;

import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.dto.transaction.TransactionStatus;
import io.oxiles.chain.service.domain.Block;
import io.oxiles.chain.service.domain.Transaction;

public interface TransactionDetailsFactory {
    TransactionDetails createTransactionDetails(
            Block block, Transaction transaction, TransactionStatus status);
}
