package io.oxiles.chain.service.strategy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

public class DragonGlassTokenTransferData {
    @Getter
    @Setter
    String tokenId;
    @Getter
    @Setter
    String transactionId;
    @Getter
    @Setter
    String accountId;
    @Getter
    @Setter
    String transferTime;
    @Getter
    @Setter
    String amount;
    @Getter
    @Setter
    ArrayList toFromAccount;
}
