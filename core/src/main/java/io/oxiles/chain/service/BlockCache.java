package io.oxiles.chain.service;

import io.oxiles.chain.service.domain.Block;

import java.util.Set;

public interface BlockCache {

    void add(Block block);

    Set<Block> getCachedBlocks();
}
