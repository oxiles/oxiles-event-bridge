package io.oxiles.chain.service.strategy;

import io.reactivex.disposables.Disposable;
import io.oxiles.chain.block.BlockListener;

public interface BlockSubscriptionStrategy {
    Disposable subscribe();

    void unsubscribe();

    void addBlockListener(BlockListener blockListener);

    void removeBlockListener(BlockListener blockListener);

    boolean isSubscribed();
}
