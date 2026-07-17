package br.com.dwnl.marketplace.catalog.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.catalog.domain.*;
import br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity.EventMetadataEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class MongoEventMetadataRepository implements EventMetadataRepository {
    private final EventMetadataEntityRepository eventMetadataEntityRepository;

    public MongoEventMetadataRepository(EventMetadataEntityRepository eventMetadataEntityRepository) {
        this.eventMetadataEntityRepository = eventMetadataEntityRepository;
    }

    @Override
    public Optional<EventMetadata> findByEventId(EventId eventId) {
        return eventMetadataEntityRepository.findByEventId(eventId.id()).map(MongoEventMetadataRepository::mapper);
    }

    private static EventMetadata mapper(EventMetadataEntity eventMetadataEntity){
        var sectors = eventMetadataEntity.getSectors().stream()
                .map(MongoEventMetadataRepository::mapper)
                .collect(Collectors.toMap(
                        sector -> sector.getId().name(),
                        Function.identity()
                ));

        var seats = eventMetadataEntity.getSeats().stream()
                .map(MongoEventMetadataRepository::mapper)
                .collect(Collectors.groupingBy(
                        seat -> sectors.get(seat.getSectorId().name())
                ));

        return new EventMetadata(
                eventMetadataEntity.getEventDescription(),
                eventMetadataEntity.getTechnicalRequirements(),
                seats
        );
    }

    private static Seat mapper(EventMetadataEntity.Seat seatEntity){
        return new Seat(new SeatId(seatEntity.getCode()), new SectorId(seatEntity.getSectorName()));
    }

    private static Sector mapper(EventMetadataEntity.Sector sectorEntity){
        return new Sector(new SectorId(sectorEntity.getName()), sectorEntity.getPrice());
    }
}
