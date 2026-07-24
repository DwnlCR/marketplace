package br.com.dwnl.marketplace.ticketing.domain;

import lombok.Data;

import java.util.UUID;

@Data
public class Seat {
    private UUID id;
    private SeatId correlationId;

    public Seat(String correlationId){
        this.id = UUID.randomUUID();
        this.correlationId = new SeatId(correlationId);
    }
}
