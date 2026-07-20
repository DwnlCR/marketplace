package br.com.dwnl.marketplace.catalog.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.catalog.domain.*;
import br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity.EventMetadataEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "event_metadata", key = "#eventId.id()")
    public Optional<EventMetadata> findByEventId(EventId eventId) {
        return eventMetadataEntityRepository.findByEventId(eventId.id()).map(MongoEventMetadataRepository::mapper);
    }

    @Override
    @CacheEvict(value = "event_metadata", key = "#eventId.id()")
    public void saveEventMetadata(EventId eventId, EventMetadata metadata){
        var entity = convertToEntity(eventId, metadata);
        eventMetadataEntityRepository.save(entity);
    }

    @Override
    @CacheEvict(value = "event_metadata", key = "#eventId.id()")
    public void deleteEventMetadata(EventId eventId){
        eventMetadataEntityRepository.deleteById(eventId.id().toString());
    }

    @CacheEvict(value = "event_metadata", allEntries = true)
    public void clearAllCache(){

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

    private EventMetadataEntity convertToEntity(EventId eventId, EventMetadata metadata) {

        EventMetadataEntity entity = new EventMetadataEntity();

        entity.setId(eventId.id().toString());
        entity.setEventId(eventId.id());

        entity.setEventDescription(metadata.eventDescription());
        entity.setTechnicalRequirements(metadata.technicalRequirements());

        entity.setSectors(
                metadata.seats().keySet().stream()
                        .map(sector -> new EventMetadataEntity.Sector(
                                sector.getId().name(),
                                sector.getPrice()
                        ))
                        .toList()
        );

        entity.setSeats(
                metadata.seats().entrySet().stream()
                        .flatMap(entry ->
                                entry.getValue().stream()
                                        .map(seat -> new EventMetadataEntity.Seat(
                                                seat.getId().seatNumber(),
                                                entry.getKey().getId().name()
                                        ))
                        )
                        .toList()
        );

        return entity;
    }
}
