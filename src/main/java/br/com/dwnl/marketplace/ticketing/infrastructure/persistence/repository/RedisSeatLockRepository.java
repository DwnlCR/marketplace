package br.com.dwnl.marketplace.ticketing.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.SeatLockEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface RedisSeatLockRepository extends CrudRepository<SeatLockEntity, String> {
    Optional<SeatLockEntity> findByCustomerId(String customerId);
}
