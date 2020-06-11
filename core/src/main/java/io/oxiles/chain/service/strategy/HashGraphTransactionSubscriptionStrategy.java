package io.oxiles.chain.service.strategy;

import io.reactivex.disposables.Disposable;

public interface HashGraphTransactionSubscriptionStrategy {
    Disposable subscribe();

    void unsubscribe();

    //void addTransactionListener(HashgraphTransactionListener blockListener);

    boolean isSubscribed();
}
