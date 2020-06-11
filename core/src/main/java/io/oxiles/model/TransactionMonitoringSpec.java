package io.oxiles.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.oxiles.chain.settings.NodeType;
import io.oxiles.constant.Constants;
import io.oxiles.dto.transaction.TransactionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Document
@Entity
@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class TransactionMonitoringSpec {
    //TODO: remove rnodetype

    @Id
    private String id;

    private TransactionIdentifierType type;

    private NodeType nodeType =  NodeType.NORMAL;

    private String nodeName = Constants.DEFAULT_NODE_NAME;

    //Need to wrap in an ArrayList so its modifiable
    @ElementCollection
    @Enumerated(EnumType.ORDINAL)
    private List<TransactionStatus> statuses = new ArrayList(
            Arrays.asList(TransactionStatus.UNCONFIRMED, TransactionStatus.CONFIRMED, TransactionStatus.FAILED));

    private String transactionIdentifierValue;

    public TransactionMonitoringSpec(TransactionIdentifierType type,
                                     String transactionIdentifierValue,
                                     NodeType nodeType,
                                     String nodeName,
                                     List<TransactionStatus> statuses) {
        this.type = type;
        this.transactionIdentifierValue = transactionIdentifierValue;
        this.nodeName = nodeName;

        if (statuses != null && !statuses.isEmpty()) {
            this.statuses = statuses;
        }

        convertToCheckSum();

        this.id = Hash.sha3String(transactionIdentifierValue + type + nodeName + this.statuses.toString()).substring(2);
    }

    public TransactionMonitoringSpec(TransactionIdentifierType type,
                                     String transactionIdentifierValue,
                                     NodeType nodeType,
                                     String nodeName) {
        this(type, transactionIdentifierValue, nodeType, nodeName, null);
    }

    @JsonSetter("type")
    public void setType(String type) {
        this.type = TransactionIdentifierType.valueOf(type.toUpperCase());
    }

    @JsonSetter("type")
    public void setType(TransactionIdentifierType type) {
        this.type = type;
    }


    @JsonSetter("nodeType")
    public void setNodeType(String nodeType) {
        this.nodeType = NodeType.valueOf(nodeType.toUpperCase());
    }

    @JsonSetter("nodeType")
    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void generateId() {
        this.id = Hash.sha3String(transactionIdentifierValue + type + nodeName + statuses.toString()).substring(2);
    }

    public void convertToCheckSum() {
        if (this.type != TransactionIdentifierType.HASH  &&  !transactionIdentifierValue.contains(".")) {
            this.transactionIdentifierValue = Keys.toChecksumAddress(this.transactionIdentifierValue);
        }
    }
}
