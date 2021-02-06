package io.oxiles.dto.message;

import io.oxiles.chain.service.strategy.HashGraphTokenTransferData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HashgraphTokenTransferEvent extends AbstractMessage<HashGraphTokenTransferData> {

    public static final String TYPE = "HASHGRAPH_TOKEN_TRANSFER";


    public HashgraphTokenTransferEvent(HashGraphTokenTransferData hashGraphTokenTransferData) {
        super(hashGraphTokenTransferData.getTransactionId(), TYPE, hashGraphTokenTransferData);
    }
}
