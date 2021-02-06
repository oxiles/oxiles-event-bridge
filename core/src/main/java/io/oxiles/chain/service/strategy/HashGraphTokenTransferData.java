package io.oxiles.chain.service.strategy;

import io.oxiles.chain.settings.NodeType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.ArrayList;

@Document
@Entity
@Data
@NoArgsConstructor
public class HashGraphTokenTransferData {
    static public HashGraphTokenTransferData factory(DragonGlassTokenTransferData data) {
        HashGraphTokenTransferData hashGraph = new HashGraphTokenTransferData();
        hashGraph.tokenId = data.tokenId;
        hashGraph.accountId = data.accountId;
        hashGraph.transferTime = data.transferTime;
        hashGraph.transactionId = data.transactionId;
        hashGraph.amount = data.amount;
        hashGraph.toFromAccount = data.toFromAccount;
        hashGraph.nodeType = NodeType.DRAGONGLASS.getNodeType();

        return hashGraph;
    }

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
    @Getter
    @Setter
    String nodeType;

}
