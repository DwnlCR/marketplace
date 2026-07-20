package br.com.dwnl.marketplace.ticketing.infrastructure.event;

import br.com.dwnl.marketplace.catalog.domain.EventMetadata;
import br.com.dwnl.marketplace.common.infrastructure.event.dto.CustomerCreated;
import br.com.dwnl.marketplace.common.infrastructure.event.dto.EventUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TicketingEventListener {
    private static final Logger logger = LoggerFactory.getLogger(TicketingEventListener.class);

    @EventListener
    @Async
    public void handle(CustomerCreated event){
        logger.info("CustomerCreated received {}", event);
    }

    @EventListener
    @Async
    public void handle(EventUpdated event){
        logger.info("EventUpdated received {}", event);
    }
}
