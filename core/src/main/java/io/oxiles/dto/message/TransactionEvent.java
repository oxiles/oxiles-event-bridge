package io.oxiles.dto.message;

import io.oxiles.dto.transaction.TransactionDetails;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransactionEvent extends AbstractMessage<TransactionDetails> {

    public static final String TYPE = "TRANSACTION";

    public TransactionEvent(TransactionDetails details) {
        super(details.getHash(), TYPE, details);
    }
}
