package io.oxiles.chain.service.strategy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Lob;
import java.util.List;

public class DragonGlassTransactionData {
    @Getter
    @Setter
    String id;
    @Getter
    @Setter
    String transactionHash;
    @Getter
    @Setter
    String startTime;
    @Getter
    @Setter
    String consensusTime;
    @Getter
    @Setter
    String amount;
    @Getter
    @Setter
    String transactionFee;
    @Getter
    @Setter
    String memo;
    @Getter
    @Setter
    String status;

    @Getter
    @Setter
    String nodeId;
    @Getter
    @Setter
    String serviceType;
    @Getter
    @Setter
    String payerId;

    @Getter
    @Setter
    String topicID;

    @Getter
    @Setter
    @Lob
    @ElementCollection
    List<DragonGlassTransactionTransfer> transfers;
}
