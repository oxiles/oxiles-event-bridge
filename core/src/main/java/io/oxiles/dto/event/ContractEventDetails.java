package io.oxiles.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.oxiles.dto.event.parameter.EventParameter;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * Represents the details of an emitted Ethereum smart contract event.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
@Document
@Entity
@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractEventDetails {

    private String name;

    @Id
    private String filterId;

    private String nodeName;

    @Lob // required because of https://stackoverflow.com/questions/43412517/sql-string-or-binary-data-would-be-truncated-error/43426863
    @ElementCollection
    private List<EventParameter> indexedParameters;


    private List<EventParameter> nonIndexedParameters;

    private String transactionHash;

    private BigInteger logIndex;

    private BigInteger blockNumber;

    private String blockHash;

    private String address;

    private String from;

    private ContractEventStatus status = ContractEventStatus.UNCONFIRMED;

    private String eventSpecificationSignature;

    private String networkName;

    private BigInteger blockTimestamp;

    public String getId() {
        return transactionHash + "-" + blockHash + "-" + logIndex;
    }
}
