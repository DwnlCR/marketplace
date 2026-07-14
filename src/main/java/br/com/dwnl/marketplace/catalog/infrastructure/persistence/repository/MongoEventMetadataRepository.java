package br.com.dwnl.marketplace.catalog.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.catalog.domain.EventId;
import br.com.dwnl.marketplace.catalog.domain.EventMetadata;
import br.com.dwnl.marketplace.catalog.domain.EventMetadataRepository;

import java.util.Optional;

public class MongoEventMetadataRepository implements EventMetadataRepository {
    private final EventMetadataEntityRepository eventMetadataEntityRepository;

    public MongoEventMetadataRepository(EventMetadataEntityRepository eventMetadataEntityRepository) {
        this.eventMetadataEntityRepository = eventMetadataEntityRepository;
    }

    @Override
    public Optional<EventMetadata> findByEventId(EventId eventId) {
        eventMetadataEntityRepository.findByEventId(eventId.id());
        return Optional.empty();
    }
}
