package io.oxiles.config;

import io.oxiles.integration.KafkaSettings;
import io.oxiles.integration.broadcast.internal.DoNothingEventeumEventBroadcaster;
import io.oxiles.integration.broadcast.internal.EventeumEventBroadcaster;
import io.oxiles.integration.broadcast.internal.KafkaEventeumEventBroadcaster;
import io.oxiles.integration.consumer.EventeumInternalEventConsumer;
import io.oxiles.integration.consumer.KafkaFilterEventConsumer;
import io.oxiles.service.SubscriptionService;
import io.oxiles.service.TransactionMonitoringService;
import io.oxiles.dto.message.EventeumMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Spring bean configuration for the FilterEvent broadcaster and consumer.
 *
 * If broadcaster.multiInstance is set to true, then register a Kafka broadcaster,
 * otherwise register a dummy broadcaster that does nothing.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
@Configuration
public class EventeumEventConfiguration {

    @Bean
    @ConditionalOnProperty(name="broadcaster.multiInstance", havingValue="true")
    public EventeumEventBroadcaster kafkaFilterEventBroadcaster(KafkaTemplate<String, EventeumMessage> kafkaTemplate,
                                                                KafkaSettings kafkaSettings) {
        return new KafkaEventeumEventBroadcaster(kafkaTemplate, kafkaSettings);
    }

    @Bean
    @ConditionalOnProperty(name="broadcaster.multiInstance", havingValue="true")
    public EventeumInternalEventConsumer kafkaFilterEventConsumer(SubscriptionService subscriptionService,
                                                                  TransactionMonitoringService transactionMonitoringService,
                                                                  KafkaSettings kafkaSettings) {
        return new KafkaFilterEventConsumer(subscriptionService, transactionMonitoringService, kafkaSettings);
    }

    @Bean
    @ConditionalOnProperty(name="broadcaster.multiInstance", havingValue="false")
    public EventeumEventBroadcaster doNothingFilterEventBroadcaster() {
        return new DoNothingEventeumEventBroadcaster();
    }
}
