package io.oxiles.integration.eventstore.db;

import java.util.List;
import java.util.Optional;

import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.integration.eventstore.db.repository.ContractEventDetailsRepository;
import io.oxiles.integration.eventstore.db.repository.HCSMessageTransactionDetailsRepository;
import io.oxiles.integration.eventstore.db.repository.LatestBlockRepository;
import io.oxiles.integration.eventstore.db.repository.TransactionDetailsRepository;
import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.integration.eventstore.SaveableEventStore;
import io.oxiles.model.LatestBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * A saveable event store that stores contract events in a db repository.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
public class MongoEventStore implements SaveableEventStore {

    private ContractEventDetailsRepository eventDetailsRepository;

    private LatestBlockRepository latestBlockRepository;

    private MongoTemplate mongoTemplate;

    private TransactionDetailsRepository transactionDetailsRepository;

    private HCSMessageTransactionDetailsRepository hcsMessageTransactionDetailsRepository;

    public MongoEventStore(
            ContractEventDetailsRepository eventDetailsRepository,
            LatestBlockRepository latestBlockRepository,
            TransactionDetailsRepository transactionDetailsRepository,
            HCSMessageTransactionDetailsRepository hcsMessageTransactionDetailsRepository,
            MongoTemplate mongoTemplate) {
        this.eventDetailsRepository = eventDetailsRepository;
        this.latestBlockRepository = latestBlockRepository;
        this.transactionDetailsRepository = transactionDetailsRepository;
        this.hcsMessageTransactionDetailsRepository = hcsMessageTransactionDetailsRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<ContractEventDetails> getContractEventsForSignature(
            String eventSignature, String contractAddress, PageRequest pagination) {

        final Query query = new Query(
                Criteria.where("eventSpecificationSignature")
                        .is(eventSignature)
                        .and("address")
                        .is(contractAddress))
                .with(new Sort(Direction.DESC, "blockNumber"))
                .collation(Collation.of("en").numericOrderingEnabled());

        final long totalResults = mongoTemplate.count(query, ContractEventDetails.class);

        //Set pagination on query
        query
                .skip(pagination.getPageNumber() * pagination.getPageSize())
                .limit(pagination.getPageSize());

        final List<ContractEventDetails> results = mongoTemplate.find(query, ContractEventDetails.class);

        return new PageImpl<>(results, pagination, totalResults);
    }

    @Override
    public Optional<LatestBlock> getLatestBlockForNode(String nodeName) {
        final Iterable<LatestBlock> blocks = latestBlockRepository.findAll();

        return latestBlockRepository.findById(nodeName);
    }

    @Override
    public boolean isPagingZeroIndexed() {
        return true;
    }

    @Override
    public boolean txExistsById(String id) {
        return transactionDetailsRepository.existsById(id);
    }

    @Override
    public Optional<HCSMessageTransactionDetails> getLatestMessageFromTopic(String mirrorNode, String topicId) {
        return hcsMessageTransactionDetailsRepository.findFirstByMirrorNodeAndTopicIdOrderByConsensusTimestampDesc(mirrorNode, topicId);
    }

    @Override
    public void save(ContractEventDetails contractEventDetails) {
        eventDetailsRepository.save(contractEventDetails);
    }

    @Override
    public void save(LatestBlock latestBlock) { latestBlockRepository.save(latestBlock); }

    @Override
    public void save(HashGraphTransactionData txData) { transactionDetailsRepository.save(txData); }

    @Override
    public void save(HCSMessageTransactionDetails hcsMessageTransactionDetails) {
        hcsMessageTransactionDetailsRepository.save(hcsMessageTransactionDetails);
    }
}
