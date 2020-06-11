package io.oxiles.chain.service.strategy;

import lombok.Getter;
import lombok.Setter;

public class HashGraphTransactionTransfer {
    static public HashGraphTransactionTransfer factory(Transfer transfer) {
        HashGraphTransactionTransfer hashGraphTransactionTransfer = new HashGraphTransactionTransfer();
        hashGraphTransactionTransfer.account = transfer.account;
        hashGraphTransactionTransfer.amount = transfer.amount;
        hashGraphTransactionTransfer.type = transfer.type;
        return hashGraphTransactionTransfer;
    }
    static public HashGraphTransactionTransfer factory(DragonGlassTransactionTransfer transfer) {
        HashGraphTransactionTransfer hashGraphTransactionTransfer = new HashGraphTransactionTransfer();
        hashGraphTransactionTransfer.account = transfer.accountID;
        hashGraphTransactionTransfer.amount = transfer.amount;
        return hashGraphTransactionTransfer;
    }
    @Getter
    @Setter
    String account;
    @Getter
    @Setter
    String amount;
    @Getter
    @Setter
    String type;
}
