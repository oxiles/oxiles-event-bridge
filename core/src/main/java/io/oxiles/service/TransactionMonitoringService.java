package io.oxiles.service;

import io.oxiles.model.TransactionMonitoringSpec;
import io.oxiles.service.exception.NotFoundException;

public interface TransactionMonitoringService {

    void registerTransactionsToMonitor(TransactionMonitoringSpec spec);

    void registerTransactionsToMonitor(TransactionMonitoringSpec spec, boolean broadcast);

    void stopMonitoringTransactions(String id) throws NotFoundException;

    void stopMonitoringTransactions(String id, boolean broadcast) throws NotFoundException;
}
