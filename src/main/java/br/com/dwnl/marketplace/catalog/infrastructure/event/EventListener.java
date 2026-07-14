package br.com.dwnl.marketplace.catalog.infrastructure.event;

import br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity.EventEntity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventListener {
    private static final Logger logger = LoggerFactory.getLogger(EventListener.class);

    @PostPersist
    public void onEventCreated(EventEntity eventEntity){
        logger.info("Event created via @PostPersist {}", eventEntity);
    }

    @PostUpdate
    public void onEventUpdate(EventEntity eventEntity){
        logger.info("Event updated via @PostUpdate {}", eventEntity);
    }

    @PostRemove
    public void onEventDeleted(EventEntity eventEntity){
        logger.info("Event deleted via @PostRemove {}", eventEntity);
    }
}
