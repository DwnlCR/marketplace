package br.com.dwnl.marketplace.common.infrastructure.event.dto;

import br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity.EventMetadataEntity;

import java.math.BigDecimal;
import java.util.List;

public record EventUpdated(String id, List<Sector> sectors) {

    public static EventUpdated from(EventMetadataEntity eventMetadata){
        List<Sector> sectors = eventMetadata.getSectors().stream()
                .map(s -> Sector.from(s, eventMetadata.getSeats()))
                .toList();

        return new EventUpdated(eventMetadata.getEventId().toString(), sectors);
    }

    public record Sector(String id, BigDecimal price, List<Seat> seats) {
        public static Sector from(EventMetadataEntity.Sector sector, List<EventMetadataEntity.Seat> allSeats) {
            List<Seat> sectorSeats = allSeats.stream()
                    .filter(seat -> seat.getSectorName().equals(sector.getName()))
                    .map(Seat::from).toList();

            return new Sector(sector.getName(), sector.getPrice(), sectorSeats);
        }

        public record Seat(String number){
            public static Seat from(EventMetadataEntity.Seat seat){
                return new Seat(seat.getCode());
            }
        }
    }
}
