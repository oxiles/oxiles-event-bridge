package io.oxiles.chain.block.tx.criteria;

import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.dto.transaction.TransactionStatus;

import java.util.List;

public class TopicMatchingCriteria extends SingleValueMatchingCriteria<String> {

    public TopicMatchingCriteria(String nodeName, String fromAddress, List<TransactionStatus> statuses) {
        super(nodeName, fromAddress, statuses);
    }

    @Override
    protected String getValueFromTx(TransactionDetails tx) {
        return null;
    }

    @Override
    public boolean isOneTimeMatch() {
        return false;
    }
}
