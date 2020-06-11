package io.oxiles.chain.block;

import io.oxiles.chain.factory.BlockDetailsFactory;
import io.oxiles.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import lombok.AllArgsConstructor;
import io.oxiles.chain.service.domain.Block;
import org.springframework.stereotype.Component;

/**
 * A block listener that broadcasts the block details via the configured broadcaster.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
@Component
@AllArgsConstructor
public class BroadcastingBlockListener implements BlockListener {

    private BlockchainEventBroadcaster eventBroadcaster;

    private BlockDetailsFactory blockDetailsFactory;

    @Override
    public void onBlock(Block block) {
        eventBroadcaster.broadcastNewBlock(blockDetailsFactory.createBlockDetails(block));
    }


}
