package airline.service;

import airline.enums.SeatClass;
import airline.enums.SeatStatus;
import airline.model.Seat;
import airline.repository.SeatRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SeatService {

    private final SeatRepository repository;

    public SeatService(SeatRepository repository) {

        this.repository = repository;

        generateSeats();

    }

    // ===========================================
    // Generate Seats
    // ===========================================

    private void generateSeats() {

        generateFirstClassSeats();

        generateBusinessSeats();

        generateEconomySeats();

    }

    private void generateFirstClassSeats() {

        for (int row = 1; row <= 2; row++) {

            for (char seat = 'A'; seat <= 'F'; seat++) {

                repository.save(

                        new Seat(

                                row + "" + seat,

                                SeatClass.FIRST_CLASS

                        )

                );

            }

        }

    }

    private void generateBusinessSeats() {

        for (int row = 3; row <= 6; row++) {

            for (char seat = 'A'; seat <= 'F'; seat++) {

                repository.save(

                        new Seat(

                                row + "" + seat,

                                SeatClass.BUSINESS

                        )

                );

            }

        }

    }

    private void generateEconomySeats() {

        for (int row = 7; row <= 30; row++) {

            for (char seat = 'A'; seat <= 'F'; seat++) {

                repository.save(

                        new Seat(

                                row + "" + seat,

                                SeatClass.ECONOMY

                        )

                );

            }

        }

    }

    // ===========================================
    // Reserve Seat
    // ===========================================

    public boolean reserveSeat(String seatNumber) {

        Seat seat = repository.findSeat(seatNumber);

        if (seat == null) {

            return false;

        }

        if (seat.getSeatStatus() == SeatStatus.RESERVED) {

            return false;

        }

        seat.setSeatStatus(SeatStatus.RESERVED);

        return true;

    }

    // ===========================================
    // Release Seat
    // ===========================================

    public boolean releaseSeat(String seatNumber) {

        Seat seat = repository.findSeat(seatNumber);

        if (seat == null) {

            return false;

        }

        seat.setSeatStatus(SeatStatus.AVAILABLE);

        return true;

    }

    // ===========================================
    // Search Seat
    // ===========================================

    public Seat searchSeat(String seatNumber) {

        return repository.findSeat(seatNumber);

    }

    // ===========================================
    // Display All Seats
    // ===========================================

    public void displaySeats() {

        repository.findAll()

                .stream()

                .sorted(

                        Comparator.comparing(

                                Seat::getSeatNumber

                        )

                )

                .forEach(System.out::println);

    }

    // ===========================================
    // Available Seats
    // ===========================================

    public List<Seat> availableSeats() {

        return repository.findAll()

                .stream()

                .filter(

                        seat -> seat.getSeatStatus()

                                == SeatStatus.AVAILABLE

                )

                .collect(Collectors.toList());

    }

    // ===========================================
    // Reserved Seats
    // ===========================================

    public List<Seat> reservedSeats() {

        return repository.findAll()

                .stream()

                .filter(

                        seat -> seat.getSeatStatus()

                                == SeatStatus.RESERVED

                )

                .collect(Collectors.toList());

    }

    // ===========================================
    // Seats By Class
    // ===========================================

    public List<Seat> seatsByClass(

            SeatClass seatClass) {

        return repository.findAll()

                .stream()

                .filter(

                        seat -> seat.getSeatClass()

                                == seatClass

                )

                .collect(Collectors.toList());

    }

    // ===========================================
    // Count Available Seats
    // ===========================================

    public long availableSeatCount() {

        return availableSeats()

                .size();

    }

    // ===========================================
    // Count Reserved Seats
    // ===========================================

    public long reservedSeatCount() {

        return reservedSeats()

                .size();

    }

    // ===========================================
    // First Available Seat
    // ===========================================

    public Optional<Seat> firstAvailableSeat() {

        return availableSeats()

                .stream()

                .findFirst();

    }

}