package io.oxiles.model;

import io.oxiles.dto.block.BlockDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

import javax.persistence.Entity;

@Document
@Entity
@Data
@NoArgsConstructor
public class LatestBlock {

    public LatestBlock(BlockDetails blockDetails) {
        this.nodeName = blockDetails.getNodeName();
        this.number = blockDetails.getNumber();
        this.hash = blockDetails.getHash();
        this.timestamp = blockDetails.getTimestamp();
    }

    @javax.persistence.Id
    @Id
    private String nodeName;

    private BigInteger number;

    private String hash;

    private BigInteger timestamp;
}
