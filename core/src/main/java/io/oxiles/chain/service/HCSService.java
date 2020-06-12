package io.oxiles.chain.service;

import com.hedera.hashgraph.sdk.consensus.ConsensusTopicId;
import com.hedera.hashgraph.sdk.mirror.MirrorClient;
import com.hedera.hashgraph.sdk.mirror.MirrorConsensusTopicQuery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import io.oxiles.chain.hcs.HCSMessageTransactionListener;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.service.EventStoreService;

@Data
@Slf4j
public class HCSService {

    private HCSMessageTransactionListener hcsMessageTransactionListener;
    private EventStoreService eventStoreService;
    private String mirrorNode;
    private MirrorClient mirrorClient;

    public HCSService(HCSMessageTransactionListener hcsMessageTransactionListener, EventStoreService eventStoreService,
                      String mirrorNode) {
        this.hcsMessageTransactionListener = hcsMessageTransactionListener;
        this.eventStoreService = eventStoreService;
        this.mirrorNode = mirrorNode;
        this.mirrorClient = new MirrorClient(mirrorNode);
    }

    public void subscribeToTopic(String topicId) {
        MirrorConsensusTopicQuery mirrorConsensusTopicQuery = new MirrorConsensusTopicQuery();
        mirrorConsensusTopicQuery.setTopicId(ConsensusTopicId.fromString(topicId));
        eventStoreService.getLatestMessageFromTopic(mirrorNode, topicId)
            .ifPresent(filter -> mirrorConsensusTopicQuery.setStartTime(filter.getConsensusTimestamp().plusMillis(1)));

        mirrorConsensusTopicQuery.subscribe(mirrorClient, resp -> {
                    try {
                        HCSMessageTransactionDetails hcsMessageTransactionDetails = new HCSMessageTransactionDetails(mirrorNode, topicId,
                                resp.consensusTimestamp, new String(resp.message), resp.sequenceNumber);
                        this.hcsMessageTransactionListener.onMessageTransaction(hcsMessageTransactionDetails);
                    } catch (Exception e) {
                        log.error("Error broadcasting hcs message", e);
                    }
                },
                e -> log.error("Error subscribing to hcs topic", e));
    }
}
