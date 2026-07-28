package airline.repository;

import airline.model.Payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaymentRepository {

    private final Map<String, Payment> payments = new HashMap<>();

    public void save(Payment payment) {
        payments.put(payment.getPaymentId(), payment);
    }

    public Payment findPayment(String paymentId) {
        return payments.get(paymentId);
    }

    public List<Payment> findAll() {
        return payments.values()
                .stream()
                .collect(Collectors.toList());
    }

    public boolean exists(String paymentId) {
        return payments.containsKey(paymentId);
    }

    public void delete(String paymentId) {
        payments.remove(paymentId);
    }

    public long count() {
        return payments.size();
    }

    public Optional<Payment> findByTransactionId(String transactionId) {

        return payments.values()
                .stream()
                .filter(payment ->
                        payment.getTransactionId()
                                .equalsIgnoreCase(transactionId))
                .findFirst();
    }

    public List<Payment> findByBookingId(String bookingId) {

        return payments.values()
                .stream()
                .filter(payment ->
                        payment.getBooking()
                                .getBookingId()
                                .equalsIgnoreCase(bookingId))
                .collect(Collectors.toList());
    }

    public List<Payment> findSuccessfulPayments() {

        return payments.values()
                .stream()
                .filter(payment ->
                        payment.getPaymentStatus().name().equals("SUCCESS"))
                .collect(Collectors.toList());
    }

    public List<Payment> findFailedPayments() {

        return payments.values()
                .stream()
                .filter(payment ->
                        payment.getPaymentStatus().name().equals("FAILED"))
                .collect(Collectors.toList());
    }

}