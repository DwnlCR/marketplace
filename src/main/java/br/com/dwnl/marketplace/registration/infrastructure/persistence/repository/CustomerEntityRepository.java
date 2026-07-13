package br.com.dwnl.marketplace.registration.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import br.com.dwnl.marketplace.registration.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(excerptProjection = CustomerEntity.class)
public interface CustomerEntityRepository extends PagingAndSortingRepository<CustomerEntity, UUID>, CrudRepository<CustomerEntity, UUID> {
   List<CustomerEntity> findByFirstNameIgnoreCase(@Param("firstName") String firstName);

    @Override
    @RestResource(exported = false)
    void deleteById(UUID id);
}
