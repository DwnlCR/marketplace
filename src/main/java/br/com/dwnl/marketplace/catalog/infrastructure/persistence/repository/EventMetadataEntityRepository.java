package br.com.dwnl.marketplace.catalog.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.catalog.domain.EventMetadata;
import br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity.EventMetadataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource
public interface EventMetadataEntityRepository extends MongoRepository<EventMetadataEntity, String> {
    Optional<EventMetadata> findByEventId(UUID eventId);
}
