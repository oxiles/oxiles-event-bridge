package io.oxiles.integration.broadcast.blockchain;

import io.oxiles.chain.service.strategy.HashGraphTokenTransferData;
import io.oxiles.dto.block.BlockDetails;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.dto.transaction.TransactionDetails;
import lombok.AllArgsConstructor;
import io.oxiles.chain.service.strategy.HashGraphTransactionData;

@AllArgsConstructor
    public class ListenerInvokingBlockchainEventBroadcaster implements BlockchainEventBroadcaster {

    private OnBlockchainEventListener listener;

    @Override
    public void broadcastNewBlock(BlockDetails block) {
        listener.onNewBlock(block);
    }

    @Override
    public void broadcastContractEvent(ContractEventDetails eventDetails) {
        listener.onContractEvent(eventDetails);
    }

    @Override
    public void broadcastTransaction(TransactionDetails transactionDetails) {
        listener.onTransactionEvent(transactionDetails);
    }

    @Override
    public void broadcastTransaction(HashGraphTransactionData txData) {

    }

    @Override
    public void broadcastTransaction(HashGraphTokenTransferData txData) {

    }

    @Override
    public void broadcastMessageTransaction(HCSMessageTransactionDetails hcsMessageTransactionDetails) {
        listener.onTransactionEvent(hcsMessageTransactionDetails);
    }

    public interface OnBlockchainEventListener {

        void onNewBlock(BlockDetails block);

        void onContractEvent(ContractEventDetails eventDetails);

        void onTransactionEvent(TransactionDetails transactionDetails);

        void onTransactionEvent(HCSMessageTransactionDetails hcsMessageTransactionDetails);
    }

}
