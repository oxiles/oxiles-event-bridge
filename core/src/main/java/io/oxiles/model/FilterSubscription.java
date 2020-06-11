package io.oxiles.model;

import io.reactivex.disposables.Disposable;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.oxiles.dto.event.filter.ContractEventFilter;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class FilterSubscription {

    private ContractEventFilter filter;

    private Disposable subscription;

    private BigInteger startBlock;

    public FilterSubscription(ContractEventFilter filter, Disposable subscription) {
        this.filter = filter;
        this.subscription = subscription;
    }
}
