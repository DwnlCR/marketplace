package br.com.dwnl.marketplace.catalog.domain;

import java.util.Optional;

public interface EventMetadataRepository {
    Optional<EventMetadata> findByEventId(EventId eventId);

    void saveEventMetadata(EventId eventId, EventMetadata metadata);
    void deleteEventMetadata(EventId eventId);
}
