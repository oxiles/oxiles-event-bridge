package io.oxiles.integration.consumer;

import io.oxiles.dto.message.EventeumMessage;

/**
 * A consumer for internal Eventeum messages sent from a different instance.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
public interface EventeumInternalEventConsumer {
    void onMessage(EventeumMessage<?> message);
}
