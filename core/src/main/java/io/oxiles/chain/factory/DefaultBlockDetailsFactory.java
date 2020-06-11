package io.oxiles.chain.factory;

import io.oxiles.chain.service.domain.Block;
import io.oxiles.dto.block.BlockDetails;
import org.springframework.stereotype.Component;

@Component
public class DefaultBlockDetailsFactory implements BlockDetailsFactory {

    @Override
    public BlockDetails createBlockDetails(Block block) {
        final BlockDetails blockDetails = new BlockDetails();

        blockDetails.setNumber(block.getNumber());
        blockDetails.setHash(block.getHash());
        blockDetails.setTimestamp(block.getTimestamp());
        blockDetails.setNodeName(block.getNodeName());

        return blockDetails;
    }
}
