package br.com.dwnl.marketplace.registration.infrastructure.persistence.entity.projection;

import br.com.dwnl.marketplace.registration.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "exerpt", types = CustomerEntity.class)
public interface CustomerExcerpt {

    String getFirstName();

    String getLastName();

    @Value("#{target.address?.toString()}")
    String getAddress();
}
