package br.com.dwnl.marketplace.catalog.application;

import br.com.dwnl.marketplace.catalog.domain.Event;
import br.com.dwnl.marketplace.catalog.domain.EventMetadataRepository;
import br.com.dwnl.marketplace.catalog.domain.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EventEnricher {
    private static final Logger logger = LoggerFactory.getLogger(BrowseShowcaseUseCase.class);

    private final EventMetadataRepository eventMetadataRepository;

    public EventEnricher(EventRepository eventRepository, EventMetadataRepository eventMetadataRepository) {
        this.eventMetadataRepository = eventMetadataRepository;
    }
    @Async
    public CompletableFuture<Event> enrich(Event event) {
        logger.info("Enriching event: {}", event);

        var metadata = eventMetadataRepository.findByEventId(event.getId());
        event.setMetadata(metadata);

        return CompletableFuture.completedFuture(event);
    }
}
