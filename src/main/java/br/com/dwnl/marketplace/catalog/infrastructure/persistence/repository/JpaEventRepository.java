package br.com.dwnl.marketplace.catalog.infrastructure.persistence.repository;

import br.com.dwnl.marketplace.catalog.domain.Event;
import br.com.dwnl.marketplace.catalog.domain.EventId;
import br.com.dwnl.marketplace.catalog.domain.EventRepository;
import br.com.dwnl.marketplace.catalog.infrastructure.persistence.entity.EventEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class JpaEventRepository implements EventRepository {
    private final EventEntityRepository eventEntityRepository;

    public JpaEventRepository(EventEntityRepository eventEntityRepository) {
        this.eventEntityRepository = eventEntityRepository;
    }

    @Override
    public List<Event> findAll() {
        var iterable = eventEntityRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(JpaEventRepository::mapper).toList();
    }

    private static Event mapper(EventEntity eventEntity){
        return new Event(new EventId(eventEntity.getId()), eventEntity.getTitle(), eventEntity.getDate(), Optional.empty());
    }
}
