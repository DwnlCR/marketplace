package br.com.dwnl.marketplace.ticketing.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false, path = "_customer")
public interface CustomerCrudRepository extends CrudRepository<CustomerEntity, UUID> {

}
