package io.oxiles.chain.block.tx.criteria;

import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.dto.transaction.TransactionStatus;

import java.util.List;

public class ToAddressMatchingCriteria extends SingleValueMatchingCriteria<String> {

    public ToAddressMatchingCriteria(String nodeName, String toAddress, List<TransactionStatus> statuses) {
        super(nodeName, toAddress, statuses);
    }

    @Override
    protected String getValueFromTx(TransactionDetails tx) {
        return tx.getTo();
    }

    @Override
    public boolean isOneTimeMatch() {
        return false;
    }
}
