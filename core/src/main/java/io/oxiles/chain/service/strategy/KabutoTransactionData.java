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
import java.util.List;

@Document
@Entity
@Data
@NoArgsConstructor
public class KabutoTransactionData {
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
    List<Transfer> transfers;
}
