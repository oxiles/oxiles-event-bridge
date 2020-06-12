package io.oxiles.dto.message;

import lombok.NoArgsConstructor;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;

@NoArgsConstructor
public class HCSMessageTransactionEvent extends AbstractMessage<HCSMessageTransactionDetails> {

    public static final String TYPE = "HCS_TRANSACTION";

    public HCSMessageTransactionEvent(HCSMessageTransactionDetails hcsMessageTransactionDetails) {
        super(Integer.valueOf(hcsMessageTransactionDetails.hashCode()).toString(), TYPE, hcsMessageTransactionDetails);
    }
}
