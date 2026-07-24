package br.com.dwnl.marketplace.ticketing.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false, path = "_event")
public interface EventCrudRepository extends CrudRepository<EventEntity, UUID> {
}
