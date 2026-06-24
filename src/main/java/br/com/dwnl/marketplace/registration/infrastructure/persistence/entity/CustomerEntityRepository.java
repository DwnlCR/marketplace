package br.com.dwnl.marketplace.registration.infrastructure.persistence.entity;

import br.com.dwnl.marketplace.registration.domain.Customer;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.repository.CrudRepository;

public interface CustomerEntityRepository extends CrudRepository<CustomerEntity, UUID> {
}
