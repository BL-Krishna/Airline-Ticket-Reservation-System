package airline.service;
import airline.notification.NotificationService;
import airline.model.Booking;
import airline.model.Payment;
import airline.model.PaymentMethod;
import airline.model.PaymentStatus;
import airline.payment.PaymentStrategy;
import airline.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;


    public PaymentService(PaymentRepository paymentRepository,
                          NotificationService notificationService) {

        this.paymentRepository = paymentRepository;
        this.notificationService = notificationService;

    }
    public Payment processPayment(Booking booking,
                                  PaymentMethod paymentMethod,
                                  PaymentStrategy paymentStrategy) {

        if (booking == null) {
            throw new RuntimeException("Booking not found.");
        }

        boolean paymentResult =
                paymentStrategy.pay(booking.getTotalFare());

        PaymentStatus paymentStatus =
                paymentResult
                        ? PaymentStatus.SUCCESS
                        : PaymentStatus.FAILED;

        Payment payment =
                new Payment(
                        generatePaymentId(),
                        booking,
                        paymentMethod,
                        booking.getTotalFare(),
                        generateTransactionId(),
                        LocalDateTime.now(),
                        paymentStatus
                );

        paymentRepository.save(payment);

        if (paymentStatus == PaymentStatus.SUCCESS) {

            notificationService.notifyBooking(booking);

            notificationService.notifyPayment(payment);

        }

        return payment;

    }
    private String generatePaymentId() {

        return "PAY-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    private String generateTransactionId() {

        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();
    }

    public Payment searchPayment(String paymentId) {

        Payment payment =
                paymentRepository.findPayment(paymentId);

        if (payment == null) {
            throw new RuntimeException("Payment not found.");
        }

        return payment;
    }

    public Optional<Payment> searchTransaction(String transactionId) {

        return paymentRepository.findByTransactionId(transactionId);
    }

    public List<Payment> paymentsByBooking(String bookingId) {

        return paymentRepository.findByBookingId(bookingId);
    }
    public void displayPayments() {

        List<Payment> payments = paymentRepository.findAll();

        if (payments.isEmpty()) {
            System.out.println("No Payments Available.");
            return;
        }

        payments.forEach(System.out::println);
    }

    public long totalPayments() {

        return paymentRepository.count();
    }

    public List<Payment> successfulPayments() {

        return paymentRepository.findSuccessfulPayments();
    }

    public List<Payment> failedPayments() {

        return paymentRepository.findFailedPayments();
    }

    public double totalRevenue() {

        return paymentRepository.findSuccessfulPayments()
                .stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    public Payment highestPayment() {

        return paymentRepository.findAll()
                .stream()
                .max(java.util.Comparator.comparing(Payment::getAmount))
                .orElseThrow(() ->
                        new RuntimeException("No Payments Found."));
    }

    public Payment lowestPayment() {

        return paymentRepository.findAll()
                .stream()
                .min(java.util.Comparator.comparing(Payment::getAmount))
                .orElseThrow(() ->
                        new RuntimeException("No Payments Found."));
    }

    public List<Payment> sortByAmount() {

        return paymentRepository.findAll()
                .stream()
                .sorted(java.util.Comparator.comparing(Payment::getAmount))
                .toList();
    }

    public List<Payment> sortByDate() {

        return paymentRepository.findAll()
                .stream()
                .sorted(java.util.Comparator.comparing(Payment::getPaymentDate))
                .toList();
    }

    public void paymentStatistics() {

        System.out.println("\n=========== PAYMENT REPORT ===========");

        System.out.println("Total Payments        : " + totalPayments());

        System.out.println("Successful Payments   : "
                + successfulPayments().size());

        System.out.println("Failed Payments       : "
                + failedPayments().size());

        System.out.println("Total Revenue         : ₹"
                + totalRevenue());

        if (totalPayments() > 0) {

            System.out.println("Highest Payment       : ₹"
                    + highestPayment().getAmount());

            System.out.println("Lowest Payment        : ₹"
                    + lowestPayment().getAmount());
        }

        System.out.println("======================================");
    }

}