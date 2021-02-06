package io.oxiles.integration.broadcast.blockchain;

import io.oxiles.chain.service.strategy.HashGraphTokenTransferData;
import io.oxiles.dto.block.BlockDetails;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.dto.transaction.TransactionDetails;
import io.oxiles.chain.service.strategy.HashGraphTransactionData;

/**
 * An interface for a class that broadcasts ethereum blockchain details to the wider system.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
public interface BlockchainEventBroadcaster {

    /**
     * Broadcast details of a new block that has been mined.
     * @param block
     */
    void broadcastNewBlock(BlockDetails block);

    /**
     * Broadcasts details of a new smart contract event that has been emitted from the ethereum blockchain.
     * @param eventDetails
     */
    void broadcastContractEvent(ContractEventDetails eventDetails);

    /**
     * Broadcasts details of a monitored transaction that has been mined.
     * @param transactionDetails
     */
    void broadcastTransaction(TransactionDetails transactionDetails);

    /**
     * Broadcasts details of a monitored transaction that has been mined.
     * @param txData
     */
    void broadcastTransaction(HashGraphTransactionData txData);

    /**
     * Broadcasts details of a monitored transaction that has been mined.
     * @param transferData
     */
    void broadcastTransaction(HashGraphTokenTransferData transferData);

    /**
     * Broadcasts details of a transaction message from HCS.
     * @param hcsMessageTransactionDetails
     */
    void broadcastMessageTransaction(HCSMessageTransactionDetails hcsMessageTransactionDetails);
}
