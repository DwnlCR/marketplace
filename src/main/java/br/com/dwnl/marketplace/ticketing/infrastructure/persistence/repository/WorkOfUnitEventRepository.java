package br.com.dwnl.marketplace.ticketing.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.ticketing.domain.*;
import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.EventEntity;
import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.SeatEntity;
import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.SeatLockEntity;
import br.com.dwnl.marketplace.ticketing.infrastructure.persistence.entity.SectorEntity;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class WorkOfUnitEventRepository implements EventRepository {
    private final EventCrudRepository eventCrudRepository;
    private final RedisSeatLockRepository seatLockRepository;

    public WorkOfUnitEventRepository(EventCrudRepository eventCrudRepository, RedisSeatLockRepository seatLockRepository) {
        this.eventCrudRepository = eventCrudRepository;
        this.seatLockRepository = seatLockRepository;
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

    @Override
    public boolean existsSeat(EventId eventId, SeatId seatId) {
        return eventCrudRepository.existsByCorrelationIdAndSectors_Seats_CorrelationId(eventId.id(), seatId.id());
    }

    @Override
    public boolean tryLockSeat(EventId eventId, SeatId seatId, CustomerId customerId) {
        String lockId = eventId.id().toString() + ":" + seatId.id();
        if (seatLockRepository.existsById(lockId)) {
            return false;
        }

        var lock = new SeatLockEntity(lockId, customerId.id().toString(), Instant.now());
        seatLockRepository.save(lock);
        return true;
    }
}