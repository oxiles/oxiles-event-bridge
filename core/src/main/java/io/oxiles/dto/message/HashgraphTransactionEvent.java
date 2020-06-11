package io.oxiles.dto.message;

import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HashgraphTransactionEvent extends AbstractMessage<HashGraphTransactionData> {

    public static final String TYPE = "HASHGRAPH_TRANSACTION";

    public HashgraphTransactionEvent(HashGraphTransactionData hashGraphTransactionData) {
        super(hashGraphTransactionData.getHash(), TYPE, hashGraphTransactionData);
    }
}
