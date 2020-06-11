package io.oxiles.chain.service.container;

import lombok.Data;
import io.oxiles.chain.service.BlockchainService;
import org.web3j.protocol.Web3j;

@Data
public class EthereumNodeServices extends AbstractNodeServices {

    private Web3j web3j;

    private BlockchainService blockchainService;
}
