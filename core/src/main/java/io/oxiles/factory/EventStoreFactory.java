package io.oxiles.factory;

import io.oxiles.integration.eventstore.SaveableEventStore;

public interface EventStoreFactory {

    SaveableEventStore build();
}
