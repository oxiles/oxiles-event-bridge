package io.oxiles.integration.eventstore.db.repository;

import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HCSMessageTransactionDetailsRepository extends CrudRepository<HCSMessageTransactionDetails, String> {

    Optional<HCSMessageTransactionDetails> findFirstByMirrorNodeAndTopicIdOrderByConsensusTimestampDesc(String mirrorNode, String topicId);
}
