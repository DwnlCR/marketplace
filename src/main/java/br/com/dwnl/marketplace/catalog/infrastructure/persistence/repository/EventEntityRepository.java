package br.com.dwnl.marketplace.catalog.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface EventEntityRepository extends CrudRepository<EventEntity, UUID> {

}
