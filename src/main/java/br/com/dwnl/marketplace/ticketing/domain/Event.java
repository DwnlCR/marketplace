package br.com.dwnl.marketplace.ticketing.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class Event {
    private UUID id;
    private EventId correlationId;
    private Map<Sector, List<Seat>> seats;

    public Event(String correlationId, Map<Sector, List<Seat>> seats){
        this.id = UUID.randomUUID();
        this.correlationId = new EventId(correlationId);
        this.seats = seats;
    }
}
