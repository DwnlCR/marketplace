package br.com.dwnl.marketplace.ticketing.domain;

public class SeatAlreadyReservedException extends RuntimeException {
    public SeatAlreadyReservedException() {
        super("Seat is already reserved");
    }
}
