package io.oxiles.chain.block.tx.criteria;

import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.dto.transaction.TransactionStatus;

import java.util.List;

public class TokenMatchingCriteria extends SingleValueMatchingCriteria<String> {

    public TokenMatchingCriteria(String nodeName, String tokenId, List<TransactionStatus> statuses) {
        super(nodeName, tokenId, statuses);
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
