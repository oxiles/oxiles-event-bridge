package io.oxiles.chain.factory;

import io.oxiles.chain.service.domain.Block;
import io.oxiles.dto.block.BlockDetails;

public interface BlockDetailsFactory {

    BlockDetails createBlockDetails(Block block);
}
