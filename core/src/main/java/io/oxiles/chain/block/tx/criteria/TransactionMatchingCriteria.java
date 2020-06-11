package io.oxiles.chain.block.tx.criteria;

import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.dto.transaction.TransactionStatus;

import java.util.List;

public interface TransactionMatchingCriteria {

    String getNodeName();

    List<TransactionStatus> getStatuses();

    boolean isAMatch(TransactionDetails tx);

    boolean isOneTimeMatch();
}
