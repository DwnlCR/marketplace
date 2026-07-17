package br.com.dwnl.marketplace.catalog.domain;

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
    private Optional<EventMetadata> metadata;

    public Event(EventId id, String title, Instant data, Optional<EventMetadata> metadata){
        this.id = id;
        this.title = title;
        this.date = data;
        this.metadata = metadata;
    }
}
