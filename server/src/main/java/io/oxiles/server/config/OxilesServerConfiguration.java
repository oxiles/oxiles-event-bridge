package io.oxiles.server.config;

import io.oxiles.annotation.ConditionalOnKafkaRequired;
import net.consensys.kafkadl.annotation.EnableKafkaDeadLetter;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableKafkaDeadLetter(topics = {"#{eventeumKafkaSettings.eventeumEventsTopic}"},
                       containerFactoryBeans = {"kafkaListenerContainerFactory", "eventeumKafkaListenerContainerFactory"},
                       serviceId = "eventeum")
@ConditionalOnKafkaRequired
public class OxilesServerConfiguration {
}
