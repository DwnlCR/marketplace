package br.com.dwnl.marketplace.catalog.infrastructure.event;

import br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity.EventMetadataEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class EventMetadataEventListener extends AbstractMongoEventListener<EventMetadataEntity> {
    private static final Logger logger = LoggerFactory.getLogger(EventMetadataEventListener.class);

    @Override
    public void onAfterSave(AfterSaveEvent<EventMetadataEntity> event){
        logger.info("Event metadata save via @onAfterSave {}", event.getDocument());
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<EventMetadataEntity> event){
        logger.info("Event metadata delete via @onAfterDelete {}", event.getDocument());
    }
}
