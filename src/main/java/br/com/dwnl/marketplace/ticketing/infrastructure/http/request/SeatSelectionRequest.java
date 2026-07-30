package br.com.dwnl.marketplace.ticketing.infrastructure.http.request;

import br.com.dwnl.marketplace.ticketing.domain.SeatId;

public record SeatSelectionRequest(String id) {
    public SeatId toInput(){
        return new SeatId(id);
    }
}
