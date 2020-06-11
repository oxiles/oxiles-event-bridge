package io.oxiles.chain.block.tx.criteria;

import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.dto.transaction.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public abstract class SingleValueMatchingCriteria<T> implements TransactionMatchingCriteria {

    private String nodeName;

    private T valueToMatch;

    private List<TransactionStatus> statuses;

    @Override
    public boolean isAMatch(TransactionDetails tx) {
        return valueToMatch.equals(getValueFromTx(tx));
    }

    protected abstract T getValueFromTx(TransactionDetails tx);
}
