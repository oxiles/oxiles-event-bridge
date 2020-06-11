package io.oxiles.chain.service.strategy;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

@Document
@Entity
@Data
@NoArgsConstructor
public class HashGraphTransactionData {
    static public HashGraphTransactionData factory(KabutoTransactionData data) {
        HashGraphTransactionData hashGraph = new HashGraphTransactionData();
        hashGraph.id = data.id;
        hashGraph.hash = data.hash;
        hashGraph.validStartAt = data.validStartAt;
        hashGraph.consensusAt = data.consensusAt;
        hashGraph.value = data.value;
        hashGraph.fee = data.fee;
        hashGraph.memo = data.memo;
        hashGraph.status = data.status;
        hashGraph.node = data.node;
        hashGraph.type = data.type;
        hashGraph.operator = data.operator;
        hashGraph.transfers = new ArrayList<HashGraphTransactionTransfer>();
        for (int cont = 0; cont < data.transfers.size(); cont++) {
            hashGraph.transfers.add(HashGraphTransactionTransfer.factory(data.transfers.get(cont)));
        }
        return hashGraph;
    }
    static public HashGraphTransactionData factory(DragonGlassTransactionData data) {
        HashGraphTransactionData hashGraph = new HashGraphTransactionData();
        hashGraph.id = data.id;
        hashGraph.hash = data.transactionHash;
        hashGraph.validStartAt = data.startTime;
        hashGraph.consensusAt = data.consensusTime;
        hashGraph.value = data.amount;
        hashGraph.fee = data.transactionFee;
        hashGraph.memo = data.memo;
        hashGraph.status = data.status;
        hashGraph.node = data.nodeId;
        hashGraph.type = data.serviceType;
        hashGraph.operator = data.payerId;
        hashGraph.transfers = new ArrayList<HashGraphTransactionTransfer>();
        for (int cont = 0; cont < data.transfers.size(); cont++) {
            hashGraph.transfers.add(HashGraphTransactionTransfer.factory(data.transfers.get(cont)));
        }
        return hashGraph;
    }
    @Getter
    @Setter
    @Id
    @org.springframework.data.annotation.Id
    String id;
    @Getter
    @Setter
    String hash;
    @Getter
    @Setter
    String validStartAt;
    @Getter
    @Setter
    String consensusAt;
    @Getter
    @Setter
    String value;
    @Getter
    @Setter
    String fee;
    @Getter
    @Setter
    String memo;
    @Getter
    @Setter
    String status;
    @Getter
    @Setter
    String node;
    @Getter
    @Setter
    String type;
    @Getter
    @Setter
    String operator;
    @Getter
    @Setter
    @Lob
    @ElementCollection
    List<HashGraphTransactionTransfer> transfers;
}
