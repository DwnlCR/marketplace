package br.com.dwnl.marketplace.registration.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.registration.domain.Customer;
import br.com.dwnl.marketplace.registration.domain.CustomerId;
import br.com.dwnl.marketplace.registration.domain.CustomerRepository;
import br.com.dwnl.marketplace.registration.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


@Repository
public class JpaCustomerRepository implements CustomerRepository {
    private final CustomerEntityRepository repository;

    public JpaCustomerRepository(CustomerEntityRepository repository){
        this.repository = repository;
    }

    @Override
    public Customer save(Customer customer) {
        var entity = convert(customer);
        var savedEntity = repository.save(entity);
        return mapper(savedEntity);
    }

    @Override
    public List<Customer> findAll() {
        var iterable = repository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(JpaCustomerRepository::mapper)
                .toList();
    }

    private static CustomerEntity convert(Customer customer){
        var entity = new CustomerEntity();
        entity.setId(customer.getId().id());
        entity.setFirstName(customer.getName());
        entity.setEmail(customer.getEmail());

        return entity;
    }

    private static Customer mapper(CustomerEntity entity){
        String fullName = Optional.ofNullable(entity.getLastName())
                .map(lastName -> entity.getFirstName() + " " + lastName)
                .orElseGet(entity::getFirstName);

        return new Customer(new CustomerId(entity.getId()), fullName, entity.getEmail());
    }
}
