package io.oxiles.dto.hcs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Document("hcsMessageTransactionDetails")
@Entity
@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HCSMessageTransactionDetails {

    @Id
    @GeneratedValue
    private String id;
    private String mirrorNode;
    private String topicId;
    private Instant consensusTimestamp;
    private String message;
    private long sequenceNumber;
    private byte[] runningHash;

    public HCSMessageTransactionDetails(String mirrorNode, String topicId, Instant consensusTimestamp, String message, long sequenceNumber) {
        this.mirrorNode = mirrorNode;
        this.topicId = topicId;
        this.consensusTimestamp = consensusTimestamp;
        this.message = message;
        this.sequenceNumber = sequenceNumber;
    }
}
