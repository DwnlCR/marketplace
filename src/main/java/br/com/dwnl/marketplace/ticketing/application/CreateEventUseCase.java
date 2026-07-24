package br.com.dwnl.marketplace.ticketing.application;

import br.com.dwnl.marketplace.common.infrastructure.event.dto.EventUpdated;
import br.com.dwnl.marketplace.ticketing.domain.Event;
import br.com.dwnl.marketplace.ticketing.domain.EventRepository;
import br.com.dwnl.marketplace.ticketing.domain.Seat;
import br.com.dwnl.marketplace.ticketing.domain.Sector;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CreateEventUseCase {
    private final EventRepository eventRepository;

    public CreateEventUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void execute(EventUpdated event) {
        Map<Sector, List<Seat>> seats = event.sectors().stream()
                .collect(Collectors.toMap(
                        sector -> new Sector(sector.id(), sector.price()),
                        sector -> sector.seats().stream()
                                .map(seatDTO -> new Seat(seatDTO.number()))
                                .toList()
                ));
        eventRepository.save(new Event(event.id(), seats));
    }
}
