package io.oxiles.chain.hcs;

import io.oxiles.dto.hcs.HCSMessageTransactionDetails;

public interface HCSMessageTransactionListener {

    void onMessageTransaction(HCSMessageTransactionDetails hcsMessageTransactionDetails);
}
