package io.oxiles.chain.block.tx.criteria.factory;

import io.oxiles.chain.block.tx.criteria.*;
import io.oxiles.model.TransactionIdentifierType;
import io.oxiles.model.TransactionMonitoringSpec;
import org.springframework.stereotype.Component;

@Component
public class DefaultTransactionMatchingCriteriaFactory implements TransactionMatchingCriteriaFactory {

    @Override
    public TransactionMatchingCriteria build(TransactionMonitoringSpec spec) {
        if (spec.getType() == TransactionIdentifierType.HASH) {
            return new TxHashMatchingCriteria(spec.getNodeName(), spec.getTransactionIdentifierValue(), spec.getStatuses());
        }

        if (spec.getType() == TransactionIdentifierType.TO_ADDRESS) {
            return new ToAddressMatchingCriteria(spec.getNodeName(), spec.getTransactionIdentifierValue(), spec.getStatuses());
        }

        if (spec.getType() == TransactionIdentifierType.FROM_ADDRESS) {
            return new FromAddressMatchingCriteria(spec.getNodeName(), spec.getTransactionIdentifierValue(), spec.getStatuses());
        }

        if (spec.getType() == TransactionIdentifierType.FROM_TOKEN) {
            return new TokenMatchingCriteria(spec.getNodeName(), spec.getTransactionIdentifierValue(), spec.getStatuses());
        }

        if (spec.getType() == TransactionIdentifierType.TOPIC) {
            return new TopicMatchingCriteria(spec.getNodeName(), spec.getTransactionIdentifierValue(), spec.getStatuses());
        }



        throw new UnsupportedOperationException("Type: " + spec.getType() + " not currently supported");
    }
}
