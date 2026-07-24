package br.com.dwnl.marketplace.ticketing.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.ticketing.domain.Event;
import br.com.dwnl.marketplace.ticketing.domain.EventRepository;
import br.com.dwnl.marketplace.ticketing.domain.Seat;
import br.com.dwnl.marketplace.ticketing.domain.Sector;
import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.EventEntity;
import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.SeatEntity;
import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.SectorEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostgresEventRepository implements EventRepository {
    private final EventCrudRepository eventCrudRepository;

    public PostgresEventRepository(EventCrudRepository eventCrudRepository) {
        this.eventCrudRepository = eventCrudRepository;
    }

    @Override
    public void save(Event event) {
        var sectors = event.getSeats().entrySet().stream()
                .map(entry -> {
                    Sector domainSector = entry.getKey();
                    List<Seat> domainSeats = entry.getValue();

                    var seats = domainSeats.stream()
                            .map(seat -> new SeatEntity(seat.getId(), seat.getCorrelationId().id()))
                            .toList();

                    return new SectorEntity(
                            domainSector.getId(),
                            domainSector.getCorrelationId().id(),
                            domainSector.getPrice(),
                            seats
                    );
                })
                .toList();

        var eventEntity = new EventEntity(
                event.getId(),
                event.getCorrelationId().id(),
                sectors);

        eventCrudRepository.save(eventEntity);
    }
}