package io.oxiles.chain.factory;

import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.dto.transaction.TransactionStatus;
import io.oxiles.chain.service.domain.Block;
import io.oxiles.chain.service.domain.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Keys;

@Component
public class DefaultTransactionDetailsFactory implements TransactionDetailsFactory {

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public TransactionDetails createTransactionDetails(
            Block block, Transaction transaction, TransactionStatus status) {

        final TransactionDetails transactionDetails = new TransactionDetails();
        modelMapper.map(transaction, transactionDetails);

        transactionDetails.setNodeName(block.getNodeName());
        transactionDetails.setStatus(status);
        transactionDetails.setBlockTimestamp(block.getTimestamp().toString());

        if (transaction.getCreates() != null) {
            transactionDetails.setContractAddress(Keys.toChecksumAddress(transaction.getCreates()));
        }

        return transactionDetails;
    }
}
