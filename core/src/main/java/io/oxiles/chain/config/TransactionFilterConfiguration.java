package io.oxiles.chain.config;

import lombok.Data;
import io.oxiles.model.TransactionMonitoringSpec;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties
@Data
public class TransactionFilterConfiguration {
    private List<TransactionMonitoringSpec> transactionFilters;

    public List<TransactionMonitoringSpec> getConfiguredTransactionFilters() {
        List<TransactionMonitoringSpec> filtersToReturn = new ArrayList<>();

        if (transactionFilters == null) {
            return filtersToReturn;
        }

        transactionFilters.forEach((configFilter) -> {
            final TransactionMonitoringSpec contractTransactionFilter = new TransactionMonitoringSpec(
                    configFilter.getType(),
                    configFilter.getTransactionIdentifierValue(),
                    configFilter.getNodeType(),
                    configFilter.getNodeName(),
                    configFilter.getStatuses()
            );

            filtersToReturn.add(contractTransactionFilter);
        });

        return filtersToReturn;
    }
}
