package br.com.dwnl.marketplace.ticketing.domain;

public interface EventRepository {
    void save(Event event);
    boolean existsSeat(EventId eventId, SeatId seatId);
    boolean tryLockSeat(EventId eventId, SeatId seatId, CustomerId customerId);
}
