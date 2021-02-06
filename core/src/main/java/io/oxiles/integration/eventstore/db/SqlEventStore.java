package io.oxiles.integration.eventstore.db;

import java.util.Optional;

import io.oxiles.chain.service.strategy.HashGraphTokenTransferData;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.integration.eventstore.db.repository.ContractEventDetailsRepository;
import io.oxiles.integration.eventstore.db.repository.LatestBlockRepository;
import io.oxiles.integration.eventstore.db.repository.TokenTransferRepository;
import io.oxiles.integration.eventstore.db.repository.TransactionDetailsRepository;
import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.integration.eventstore.SaveableEventStore;
import io.oxiles.model.LatestBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * A saveable event store that stores contract events in a db repository.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
public class SqlEventStore implements SaveableEventStore {

    private ContractEventDetailsRepository eventDetailsRepository;

    private LatestBlockRepository latestBlockRepository;

    private JdbcTemplate jdbcTemplate;

    private TransactionDetailsRepository transactionDetailsRepository;

    private TokenTransferRepository tokenTransferDetailRepository;

    public SqlEventStore(
            ContractEventDetailsRepository eventDetailsRepository,
            LatestBlockRepository latestBlockRepository,
            TransactionDetailsRepository transactionDetailsRepository,
            JdbcTemplate jdbcTemplate) {
        this.eventDetailsRepository = eventDetailsRepository;
        this.latestBlockRepository = latestBlockRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.transactionDetailsRepository = transactionDetailsRepository;
    }

    @Override
    public Page<ContractEventDetails> getContractEventsForSignature(
            String eventSignature, String contractAddress, PageRequest pagination) {
        return eventDetailsRepository.findByEventSpecificationSignatureAndAddress(
        		eventSignature, contractAddress, pagination);
    }

    @Override
    public Optional<LatestBlock> getLatestBlockForNode(String nodeName) {
        final Iterable<LatestBlock> blocks = latestBlockRepository.findAll();

        return latestBlockRepository.findById(nodeName);
    }

    @Override
    public boolean txExistsByHashAndNodeType(String hash, String nodeType) {
        return transactionDetailsRepository.existsByHashAndNodeType(hash, nodeType);
    }

    @Override
    public boolean transferExistsByHashAndNodeType(String hash, String nodeType) {
        return tokenTransferDetailRepository.existsByTransactionIdAndNodeType(hash, nodeType);
    }

    @Override
    public boolean isPagingZeroIndexed() {
        return true;
    }

    @Override
    public void save(ContractEventDetails contractEventDetails) {
        eventDetailsRepository.save(contractEventDetails);
    }

    @Override
    public void save(LatestBlock latestBlock) {
        latestBlockRepository.save(latestBlock);
    }

    @Override
    public void save(HashGraphTransactionData txData) {
        transactionDetailsRepository.save(txData);
    }

    @Override
    public void save(HashGraphTokenTransferData transferData) {
        tokenTransferDetailRepository.save(transferData);
    }
}
