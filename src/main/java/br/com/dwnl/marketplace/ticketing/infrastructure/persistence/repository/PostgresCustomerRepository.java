package br.com.dwnl.marketplace.ticketing.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.ticketing.domain.Customer;
import br.com.dwnl.marketplace.ticketing.domain.CustomerRepository;
import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresCustomerRepository implements CustomerRepository {
    private final CustomerCrudRepository customerCrudRepository;

    public PostgresCustomerRepository(CustomerCrudRepository customerCrudRepository) {
        this.customerCrudRepository = customerCrudRepository;
    }

    @Override
    public void save(Customer customer) {
        var entity = new CustomerEntity(customer.getId(), customer.getCorrelationId().id(), customer.getName());
        customerCrudRepository.save(entity);
    }
}
