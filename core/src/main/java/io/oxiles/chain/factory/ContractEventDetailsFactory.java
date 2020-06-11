package io.oxiles.chain.factory;

import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.chain.service.domain.TransactionReceipt;
import io.oxiles.dto.event.filter.ContractEventFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;

/**
 * A factory interface for creating ContractEventDetails objects from the event filter plus the
 * Web3J log.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
public interface ContractEventDetailsFactory {
    ContractEventDetails createEventDetails(ContractEventFilter eventFilter, Log log, EthBlock ethBlock, TransactionReceipt transactionReceipt);
}
