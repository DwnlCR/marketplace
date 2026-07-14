package br.com.dwnl.marketplace.catalog.domain;

import br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity.EventMetadataEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Optional;

@Getter
@Setter
public class Event {
    private EventId id;
    private String title;
    private Instant date;
    private Optional<EventMetadataEntity> metadata;

    public Event(EventId id, String title, Instant data, Optional<EventMetadataEntity> metadata){
        this.id = id;
        this.title = title;
        this.date = data;
        this.metadata = metadata;
    }
}
