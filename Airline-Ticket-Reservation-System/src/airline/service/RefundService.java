package airline.service;
import airline.model.Booking;
import airline.enums.BookingStatus;
import airline.model.Payment;
import airline.model.PaymentStatus;
import airline.model.Refund;
import airline.model.RefundStatus;
import airline.repository.RefundRepository;
import airline.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import airline.singleton.PaymentManager;
import airline.singleton.FlightManager;
import airline.singleton.BookingManager;

public class RefundService {

    private final RefundRepository refundRepository;
    private final SeatService seatService;
    private final NotificationService notificationService;
    private final BookingRepository bookingRepository;

    public RefundService() {
        this.refundRepository = PaymentManager.getInstance().getRefundRepository();
        this.seatService = new SeatService(FlightManager.getInstance().getSeatRepository());
        this.notificationService = new NotificationService();
        this.bookingRepository = BookingManager.getInstance().getBookingRepository();
    }

    public RefundService(RefundRepository refundRepository,
                         SeatService seatService,
                         NotificationService notificationService,
                         BookingRepository bookingRepository) {

        this.refundRepository = refundRepository;
        this.seatService = seatService;
        this.notificationService = notificationService;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Process Refund
     */
    public Refund processRefund(Booking booking,
                                Payment payment) {

        if (booking == null) {
            throw new RuntimeException("Booking not found.");
        }

        if (payment == null) {
            throw new RuntimeException("Payment not found.");
        }

        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Refund cannot be processed.");
        }

        Refund refund = new Refund(
                generateRefundId(),
                booking,
                payment,
                payment.getAmount(),
                RefundStatus.COMPLETED,
                LocalDateTime.now()
        );

        refundRepository.save(refund);

        payment.setPaymentStatus(PaymentStatus.REFUNDED);

        booking.setBookingStatus(BookingStatus.CANCELLED);

        if (booking.getSeat() != null) {
            seatService.releaseSeat(booking.getSeat().getSeatNumber());
        }

        notificationService.notifyRefund(refund);
        return refund;
    }

    /**
     * Generate Refund Id
     */
    private String generateRefundId() {

        return "REF-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    /**
     * Search Refund
     */
    public Refund searchRefund(String refundId) {

        Refund refund = refundRepository.findRefund(refundId);

        if (refund == null) {
            throw new RuntimeException("Refund not found.");
        }

        return refund;
    }

    /**
     * Search By Booking
     */
    public Optional<Refund> searchByBooking(String bookingId) {

        return refundRepository.findByBooking(bookingId);
    }

    /**
     * Search By Payment
     */
    public Optional<Refund> searchByPayment(String paymentId) {

        return refundRepository.findByPayment(paymentId);
    }
    /**
     * Display All Refunds
     */
    public void displayRefunds() {

        List<Refund> refunds = refundRepository.findAll();

        if (refunds.isEmpty()) {
            System.out.println("No Refunds Available.");
            return;
        }

        refunds.forEach(System.out::println);
    }

    /**
     * Total Refunds
     */
    public long totalRefunds() {

        return refundRepository.count();
    }

    /**
     * Successful Refunds
     */
    public List<Refund> successfulRefunds() {

        return refundRepository.successfulRefunds();
    }

    /**
     * Failed Refunds
     */
    public List<Refund> failedRefunds() {

        return refundRepository.failedRefunds();
    }

    /**
     * Pending Refunds
     */
    public List<Refund> pendingRefunds() {

        return refundRepository.pendingRefunds();
    }

    /**
     * Total Refunded Amount
     */
    public double totalRefundAmount() {

        return refundRepository.totalRefundAmount();
    }

    /**
     * Highest Refund
     */
    public Refund highestRefund() {

        return refundRepository.highestRefund()
                .orElseThrow(() ->
                        new RuntimeException("No Refunds Found."));
    }

    /**
     * Lowest Refund
     */
    public Refund lowestRefund() {

        return refundRepository.lowestRefund()
                .orElseThrow(() ->
                        new RuntimeException("No Refunds Found."));
    }

    /**
     * Sort Refunds By Amount
     */
    public List<Refund> sortByAmount() {

        return refundRepository.sortByAmount();
    }

    /**
     * Sort Refunds By Date
     */
    public List<Refund> sortByDate() {

        return refundRepository.sortByDate();
    }

    /**
     * Refund Statistics
     */
    public void refundStatistics() {

        System.out.println("\n========== REFUND REPORT ==========");

        System.out.println("Total Refunds        : " + totalRefunds());

        System.out.println("Successful Refunds   : "
                + successfulRefunds().size());

        System.out.println("Failed Refunds       : "
                + failedRefunds().size());

        System.out.println("Pending Refunds      : "
                + pendingRefunds().size());

        System.out.println("Total Refund Amount  : ₹"
                + totalRefundAmount());

        if (totalRefunds() > 0) {

            System.out.println("Highest Refund       : ₹"
                    + highestRefund().getRefundAmount());

            System.out.println("Lowest Refund        : ₹"
                    + lowestRefund().getRefundAmount());
        }

        System.out.println("===================================");
    }
    /**
     * Cancel Booking
     */
    public Refund cancelBooking(String bookingId,
                                Payment payment) {

        Booking booking = bookingRepository.findBooking(bookingId);

        if (booking == null) {
            throw new RuntimeException("Booking not found.");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled.");
        }

        Refund refund = processRefund(
                booking,
                payment
        );

        return refund;
    }
    /**
     * Cancelled Bookings
     */
    public List<Booking> cancelledBookings() {

        return bookingRepository.findAll()
                .stream()
                .filter(booking ->
                        booking.getBookingStatus()
                                == BookingStatus.CANCELLED)
                .toList();
    }
    /**
     * Active Bookings
     */
    public List<Booking> activeBookings() {

        return bookingRepository.findAll()
                .stream()
                .filter(booking ->
                        booking.getBookingStatus()
                                == BookingStatus.BOOKED)
                .toList();
    }
    /**
     * Booking Statistics
     */
    public void bookingStatistics() {

        System.out.println("\n========== BOOKING REPORT ==========");

        System.out.println("Total Bookings      : "
                + bookingRepository.count());

        System.out.println("Active Bookings     : "
                + activeBookings().size());

        System.out.println("Cancelled Bookings  : "
                + cancelledBookings().size());

        System.out.println("====================================");
    }

}