package io.oxiles.dto.message;

import io.oxiles.model.TransactionMonitoringSpec;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransactionMonitorRemoved extends AbstractMessage<TransactionMonitoringSpec> {

    public static final String TYPE = "TRANSACTION_MONITOR_REMOVED";

    public TransactionMonitorRemoved(TransactionMonitoringSpec spec) {
        super(spec.getId(), TYPE, spec);
    }
}
